package phoraMain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Fora")
@XmlAccessorType(XmlAccessType.FIELD)
public class Fora {
  @XmlElement
  @NonNull String name;
  @XmlElement
  List<URL> links = new ArrayList<>();
  @XmlElement
  String areal;
  @XmlElement
  boolean aggl;
  @XmlElement
  List<File> images = new ArrayList<>();
  public Fora() {
  }
}
