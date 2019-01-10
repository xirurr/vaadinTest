package com.xirurr;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.grid.cellrenderers.editable.BooleanSwitchRenderer;
import phoraMain.Serialiser;

import javax.servlet.annotation.WebServlet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;


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
  Grid<Fora> mainGrid;
  List<Fora> foraList;
  ListDataProvider<Fora> dataProvider;
  TextField filterTextField;
  String resDIr = new WorkingExt().getBaseDir();


  @Override
  protected void init(VaadinRequest vaadinRequest) {
    // Navigator navigator = new Navigator(this, this);

    final VerticalLayout layout = new VerticalLayout();
    chechCreateTrashDir(resDIr);
    mainGrid = new Grid<>();
    foraList = getForaWithString();
    mainGrid.addColumn(Fora::getName).setCaption("Название").setExpandRatio(1);
    mainGrid.addColumn(Fora::getAreal).setCaption("Ареал").setExpandRatio(1);
    BooleanSwitchRenderer<Fora> booleanRenderer = new BooleanSwitchRenderer<>(Fora::setAggl, "", "");
    mainGrid.addColumn(Fora::isAggl).setId("aggl").setCaption("Агглютинированный").setExpandRatio(1).setRenderer(booleanRenderer);
    mainGrid.addComponentColumn(person -> new HorizontalLayout2(person.getImages())).setSortable(false).setId("image").setCaption("image").setExpandRatio(8);
    mainGrid.addComponentColumn(this::buildExtendButton);
    mainGrid.addComponentColumn(this::buildDeleteButton);
    HashSet<String> arials = new HashSet<>();
    //   arials = setListofArials(foraList, arials);
    //  ComboBox<String> comboBox = new ComboBox<>("Ареал");


    //  mainGrid.setItems(foraList);
    //  mainGrid.setColumnResizeMode(ColumnResizeMode.SIMPLE);
    mainGrid.setWidth("100%");
    mainGrid.setSelectionMode(Grid.SelectionMode.NONE);

    mainGrid.getColumns().forEach(column -> column.setHidable(true));
    UI.getCurrent().getPushConfiguration().setPushMode(PushMode.DISABLED);
    filterTextField = new TextField("Filter by name");
    filterTextField.setPlaceholder("name filter");
    addDataProvider(foraList);


    Button addButton = new Button("ADD NEW");
    addButton.addClickListener(e -> addPerson());
    layout.addComponents(filterTextField, mainGrid, addButton);
    setContent(layout);
  }

  private void chechCreateTrashDir(String resDIr) {
    Path trashDIr = Paths.get(resDIr + "/output/trash/");
    if (!Files.exists(trashDIr)) {
      try {
        Files.createDirectory(trashDIr);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  void addDataProvider(List<Fora> foraList) {
    this.foraList = foraList;
    dataProvider = DataProvider.ofCollection(this.foraList);
    mainGrid.setDataProvider(dataProvider);
    addFilterByName(dataProvider);
    setFilterField(filterTextField.getValue());
  }

  void addFilterByName(ListDataProvider<Fora> dataProvider) {
    filterTextField.addValueChangeListener(event -> dataProvider.setFilter(Fora::getName, name -> {
      String nameLower = name == null ? ""
              : name.toLowerCase(Locale.ENGLISH);
      String filterLower = event.getValue()
              .toLowerCase(Locale.ENGLISH);
      return nameLower.contains(filterLower);
    }));
    //filterTextField.setValue(filter);
  }


  public void setFilterField(String filter) {
    filterTextField.setValue("");
    filterTextField.setValue(filter);
  }

  private <V extends Component> V buildExtendButton(Fora varFora) {
    Button button = new Button(VaadinIcons.EDIT);
    button.addStyleName(ValoTheme.BUTTON_SMALL);
    button.addClickListener(e -> {
      extendPerson(varFora);
    });
    return (V) button;
  }

  private <V extends Component> V buildDeleteButton(Fora fora) {
    Button button = new Button(VaadinIcons.DEL);
    button.addStyleName(ValoTheme.BUTTON_SMALL);
    button.addClickListener(e -> {
      askDeletePerson(fora);
    });
    return (V) button;
  }

  private void askDeletePerson(Fora fora) {
    AlertWindow sub = new AlertWindow("DO YOU WANT TO DELETE ", fora, this);
    UI.getCurrent().addWindow(sub);
  }

  private void extendPerson(Fora p) {
    EditWindow sub = new EditWindow(p, foraList, this);
    UI.getCurrent().addWindow(sub);
  }

  public void deletePerson(Fora varFora) throws IOException {
    Path varPath = Paths.get(resDIr + "output/" + varFora.getName() + "/");
    Path destPath = Paths.get(resDIr + "output/trash/" + varFora.getName() + "/");
    List<File> varImages = new ArrayList<>();
    try {
      Files.move(varPath, destPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (DirectoryNotEmptyException e) {
      for (int i = 0; i < 100; i++) {
        Path varDestPath;
        try {
          varDestPath = Paths.get(destPath.toString().replace(varFora.getName(), varFora.getName() + i));
          Files.move(varPath, varDestPath, StandardCopyOption.REPLACE_EXISTING);
          break;
        } catch (DirectoryNotEmptyException xe) {
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    for (File varFile : varFora.getImages()) {
      varFile = new File(varFile.toString().replace("output\\" + varFora.getName(), "output\\trash\\" + varFora.getName()));
      varImages.add(varFile);
    }
    varFora.setImages(varImages);
    Serialiser ser = new Serialiser();
    ser.marshaToXML(Paths.get(destPath.toString() + "/prop.xml"), varFora);
    foraList.remove(varFora);
    addDataProvider(foraList);
  }

  private void addPerson() {
    AddWindow sub = new AddWindow(foraList, mainGrid, this);
    UI.getCurrent().addWindow(sub);
  }

  private List<Fora> getForaWithString() {
    List<Fora> foraList = new ArrayList<>();
    Path mainDir = Paths.get(resDIr + "/output");

    if (Files.exists(mainDir)) {
      ArrayList<String> directories = new ArrayList<>(Arrays.asList(((mainDir.toFile())).list()));

      for (String var : directories) {
        if (var.equals("trash")) continue;
        Path varPath = Paths.get(mainDir + "/" + var + "/" + "prop.xml");
        if (Files.exists(varPath)) {
          Fora varFora = getFora(varPath);
          foraList.add(varFora);
        }
      }
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
