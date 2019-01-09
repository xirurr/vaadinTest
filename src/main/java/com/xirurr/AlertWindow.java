package com.xirurr;

import com.vaadin.ui.*;

import java.io.File;
import java.io.IOException;

public class AlertWindow extends AbstractWindow {
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


  @Override
  public void addToImageList(File file) {

  }
}
