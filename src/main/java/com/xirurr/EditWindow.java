package com.xirurr;


import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.*;
import phoraMain.Converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditWindow extends AbstractWindow {
  List<Fora> foraList;
  List<File> varImageList = new ArrayList<>();
  String resDIr = new WorkingExt().getBaseDir();
  Fora varFora;
  boolean ImageAdded;

  public EditWindow(Fora varFora2, List<Fora> foraList2, MyUI currentUI) {
    super(varFora2.getName());
    this.foraList = foraList2;
    this.varFora = varFora2;
    this.setWidth(300.0f, Unit.PIXELS);
    final FormLayout content = new FormLayout();
    TextField foraName = new TextField("Name");
    foraName.setValue(varFora.getName());
    ComboBox<String> arialCombo = new MyComboBox("Areal", varFora, foraList);
    Button addImageButton = new Button("Add image");
    addImageButton.addClickListener(e -> UI.getCurrent().getUI().getUI().addWindow(new UploadWindow<>(this)));

    CheckBox aggllState = new CheckBox("Агглютинированный");
    aggllState.setValue(varFora.isAggl());

    content.addComponents(foraName, arialCombo, aggllState, addImageButton);
    content.setMargin(true);
    setContent(content);

    Map<File, CheckBox> imagesToDelete = new HashMap<>();
    for (File var : varFora.getImages()) {
      HorizontalLayout lane = new HorizontalLayout();
      String icoPath = var.toString().replace(".jpeg", "ico.jpeg");
      ThemeResource varRes = new ThemeResource("../../" + icoPath);
      Link combo = new Link(null,
              new ExternalResource("http://66160762e8ed.sn.mynetname.net:8080/VAADIN/" + var), "MYLINK", 40, 40, BorderStyle.DEFAULT);
      combo.setIcon(varRes);
      CheckBox cb = new CheckBox("delete?");
      imagesToDelete.put(var, cb);
      lane.addComponents(combo, cb);
      content.addComponent(lane);
    }

    Button saveButton = new Button("SAVE");
    saveButton.addClickListener((e) -> checkAndSave(currentUI, foraName, arialCombo, aggllState, imagesToDelete));
    content.addComponent(saveButton);
  }

  private void checkAndSave(MyUI currentUI, TextField foraName, ComboBox<String> arialCombo, CheckBox aggllState, Map<File, CheckBox> imagesToDelete) {
    AtomicBoolean changes = new AtomicBoolean(false);
    imagesToDelete.forEach((file, value) -> {
      if (value.getValue()) {
        changes.set(true);
        varFora.removeFromImagesList(file);
        replaceToTrash(file, varFora.getName());
        file = new File(file.toString().replace(".jpeg", "ico.jpeg"));
        replaceToTrash(file, varFora.getName());
      }
    });
    if (ImageAdded) {
      convertAndSaveImagesFromList();
    }
    if (!foraName.getValue().equals(varFora.getName())) {
      changes.set(true);
      if (checkNameFromList(foraList, foraName.getValue())) {
        Window popUp = new AlertWindow("Name is already used");
        UI.getCurrent().getUI().getUI().addWindow(popUp);
      } else rename(varFora, foraName.getValue());
    }
    if (aggllState.getValue() != varFora.isAggl()) {
      changes.set(true);
      varFora.setAggl(aggllState.getValue());
    }
    if (!arialCombo.getSelectedItem().toString().equals(varFora.getAreal())) {
      changes.set(true);
      varFora.setAreal(arialCombo.getValue());
    }
    if (changes.get()) {
      new phoraMain.Serialiser().marshaToXML(Paths.get(resDIr+"/output/"
                      + varFora.getName() + "/" + "prop.xml")
              , varFora);
    }
    currentUI.addDataProvider(foraList);
    this.close();
  }

  private void replaceToTrash(File file, String oldName) {
    String varPath = resDIr + file.toString().replace(oldName, "trash");
    file = new File(resDIr + file.toString());
    File varFile = new File(varPath);
    try {
      Files.move(file.toPath(), varFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean checkNameFromList(List<Fora> foraList, String value) {
    for (Fora varFora : foraList) {
      if (varFora.getName().equals(value)) return true;
    }
    return false;
  }

  private void rename(Fora varFora, String newName) {
    Path pathToDir = Paths.get("D:\\YandexDisk\\GIT\\vaadinTests\\src\\main\\webapp\\VAADIN\\output\\" + varFora.getName());
    if (!Files.exists(pathToDir)) {
      try {
        Files.createDirectory(pathToDir);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    pathToDir.toFile().renameTo(new File("D:\\YandexDisk\\GIT\\vaadinTests\\src\\main\\webapp\\VAADIN\\output\\" + newName));

    List<File> filesToRemove = new ArrayList<>();
    List<File> filesToAdd = new ArrayList<>();
    for (File var : varFora.getImages()) {
      filesToRemove.add(var);
      var = new File(var.toString().replace(varFora.getName(), newName));
      filesToAdd.add(var);
    }
    for (File var : filesToRemove) {
      varFora.removeFromImagesList(var);
    }
    for (File var : filesToAdd) {
      varFora.addToImagesList(var);
    }
    varFora.setName(newName);
  }

  private HashSet<String> setListofArials(List<Fora> foraList, HashSet<String> arials) {
    foraList.forEach(o -> arials.add(o.getAreal()));
    arials.remove("");
    return arials;
  }

  @Override
  public void addToImageList(File file) {
    varImageList.add(file);
    ImageAdded = true;
  }

  private void convertAndSaveImagesFromList() {
    Converter conv = new Converter();
    try {
     varImageList = conv.converAndAddToFinalImageList(varFora.getName(),varImageList);
     for (File varFile:varImageList){
       varFora.addToImagesList(varFile);
     }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}