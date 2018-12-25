package com.xirurr;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.*;

import java.io.File;
import java.util.List;

public class HorizontalLayout2  extends HorizontalLayout {
 /* HorizontalLayout2(List<File> list) {
    super();
    addComponents(list);
  }
  public void addComponents(List<File> list) {
    for (File var : list) {
      // File varFile = new File("D:\\\\YandexDisk\\\\GIT\\\\Foraminifera\\"+var); ///УДАЛИТЬ
      if (var.toString().contains(".emf")){
        continue;
      }
      FileResource varRes = new FileResource(var);
      Image varImage = new Image(null,varRes);
      //   System.out.println(varRes.getSourceFile().toString());
      varImage.setWidth("40");
      varImage.setHeight("40");
      super.addComponent(varImage);
    }
  }*/
  HorizontalLayout2(List<File> list) {
    super();
    addComponents(list);
  }

  public void addComponents(List<File> list) {
    for (File varFile : list) {
      String var = varFile.toString();
      String icoPath = var.replace(".jpeg","ico.jpeg");
      ThemeResource varRes = new ThemeResource("../../"+icoPath);
      Link combo = new Link(null,
              new ExternalResource("http://66160762e8ed.sn.mynetname.net:8080/VAADIN/"+var),"MYLINK",40,40,BorderStyle.DEFAULT);
      combo.setIcon(varRes);
      super.addComponent(combo);
    }
  }
}