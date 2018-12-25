package phoraMain;

import com.aspose.imaging.Image;
import com.aspose.imaging.coreexceptions.ImageLoadException;
import com.aspose.imaging.fileformats.emf.EmfImage;
import com.aspose.imaging.imageloadoptions.MetafileLoadOptions;
import com.aspose.imaging.imageoptions.EmfRasterizationOptions;
import com.aspose.imaging.imageoptions.JpegOptions;
import com.aspose.imaging.system.io.FileStream;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Converter {
  public void converEmfToJpegANdSave(InputStream IS, String savePath) {
    EmfImage image = (EmfImage) Image.load(IS, new MetafileLoadOptions(true));
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
        writeImageAndIcon(savePath,varImg);
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

  public void convertPNGtoJpegAndSave2(InputStream IS, String savePath) {
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

  private void writeImageAndIcon(String savePath, BufferedImage newBufferedImage) throws IOException {
    BufferedImage icon = createIcon(newBufferedImage,50);
    ImageIO.write(newBufferedImage, "jpg", new File(savePath));
    ImageIO.write(icon, "jpg", new File(savePath.replace(".jpeg","ico.jpeg")));
  }

  public BufferedImage createIcon(BufferedImage bufferedImage, int size) {
    BufferedImage outputImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, size, size);
 //   BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = outputImage.createGraphics();
    g2d.drawImage(outputImage, 0, 0, size, size, null);
    g2d.dispose();
    return outputImage;
  }
}

