package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public class ElementParser {
   private final ProductCatalogue productCatalogue;

   /**
    * @param connection Connection to database needed for retrieving products
    */
   public ElementParser(Connection connection) {
      this.productCatalogue = new ProductCatalogue(connection);
   }

   /**
    * Creates an element from given input.
    * @param input Input in format "product_id quantity" or "product_name quantity"
    * @return Element created from input
    * @throws SQLException Product with given id or name does not exist
    */
   public Element parse(final String input) throws SQLException{
      final String[] split = input.split(" ");
      if (split.length != 2) {
         throw new IllegalArgumentException("Niepoprawny format danych");
      }

      ProductCatalogue.Product product;
      try {
         product = productCatalogue.getProduct(Integer.parseInt(split[0]));
      } catch (NumberFormatException e) {
         product = productCatalogue.getProduct(split[0]);
      }

      final int quantity = Integer.parseInt(split[1]);

      return new Element(product, quantity);
   }
}
