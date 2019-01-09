package com.xirurr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class WorkingExt {
  public Path genRandomDir() {
    String uuid = UUID.randomUUID().toString();
    String resDIr = this.getClass().getClassLoader().getResource("").
            getPath().replace("WEB-INF/classes/", "/VAADIN/").replaceFirst("/", "");
    Path file = Paths.get(resDIr + "\\output\\temp\\" + uuid);
    if (!Files.exists(file)) {
      try {
        Files.createDirectory(file);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  public String getBaseDir() {
    String resDIr = this.getClass().getClassLoader().getResource("").
            getPath();
    if (resDIr.contains("WEB-INF/classes/")) {
      resDIr = resDIr.replace("WEB-INF/classes/", "VAADIN/");
    }
    if (resDIr.contains("target/classes")) {
      resDIr = resDIr.replace("target/classes/", "src/main/webapp/VAADIN/");
    }
    if (resDIr.indexOf("/")==0){
      resDIr = resDIr.replaceFirst(Character.toString(resDIr.charAt(0)),"");
    }
    return resDIr;
  }

  public String getFromTempDir() {
    return System.getProperty("java.io.tmpdir").replace(System.getProperty("java.io.tmpdir"), getBaseDir());
  }
  public String getFromTempDir(String varName) {
    return varName = varName.replace(System.getProperty("java.io.tmpdir"), getBaseDir());
  }
}
