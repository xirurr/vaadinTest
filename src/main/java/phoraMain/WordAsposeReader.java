package phoraMain;


import com.aspose.words.*;
import com.aspose.words.Shape;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class WordAsposeReader {
  Path fileDir;
  Document document;
  List<Fora> foraList = new ArrayList<>();
  Fora foraminifera;
  String areal = new String();
  Boolean aggl;
  int imageIndex = 0;
  String prevDir;
  Fora varFora;

  public void test() throws Exception {
    Converter converter = new Converter();
    for (int i = 1; i < 5; i++) {
      document = new Document(new FileInputStream("res/" + i + ".docx"));
      NodeCollection paragraphs = document.getChildNodes(NodeType.PARAGRAPH, true);
      aggl = false;
      for (Paragraph par : (Iterable<Paragraph>) paragraphs) {
        StringBuilder varName = new StringBuilder();
        List<URL> linklist = new ArrayList<>();
        List<File> imageFiles = new ArrayList<>();
        NodeCollection children = par.getChildNodes();
        List<ImageData> realImages = new ArrayList<>();
        for (Node child : (Iterable<Node>) children) {
          if (child.getNodeType() == NodeType.RUN) {
            if (child.getText().indexOf("Aspose.Words") > 0) {
              continue;
            }
            if (child.getText().equals(" ")) {
              continue;
            }
            if (child.getText().indexOf("HYPERLINK") > 0) {
              String var = clearVar(child.getText());
              linklist.add(new URL(var));
              continue;
            }
            if (child.getText().contains("АГГЛЮТИНИРОВАННЫЕ")) {
              aggl = true;
              continue;
            }
            if (!child.getText().equals("\f")) {
              varName = varName.append(child.getText()).append(" ");
            }
          }
          if (child.getNodeType() == NodeType.SHAPE) {
            Shape var = (Shape) child;
            if (var.hasImage()) {
              realImages.add(var.getImageData());
            }
          }
        }
        if (varName.toString().equals("СЕВЕРНАЯ АТЛАНТИКА ")) {
          areal = "СЕВЕРНАЯ АТЛАНТИКА";
          continue;
        }
        while (varName.toString().matches("[0-9].*") || varName.indexOf(".") == 0 || varName.indexOf(" ") == 0 || varName.indexOf("\f") == 0 || varName.indexOf("\u000B") == 0) {
          varName.delete(0, 1);
        }
        if (varName.length() != 0) {
          while (varName.lastIndexOf(" ") + 1 == varName.length()) {
            varName.delete(varName.length() - 1, varName.length());
          }
          String var = "output/" + varName.toString();
          prevDir = var;
          fileDir = Paths.get(var);
          if (!Files.exists(fileDir)) {
            Files.createDirectory(fileDir);
          }
          writeProperties(varName.toString(), fileDir, linklist, areal, aggl, imageFiles);
        }
        if (realImages.size() > 0) {
          for (ImageData var2 : realImages) {
            String imageFileName = java.text.MessageFormat.format(
                    "Aspose.Images.{0}{1}", imageIndex, FileFormatUtil.imageTypeToExtension(var2.getImageType()));
            if (imageFileName.contains(".emf")) {
              converter.converEmfToJpegANdSave(var2.toStream(), prevDir + "/" + imageFileName.replace(".emf", ".jpeg"));
              writeImageToClassAndMarshalIt(new File(prevDir + "/" + imageFileName.replace(".emf", ".jpeg")));
              imageIndex++;
            } else {
              if (imageFileName.contains(".png")) {
                imageFileName = imageFileName.replace(".png", ".jpeg");
              }
              converter.convertPNGtoJpegAndSave2(var2.toStream(), prevDir + "/" + imageFileName);
              writeImageToClassAndMarshalIt(new File(prevDir + "/" + imageFileName));
              imageIndex++;
            }
          }
        }
      }
    }
  }

  private void writeImageToClassAndMarshalIt(File file) {
    varFora.getImages().add(file);
    marshaToXML(Paths.get(fileDir + "/" + "prop.xml"));
  }

  private void writeProperties(String name, Path currentPath, List<URL> linklist, String areal, Boolean
          varaggl, List<File> imageFiles) {
    varFora = new Fora(name, linklist, areal, varaggl, imageFiles);
    Path file = Paths.get(currentPath + "/" + "prop.xml");
    if (!Files.exists(file)) {
      try {
        Files.createFile(file);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    marshaToXML(file);
  }

  private void marshaToXML(Path file) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Fora.class);
      Marshaller jaxbMarshaller = null;
      jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(varFora, file.toFile());
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }

  private String clearVar(String text) {
    text = text.replace(" HYPERLINK ", "");
    text = text.replaceAll("\"", "");
    return text;
  }


}

