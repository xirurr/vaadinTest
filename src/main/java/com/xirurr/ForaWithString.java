package com.xirurr;


import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import lombok.*;

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
@NoArgsConstructor
@XmlRootElement(name = "ForaWithImage")
@XmlAccessorType(XmlAccessType.FIELD)
public class ForaWithString {
  @NonNull  @XmlElement  String name;
  @XmlElement List<URL> links = new ArrayList<>();
  @XmlElement String areal;
  @XmlElement boolean aggl;
  @XmlElement List<String> images = new ArrayList<>();

  public String getName() {
    return name;
  }

  public String getAreal() {
    return areal;
  }

  public ForaWithString(Fora varFora) {
    this.name =varFora.getName();
    this.areal = varFora.getAreal();
    this.aggl=varFora.isAggl();
    this.links= varFora.getLinks();
    this.images=getImageFromFIleList(varFora.getImages());
  }

  private List<String> getImageFromFIleList(List<File> images) {
    List<String> varList= new ArrayList<>();
    for (File varFile:images){
     String img = varFile.toString();
      varList.add(img);
    }
    return varList;
  }


}
