package com.xirurr;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;

import java.io.File;
import java.util.List;

public class HorizontalLayout2 extends HorizontalLayout {
  HorizontalLayout2(List<File> list) {
    super();
    addComponents(list);
  }

  public void addComponents(List<File> list) {
    for (File varFile : list) {
      String var = varFile.toString();
      String icoPath = var.replace(".jpeg", "ico.jpeg");
      Resource icoRes2 = new ExternalResource("http://66160762e8ed.sn.mynetname.net:8080/static/" + icoPath);
      Resource imgRes2 = new ExternalResource("http://66160762e8ed.sn.mynetname.net:8080/static/" + var);
      Link combo = new Link(null,
              imgRes2, "MYLINK", 40, 40, BorderStyle.DEFAULT);
      combo.setIcon(icoRes2);
      super.addComponent(combo);
    }
  }
}
