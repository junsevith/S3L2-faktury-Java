package org.example;

import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Prosta aplikacja konsolowa pozwalająca na dodawanie i zapisywanie faktur.
 * Aby uniknąć niskiej spójności, zastosowano pure fabrication, i stworzono klasę ElementParser,
 * która zajmuje się zamienianiem stringa na obiekt Element.
 * Zastosowano Dependency inversion i utworzono interfejs FakturaWriter, który jest implementowany przez klasy FakturaPDF i FakturaBD.
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
      System.out.println("Podaj nazwę pliku: (puste - domyślna nazwa)");
      final String filename = scanner.nextLine();

      System.out.println("Gotowe!");

      FakturaWriter fakturaWriter;
      if (filename.isEmpty()) {
         fakturaWriter = new FakturaPDF();
      } else {
         fakturaWriter = new FakturaPDF(filename);
      }
      fakturaWriter.write(faktura);



      scanner.close();
   }

}
