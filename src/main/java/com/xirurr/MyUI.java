package com.xirurr;

import javax.servlet.annotation.WebServlet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.*;
import com.vaadin.ui.TextField;
import org.vaadin.grid.cellrenderers.editable.BooleanSwitchRenderer;


import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
  JAXBContext jaxbContext;
  XMLInputFactory xif = XMLInputFactory.newFactory();

  @Override
  protected void init(VaadinRequest vaadinRequest) {
    final VerticalLayout layout = new VerticalLayout();
    Grid<Fora> mainGrid = new Grid<>();
    List<Fora> foraList = getForaWithString();
    mainGrid.addColumn(Fora::getName).setCaption("Название").setExpandRatio(1);
    mainGrid.addColumn(Fora::getAreal).setCaption("Ареал").setExpandRatio(1);
    BooleanSwitchRenderer<Fora> booleanRenderer = new BooleanSwitchRenderer<>(Fora::setAggl,"","");
    mainGrid.addColumn(Fora::isAggl).setId("aggl").setCaption("Агглютинированный").setExpandRatio(1).setRenderer(booleanRenderer);
    mainGrid.addComponentColumn(person -> new HorizontalLayout2(person.getImages())).setSortable(false).setId("image").setCaption("image").setExpandRatio(8);

  //  mainGrid.setItems(foraList);
  //  mainGrid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
    mainGrid.setWidth("100%");


    mainGrid.setSelectionMode(Grid.SelectionMode.NONE);
    mainGrid.setDetailsVisible(Fora::getName,true);


    ListDataProvider<Fora> dataProvider = DataProvider.ofCollection(foraList);
    mainGrid.setDataProvider(dataProvider);
    mainGrid.getColumns().forEach(column -> column.setHidable(true));
    UI.getCurrent().getPushConfiguration().setPushMode(PushMode.DISABLED);

    TextField filterTextField = new TextField("Filter by name");
    filterTextField.setPlaceholder("name filter");
    filterTextField.addValueChangeListener(event -> {
      dataProvider.setFilter(Fora::getName, name -> {
        String nameLower = name == null ? ""
                : name.toLowerCase(Locale.ENGLISH);
        String filterLower = event.getValue()
                .toLowerCase(Locale.ENGLISH);
        return nameLower.contains(filterLower);
      });
    });
    layout.addComponents(filterTextField,mainGrid);
    setContent(layout);
  }

  private List<ForaWithImage> getForaWithImageList() {
    List<ForaWithImage> foraList = new ArrayList<>();
    Path mainDir = Paths.get("C:\\Users\\User\\Downloads\\vaadinTests\\src\\main\\webapp\\VAADIN\\output");
    ArrayList<String> directories = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(String.valueOf(mainDir.toFile())).list())));
    for (String var : directories) {
      Path varPath = Paths.get(mainDir+"\\"+var+"\\"+"prop.xml");
      ForaWithImage varForaWithImage = new ForaWithImage(getFora(varPath));
      foraList.add(varForaWithImage);
    }
    return foraList;
  }
  private List<Fora> getForaWithString() {
    List<Fora> foraList = new ArrayList<>();
    Path mainDir = Paths.get("D:\\YandexDisk\\GIT\\vaadinTests\\src\\main\\webapp\\VAADIN\\output");
    ArrayList<String> directories = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(String.valueOf(mainDir.toFile())).list())));
    for (String var : directories) {
      Path varPath = Paths.get(mainDir+"\\"+var+"\\"+"prop.xml");
      Fora varFora = getFora(varPath);
      foraList.add(varFora);
    }
    return foraList;
  }
  private Fora getFora(Path varPath) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Fora.class);
      xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
      StreamSource xml = new StreamSource(varPath.toFile());
      XMLStreamReader xsr = xif.createXMLStreamReader(xml);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (Fora) jaxbUnmarshaller.unmarshal(xsr);
    } catch (JAXBException | XMLStreamException e) {
      e.printStackTrace();
    }
    return null;
  }

  @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
  @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
  public static class MyUIServlet extends VaadinServlet {
  }
}
