package phoraMain;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    try {
      new WordAsposeReader().test();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
