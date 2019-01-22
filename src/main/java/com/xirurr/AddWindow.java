package com.xirurr;

import com.vaadin.ui.*;
import org.jsoup.helper.StringUtil;
import phoraMain.Converter;
import phoraMain.Serialiser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AddWindow extends AbstractWindow {
  List<Fora> foraList;
  List<File> varImageList = new ArrayList<>();
  List<File> finalImagesList = new ArrayList<>();
  String resDIr = new WorkingExt().getResDir();


  public AddWindow(List<Fora> foraList,Grid mainGrid,MyUI currentUI) {
    this.foraList = foraList;
    this.setWidth(300.0f, Unit.PIXELS);
    Fora varFora = new Fora();
    final FormLayout content = new FormLayout();
    TextField foraName = new TextField("Name");
    ComboBox<String> arialCombo = new MyComboBox("Areal", varFora, foraList);
    CheckBox aggllState = new CheckBox("Агглютинированный");
    Button addImageButton = new Button("Add image");
    Button saveButton = new Button("Save");
    Serialiser ser = new Serialiser();

/*
    foraName.addValueChangeListener(o->{
      if (!checkName(foraName.getValue())) {
        content.addComponent(saveButton);
      }
    });*/

    addImageButton.addClickListener(e -> UI.getCurrent().getUI().getUI().addWindow(new UploadWindow<>(this)));
    saveButton.addClickListener(e -> {
              if (checkName(foraName.getValue())) {
                Window popUp = new AlertWindow("Name cannot be blank, and starts from SPACE, .+= and have symbols like +=[]:;«/?/\\:*?«<>| ");
                UI.getCurrent().getUI().getUI().addWindow(popUp);
              } else {

                Path dir = Paths.get(resDIr+"/output/" + foraName.getValue());
                if (Files.exists(dir)) {
                  Window popUp = new AlertWindow("This Fora already exists ");
                  UI.getCurrent().getUI().getUI().addWindow(popUp);
                } else {
                  try {
                    Files.createDirectory(dir);
                  } catch (IOException e1) {
                    e1.printStackTrace();
                  }
                  varFora.setName(foraName.getValue());
                  if (arialCombo.getValue() != null || !StringUtil.isBlank(arialCombo.getValue())) {
                    varFora.setAreal(arialCombo.getValue());
                  }
                  varFora.setAggl(aggllState.getValue());
                  if (varImageList.size() != 0) {
                    try {
                      Converter conv = new Converter();
                      finalImagesList = conv.converAndAddToFinalImageList(varFora.getName(),varImageList);
                    } catch (IOException e1) {
                      e1.printStackTrace();
                    }
                    varFora.setImages(finalImagesList);
                  }
                  foraList.add(varFora);
                  ser.marshaToXML(Paths.get(dir+"/prop.xml"),varFora);
                  currentUI.addDataProvider(foraList);
                  this.close();
                }
              }
            }
    );
    content.addComponents(foraName, arialCombo, aggllState, addImageButton, saveButton);
    content.setMargin(true);
    setContent(content);
  }

  private boolean checkName(String name) {
    String charsALL = ":;«/?\\*?«<>|";
    String charsFirst = ".+=";
    char[] varChars = charsALL.toCharArray();
    if (name == null || name.isEmpty() || StringUtil.isBlank(name)) return true;
    for (int i = 0; i < charsALL.length(); i++) {
      if (name.contains(Character.toString(varChars[i]))) return true;
    }
    varChars = charsFirst.toCharArray();
    for (int i = 0; i < varChars.length; i++) {
      if (name.indexOf(Character.toString(varChars[i])) == 0) {
        return true;
      }
    }
    return false;
  }


  @Override
  public void addToImageList(File file) {
    varImageList.add(file);
  }
}
