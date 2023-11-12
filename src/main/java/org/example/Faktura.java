package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa przechowywująca dane faktury.
 * Creator:
 * Tworzy 2 obiekty Entity, ponieważ bezpośrednio ich używa
 */
public class Faktura {
   /**
    * Faktura id.
    */
   private final Integer fakturaId = ThreadLocalRandom.current().nextInt(100_000, 999_999);

   /**
    * Seller data.
    */
   private final Entity seller = Entity.milkCorp();

   private Entity buyer;
   /**
    * Elements of faktura.
    */
   List<Element> elements = new ArrayList<>();

   /**
    * Adds element to faktura.
    *
    * @param element Element to add
    */
   public void addElement(final Element element) {
      elements.add(element);
   }

   /**
    * Sets buyer data.
    *
    * @param name   Buyer name
    * @param street Buyer address
    * @param city   Buyer city
    * @param nip    Buyer NIP
    */
   public void setBuyer(final String name, final String street, final String city, final String nip) {
      buyer = new Entity(name, street, city, nip);
   }

   public int getId() {
      return fakturaId;
   }

   public Entity getSeller() {
      return seller;
   }

   public Entity getBuyer() {
      return buyer;
   }

   public Map<String, String> getSellerData() {
      return seller.getData();
   }

   public Map<String, String> getBuyerData() {
      return buyer.getData();
   }

   public List<Element> getElements() {
      return Collections.unmodifiableList(elements);
   }

   public double getSum() {
      return elements.stream().mapToDouble(Element::getPrice).sum();
   }

}
