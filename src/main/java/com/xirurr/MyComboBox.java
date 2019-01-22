package com.xirurr;

import com.vaadin.ui.ComboBox;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class MyComboBox extends com.vaadin.ui.ComboBox {
  HashSet<String> arials;

  public MyComboBox(String caption, Fora varFora, List<Fora> foraList) {
    super(caption);
    if (arials == null) {
      arials = new HashSet<>();
    }
    this.arials = setListofArials(foraList, arials);

    this.setItems(arials);
    this.setSelectedItem(varFora.getAreal());
    this.setWidth(100.0f, Unit.PERCENTAGE);
    HashSet<String> finalArials = arials;
    ComboBox.NewItemProvider<String> itemHandler = newItemCaption -> {
      String finalNewItemCaption = newItemCaption + "";
      boolean newItem = foraList.stream().noneMatch(data ->
              data.getAreal() != null && data.getAreal().equalsIgnoreCase(finalNewItemCaption));
      String newCountryData = null;
      if (newItem) {
        newCountryData = newItemCaption;
        finalArials.add(newCountryData);
        this.setItems(finalArials);
        this.setSelectedItem(newCountryData);
      }
      return Optional.ofNullable(newCountryData);
    };
    this.setNewItemProvider(itemHandler);
  }

  private HashSet<String> setListofArials(List<Fora> foraList, HashSet<String> varArials) {
    foraList.forEach(o ->
            {
              if (o.getAreal() == null) {
              }
              else varArials.add(o.getAreal());
            }
    );
    if (varArials.size() != 0) {
      varArials.remove("");
      varArials.remove(null);
    }
    return varArials;
  }
}
