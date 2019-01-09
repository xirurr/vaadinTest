package com.xirurr;


import com.vaadin.annotations.Theme;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.io.FilenameUtils;
import server.droporchoose.UploadComponent;

import java.io.File;
import java.nio.file.Path;

@Theme("valo")
public class UploadWindow<T extends AbstractWindow> extends AbstractWindow {

  private static final long serialVersionUID = 6255201979868278965L;
  private final VerticalLayout mainLayout = new VerticalLayout();
  T origin;

  public UploadWindow(T originWindow) {
    super("UPLOAD");
    origin = originWindow;
    UploadComponent uploadComponent = new UploadComponent(this::uploadReceived);
    uploadComponent.setStartedCallback(this::uploadStarted);
    uploadComponent.setProgressCallback(this::uploadProgress);
    uploadComponent.setFailedCallback(this::uploadFailed);
    uploadComponent.setWidth(500, Unit.PIXELS);
    uploadComponent.setHeight(300, Unit.PIXELS);
    setModal(true);
    mainLayout.addComponent(uploadComponent);
    mainLayout.setMargin(true);
    setContent(mainLayout);
  }

  private void uploadReceived(String fileName, Path file) {
    Notification.show("Upload finished: " + fileName, Type.HUMANIZED_MESSAGE);
    String ext1 = FilenameUtils.getExtension(fileName);
    File dest = new File(file.toString() + "." + ext1);
    file.toFile().renameTo(dest);
    origin.addToImageList(dest);
  }

  private void uploadStarted(String fileName) {
    Notification.show("Upload started: " + fileName, Type.HUMANIZED_MESSAGE);
  }

  private void uploadProgress(String fileName, long readBytes, long contentLength) {
    Notification.show(String.format("Progress: %s : %d/%d", fileName, readBytes, contentLength),
            Type.TRAY_NOTIFICATION);
  }

  private void uploadFailed(String fileName, Path file) {
    Notification.show("Upload failed: " + fileName, Type.ERROR_MESSAGE);
  }


}
