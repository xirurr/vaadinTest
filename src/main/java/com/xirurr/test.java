package com.xirurr;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class test {
  public static void main(String[] args) {
    try {
      new test().pngToJpeg();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private List<Fora> getForaList() {
    List<Fora> foraList = new ArrayList<>();
    Path mainDir = Paths.get("C:\\Users\\User\\Downloads\\vaadinTests\\output\\");
    ArrayList<String> directories = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(String.valueOf(mainDir.toFile())).list())));
    for (String var : directories) {
      Path varPath = Paths.get(mainDir + "\\" + var + "\\" + "prop.xml");
      foraList.add(getFora(varPath));
    }
    return foraList;
  }

  private Fora getFora(Path varPath) {
    try {
      XMLInputFactory xif = XMLInputFactory.newFactory();
      JAXBContext jaxbContext = JAXBContext.newInstance(Fora.class);
      xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
      StreamSource xml = new StreamSource(varPath.toFile());
      XMLStreamReader xsr = xif.createXMLStreamReader(xml);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (Fora) jaxbUnmarshaller.unmarshal(xsr);
    } catch (JAXBException | XMLStreamException e) {
      e.printStackTrace();
    }
    return null;
  }

  void pngToJpeg() throws IOException {
    List<Fora> varFora = getForaList();
    BufferedImage bufferedImage;
    List<File> varList;
    for (Fora var : varFora) {
      varList = var.getImages();
      System.out.println(varList);
      Path orig = Paths.get(varList.get(0).getParent()+"/"+"prop.xml");
      Path newOrig= Paths.get(orig.toString().replace("output","outputTest"));
      Files.createDirectories(newOrig.getParent());
      Files.copy(orig,newOrig);

      for (File varFile : varList) {
        if (varFile.toString().contains("emf")){
          Path varP = Paths.get(varFile.toString().replace("output","outputTest"));
          Files.createDirectories(varP.getParent());
            Files.copy(varFile.toPath(),varP);
            continue;
        }
        bufferedImage = ImageIO.read(varFile);
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
        String varString = varFile.toString();
        varString = varString.replace("output", "outputTest");
        varString = varString.replace("png", "jpeg");
        varFile = new File(varString);
        System.out.println(varString);
        // write to jpeg file
        if (!Files.exists(varFile.toPath())){
          Files.createDirectories(varFile.toPath().getParent());
          Files.createFile(varFile.toPath());

        }
        ImageIO.write(newBufferedImage, "jpg", varFile);
      }


    }
  }
}
