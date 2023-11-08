package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a faktura.
 */
public class Faktura {
   /**
    * Faktura id.
    */
   Integer fakturaId = ThreadLocalRandom.current().nextInt(100_000, 999_999);
   /**
    * Seller name.
    */
   String sellerName = "Milk Corp.";
   /**
    * Seller address.
    */
   String sellerAddress = "ul. Mleczna 1\n00-000 Mleczno";
   /**
    * Seller NIP.
    */
   String sellerNip = "000-000-00-00";
   /**
    * Buyer name.
    */
   String buyerName;
   /**
    * Buyer address.
    */
   String buyerAddress;
   /**
    * Buyer NIP.
    */
   String buyerNip;
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
    * @param name    Buyer name
    * @param address Buyer address
    * @param nip     Buyer NIP
    */
   public void setBuyer(final String name, final String address, final String nip) {
      buyerName = name;
      buyerAddress = address;
      buyerNip = nip;
   }


}
