package com.xirurr;

import com.vaadin.ui.Window;

import java.io.File;

public abstract class AbstractWindow extends Window {
  public AbstractWindow() {
    super();
  }

  public AbstractWindow(String varName) {
    super(varName);
  }

  public void addToImageList(File file) {
  }

  public void renewImg() {
  }
}
