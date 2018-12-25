package com.xirurr;


import com.vaadin.ui.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Fora")
@XmlAccessorType(XmlAccessType.FIELD)
public class Fora {
  @NonNull  @XmlElement  String name;

  public String getName() {
    return name;
  }

  public String getAreal() {
    return areal;
  }

  public Fora() {
  }

  @XmlElement List<URL> links = new ArrayList<>();
  @XmlElement String areal;
  @XmlElement boolean aggl;
  @XmlElement List<File> images = new ArrayList<>();


}
