package org.example;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Handles console input.
 * Allows user to add products to faktura and then generates a pdf file.
 */
public class ConsoleHandler {
   /**
    * Connection to database.
    */
   private final Connection connection;
   /**
    * Product catalogue that provides access to products.
    */
   private final ProductCatalogue productCatalogue;
   /**
    * Faktura on which we are working.
    */
   private final FakturaPDF faktura = new FakturaPDF();
   /**
    * Reader to read input from.
    */
   private final Reader reader;

   /**
    * @param connection Connection to database
    * @param reader     Reader to read input from
    */
   public ConsoleHandler(final Connection connection, final Reader reader) {
      this.connection = connection;
      this.productCatalogue = new ProductCatalogue(connection);
      this.reader = reader;
   }

   /**
    * Starts the console handler.
    */
   public void start() {
      System.out.println("Dodawanie faktury");
      System.out.println("Wprowadź produkt i ilość (puste kończy wprowadzanie)");
      final Scanner scanner = new Scanner(reader);
      while (scanner.hasNextLine()) {
         final String string = scanner.nextLine();
         if (string.isEmpty()) {
            break;
         }
         Element element;
         try {
            element = parseInput(string);
         } catch (SQLException e) {
            System.out.println("Nie udało się pobrać produktu");
            continue;
         } catch (IllegalArgumentException e) {
            System.out.println("Niepoprawny format danych");
            continue;
         }
         faktura.addElement(element);
         System.out.println("Dodano " + element);
      }
      System.out.println("Podaj dane kupującego");
      System.out.println("Imię i nazwisko:");
      final String name = scanner.nextLine();
      System.out.println("Adres:");
      final String address = scanner.nextLine();
      System.out.println("NIP:");
      final String nip = scanner.nextLine();
      faktura.setBuyer(name, address, nip);

      System.out.println("Gotowe!");
      try {
         faktura.createPdf();
      } catch (IOException | DocumentException e) {
         System.out.println("Nie udało się wygenerować pliku PDF");
      } finally {
         System.out.println("Wygnerowano plik PDF");
      }

      scanner.close();
   }

   /**
    * Parses input string and returns new Element object.
    *
    * @param input Input string
    * @return Element
    * @throws SQLException             When product cannot be found matching input
    * @throws IllegalArgumentException When input is in wrong format
    */
   public Element parseInput(final String input) throws SQLException{
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
