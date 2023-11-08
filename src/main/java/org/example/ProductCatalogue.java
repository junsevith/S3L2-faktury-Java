package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides access to products from database.
 */
public class ProductCatalogue {
   /**
    * Connection to database.
    */
   private final Connection connection;

   /**
    * @param connection Connection to database needed for retrieving products
    */
   public ProductCatalogue(final Connection connection) {
      this.connection = connection;
   }

   /**
    * @param id Product id
    * @return Product with given id
    * @throws SQLException When product with given id does not exist
    */
   public Product getProduct(final Integer id) throws SQLException {
      final Statement statement = connection.createStatement();
      final ResultSet resultSet = statement.executeQuery("SELECT * FROM products WHERE product_id = " + id);
      resultSet.next();
      final Product product = new Product(id, resultSet.getString("name"), Double.parseDouble(resultSet.getString("price")));
      statement.close();
      resultSet.close();
      return product;
   }

   /**
    * @param name Product name
    * @return Product with given name
    * @throws SQLException When product with given name does not exist
    */
   public Product getProduct(final String name) throws SQLException {
      final Statement statement = connection.createStatement();
      final ResultSet resultSet = statement.executeQuery("SELECT * FROM products WHERE name = \"" + name + "\"");
      resultSet.next();
      final Product product = new Product(Integer.parseInt(resultSet.getString("product_id")), name, Double.parseDouble(resultSet.getString("price")));
      statement.close();
      resultSet.close();
      return product;
   }

   /**
    * @return String representation of all available products
    * @throws SQLException When there is a problem with database
    */
   public String getAllProducts() throws SQLException {
      final Statement statement = connection.createStatement();
      final ResultSet resultSet = statement.executeQuery("SELECT * FROM products");
      final StringBuilder stringBuilder = new StringBuilder();
      while (resultSet.next()) {
         stringBuilder.append(resultSet.getString("product_id")).append(' ').append(resultSet.getString("name")).append(' ').append(resultSet.getString("price")).append('\n');
      }
      statement.close();
      resultSet.close();
      return stringBuilder.toString();
   }

   /**
    * Product.
    */
   public static class Product {
      /**
       * Product id.
       */
      private final int id;
      /**
       * Product name.
       */
      private final String name;
      /**
       * Product price.
       */
      private final double price;

      /**
       * @param id    Product id
       * @param name  Product name
       * @param price Product price
       */
      private Product(final Integer id, final String name, final double price) {
         this.id = id;
         this.name = name;
         this.price = price;
      }

      /**
       * @return Product id
       */
      public int getId() {
         return id;
      }

      /**
       * @return Product name
       */
      public String getName() {
         return name;
      }

      /**
       * @return Product price
       */
      public Double getPrice() {
         return price;
      }

      /**
       * @return String representation of product
       */

      public String toString() {
         return "Product [id=" + id + ", name=" + name + ", price=" + price + "]";
      }

   }
}
