package com.xirurr;

import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;

import java.io.File;
import java.io.IOException;

public class AlertWindow extends AbstractWindow {
  VerticalLayout ver;
  public AlertWindow(String alert) {
    final FormLayout content = new FormLayout();
    Label lb = new Label(alert);
    content.addComponent(lb);
    setContent(content);
    setModal(true);
  }

  public AlertWindow(String alert, Fora element, MyUI currentUI) {
    final VerticalLayout content = new VerticalLayout();
    final HorizontalLayout horContent = new HorizontalLayout();
    Label lb = new Label(alert + element.getName());
    Button bt = new Button("DELETE");
    Button dontbt = new Button("DONT!!!!");
    horContent.addComponents(bt, dontbt);
    content.addComponents(lb, horContent);
    setContent(content);
    setModal(true);

    bt.addClickListener(e -> {
      try {
        currentUI.deletePerson(element);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      close();
    });
    dontbt.addClickListener(e -> close());
  }

  public AlertWindow(){
    ver = new VerticalLayout();
    setContent(ver);
  }
  public void addImage(File varFile){
    Resource res = new FileResource(varFile);
    Image img = new Image(null,res);
    img.setWidth("60");
    img.setHeight("60");
    ver.addComponent(img);
  }
}
