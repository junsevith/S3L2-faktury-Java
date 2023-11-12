import org.example.*;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

class FakturaTests {
   Connection connection;
   ProductCatalogue productCatalogue;

   @Test
   void testConnection() throws SQLException {
      connection = DriverManager.getConnection("jdbc:mysql://localhost/milk_corp", "root", "");
      Assertions.assertNotNull(connection);
   }

   @Test
   void testProductCatalogue() throws SQLException {
      testConnection();
      productCatalogue = new ProductCatalogue(connection);
      Assertions.assertNotNull(productCatalogue.getProduct(1));
      assert !productCatalogue.getAllProducts().isEmpty();
   }

   @Test
   void testFaktura() throws SQLException {
      testProductCatalogue();
      final Faktura faktura = new Faktura();
      faktura.setBuyer("Jan Kowalski", "ul. Kowalska 1", "00-000 Kowalewo", "000-000-00-00");
      faktura.addElement(new Element(productCatalogue.getProduct(1), 2));
      faktura.addElement(new Element(productCatalogue.getProduct(2), 1));
      faktura.addElement(new Element(productCatalogue.getProduct(3), 1));
      assert faktura.getBuyer().getName().equals("Jan Kowalski");
      assert faktura.getBuyer().getAdress().equals("ul. Kowalska 1");
      assert faktura.getBuyer().getCity().equals("00-000 Kowalewo");
      assert faktura.getElements().size() == 3;
      assert Objects.equals(faktura.getElements().get(0).getName(), "cheese");
      assert Objects.equals(faktura.getElements().get(1).getName(), "yoghurt");
      assert Objects.equals(faktura.getElements().get(2).getName(), "milk");
   }

   @Test
   void testPDF() throws SQLException {
      testProductCatalogue();
      final Faktura faktura = new Faktura();
      faktura.setBuyer("Jan Kowalski", "ul. Kowalska 1", "00-000 Kowalewo", "000-000-00-00");
      faktura.addElement(new Element(productCatalogue.getProduct(1), 2));
      faktura.addElement(new Element(productCatalogue.getProduct(2), 1));
      faktura.addElement(new Element(productCatalogue.getProduct(3), 1));
      new FakturaPDF("test").write(faktura);
   }

   @Test
   void testConsoleHandler() throws SQLException, IOException {
      testConnection();
      final Reader reader = new StringReader("1 2\n2 1\n3 1\n\nJan Kowalski\nul. Kowalska 1\n 00-000 Kowalewo\n000-000-00-00\ntest\n");
      final ConsoleHandler consoleHandler = new ConsoleHandler(connection, reader);
      consoleHandler.start();
      reader.close();
   }
}
