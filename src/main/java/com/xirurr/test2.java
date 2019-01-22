package com.xirurr;

import phoraMain.Converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class test2 {
  public static void main(String[] args) throws IOException {
   /*Path file = Paths.get("C:\\1.pdf");
    Converter conv = new Converter();
    InputStream IS = new FileInputStream(file.toFile());
    conv.convertPdfToJpegAndSave(IS,"C:\\2.jpeg");
    List<File> fileList = new ArrayList<>();
    */
   String t = System.getProperty("java.io.tmpdir");

    System.out.println(t);

  }
}
