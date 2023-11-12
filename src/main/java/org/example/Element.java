package org.example;

/**
 * Klasa reprezentująca kilka produktów tego samego typu
 */
public class Element {
   /**
    * Product.
    */
   private final ProductCatalogue.Product product;
   /**
    * Quantity of product.
    */
   private final int quantity;
   /**
    * @param product  Product
    * @param quantity Quantity of product
    */
   public Element(final ProductCatalogue.Product product, final int quantity) {
      this.product = product;
      this.quantity = quantity;
   }
   /**
    * @return Product id
    */
   public int getProductId() {
      return product.getId();
   }

   /**
    * @return Product name
    */
   public String getName() {
      return product.getName();
   }

   /**
    * @return Product unit
    */
   public String getUnit() {
      return "szt.";
   }

   /**
    * @return Price of all products
    */
   public double getPrice() {
      return product.getPrice() * quantity;
   }

   /**
    * @return Price of single product
    */
   public double getSinglePrice() {
      return product.getPrice();
   }

   /**
    * @return Quantity of product
    */
   public int getQuantity() {
      return quantity;
   }

   /**
    * @return String representation of element
    */
   public String toString() {
      return product.getName() + " x " + quantity;
   }
}
