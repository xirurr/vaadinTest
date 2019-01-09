package phoraMain;

import com.aspose.imaging.Image;
import com.aspose.imaging.coreexceptions.ImageLoadException;
import com.aspose.imaging.fileformats.emf.EmfImage;
import com.aspose.imaging.imageoptions.EmfRasterizationOptions;
import com.aspose.imaging.imageoptions.JpegOptions;
import com.aspose.imaging.system.io.FileStream;
import com.xirurr.WorkingExt;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Converter {
  //String resDIr = new WorkingExt().getFromTempDir();
  public void converEmfToJpegANdSave(InputStream IS, String savePath) {
    EmfImage image = (EmfImage) Image.load(IS);
    try {
      FileStream outputStream = new FileStream(savePath, com.aspose.imaging.system.io.FileMode.Create);
      try {
        if (!image.getHeader().getEmfHeader().getValid()) {
          throw new ImageLoadException("The file" + savePath + " is not valid");
        }
        EmfRasterizationOptions emfRasterization = new EmfRasterizationOptions();
        emfRasterization.setPageWidth(image.getWidth());
        emfRasterization.setPageHeight(image.getHeight());
        emfRasterization.setBackgroundColor(com.aspose.imaging.Color.getWhiteSmoke());
        JpegOptions jpegOptions = new JpegOptions();
        jpegOptions.setVectorRasterizationOptions(emfRasterization);
        image.save(outputStream.toOutputStream(), jpegOptions);
        BufferedImage varImg = ImageIO.read(new File(savePath));
        writeImageAndIcon(savePath, varImg);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        outputStream.close();
        outputStream.dispose();
      }
    } finally {
      image.dispose();
    }
  }

  public void convertPdfToJpegAndSave(InputStream IS, String savePath) {
    try {
      PDDocument document = PDDocument.load(IS);
      PDFRenderer pdfRenderer = new PDFRenderer(document);
      BufferedImage image = pdfRenderer.renderImageWithDPI(0, 175, ImageType.RGB);
      writeImageAndIcon(savePath, image);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void convertOtherToJpegAndSave(InputStream IS, String savePath) {
    try {
      BufferedImage bufferedImage = ImageIO.read(IS);
      BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
              bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
      newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
      writeImageAndIcon(savePath, newBufferedImage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private BufferedImage createIcon(BufferedImage bufferedImage, int size) {
    BufferedImage outputImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, size, size);
    Graphics2D g2d = outputImage.createGraphics();
    g2d.drawImage(outputImage, 0, 0, size, size, null);
    g2d.dispose();
    return outputImage;
  }

  private void writeImageAndIcon(String savePath, BufferedImage newBufferedImage) throws IOException {
    BufferedImage icon = createIcon(newBufferedImage, 50);
    if (!Files.exists(Paths.get(savePath))) {
      ImageIO.write(newBufferedImage, "jpg", new File(savePath));
    }
    ImageIO.write(icon, "jpg", new File(savePath.replace(".jpeg", "ico.jpeg")));
  }

  public List<File> converAndAddToFinalImageList(String name, List<File> varImageList) throws IOException {
    List<File> finalImagesList = new ArrayList<>();
    for (File varFile : varImageList) {
      if (varFile.toString().toLowerCase().contains(".jpg")){
        varFile.renameTo(new File(varFile.toString().replace(".jpg",".jpeg")));
        varFile = new File(varFile.toString().replace(".jpg",".jpeg"));
      }
      if (varFile.toString().toLowerCase().contains("temp")) {
        String baseDir = new WorkingExt().getBaseDir();
        String dest = new WorkingExt().getFromTempDir(varFile.toString()).replace("/VAADIN/", "/VAADIN/output/" + name + "/");
        InputStream IS = new FileInputStream(varFile);
        String ext1 = FilenameUtils.getExtension(varFile.toString());
        switch (ext1) {
          case "png":
            dest = dest.replace(".png", ".jpeg");
            convertOtherToJpegAndSave(IS, dest);
            dest = dest.replace(baseDir, "");
            finalImagesList.add(new File(dest));
            break;
          case "pdf":
            dest = dest.replace(".pdf", ".jpeg");
            convertPdfToJpegAndSave(IS, dest);
            dest = dest.replace(baseDir, "");
            finalImagesList.add(new File(dest));
            break;
          case "jpeg":
            convertOtherToJpegAndSave(IS, dest);
            dest = dest.replace(baseDir, "");
            finalImagesList.add(new File(dest));
            break;

          case "emf":
            dest = dest.replace(".emf", ".jpeg");
            converEmfToJpegANdSave(IS, dest);
            dest = dest.replace(baseDir, "");
            finalImagesList.add(new File(dest));
            break;
        }
      } else finalImagesList.add(varFile);
    }
    return finalImagesList;
  }

}

