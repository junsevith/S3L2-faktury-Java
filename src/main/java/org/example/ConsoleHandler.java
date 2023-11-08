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

   private final ElementParser elementParser;
   /**
    * Faktura on which we are working.
    */
   private final Faktura faktura = new Faktura();
   /**
    * Reader to read input from.
    */
   private final Reader reader;

   /**
    * @param connection Connection to database
    * @param reader     Reader to read input from
    */
   public ConsoleHandler(final Connection connection, final Reader reader) {
      this.elementParser = new ElementParser(connection);
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
            element = elementParser.parse(string);
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
      System.out.println("Ulica:");
      final String street = scanner.nextLine();
      System.out.println("Miasto:");
      final String city = scanner.nextLine();
      System.out.println("NIP:");
      final String nip = scanner.nextLine();
      faktura.setBuyer(name, street, city, nip);

      System.out.println("Gotowe!");
      try {
         new FakturaPDF(faktura).createPdf();
      } catch (IOException | DocumentException e) {
         System.out.println("Nie udało się wygenerować pliku PDF");
      } finally {
         System.out.println("Wygnerowano plik PDF");
      }

      scanner.close();
   }

}
