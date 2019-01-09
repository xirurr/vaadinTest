package phoraMain;

import com.xirurr.Fora;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.nio.file.Path;

public class Serialiser {
  public void marshaToXML(Path file, Fora varFora) {
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
}
