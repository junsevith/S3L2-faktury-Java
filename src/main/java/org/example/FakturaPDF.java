package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;

/**
 * Handles saving faktura to pdf file.
 */
public class FakturaPDF extends Faktura {

   /**
    * Font used in document.
    */
   private final Font font;
   /**
    * Bold font used in document.
    */
   private final Font fontBold;

   /**
    * Decimal format used in document.
    */
   private final DecimalFormat decimalFormat = new DecimalFormat("0.00zł");

   public FakturaPDF() {
      super();
      BaseFont bf;
      BaseFont bfBold;
      Font font;
      Font fontBold;
      try {
         bf = BaseFont.createFont("ArchivoNarrow-Regular.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
         font = new Font(bf, 12);
      } catch (DocumentException | IOException e) {
         System.out.println("Cannot load font");
         font = FontFactory.getFont(FontFactory.TIMES, 12, Font.NORMAL);
      }
      try {
         bfBold = BaseFont.createFont("ArchivoNarrow-Bold.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
         fontBold = new Font(bfBold, 12);
      } catch (DocumentException | IOException e) {
         System.out.println("Cannot load font");
         fontBold = FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD);
      }
      this.font = font;
      this.fontBold = fontBold;
   }

   /**
    * Creates a pdf file from the data in the object.
    *
    * @throws IOException       When file cannot be created
    * @throws DocumentException When cant add elements to document
    */
   public void createPdf() throws IOException, DocumentException {
      final Document document = new Document();
      PdfWriter.getInstance(document, Files.newOutputStream(java.nio.file.Paths.get("faktura" + fakturaId + ".pdf")));

      document.open();
      document.add(new Paragraph("Milk Corp. ///\n\n", FontFactory.getFont(FontFactory.TIMES, 20, Font.BOLD)));
      document.add(new Paragraph("Faktura VAT nr. " + fakturaId + "\n", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD)));
      document.add(new Paragraph("   Data wystawienia: " + LocalDate.now() + "\n\n", font));
      document.add(sellerBuyerTable());
      document.add(new Paragraph("\n\n"));
      document.add(productsTable());
      document.close();
   }

   /**
    * Creates a table with seller and buyer data.
    *
    * @return PdfPTable
    */
   private PdfPTable sellerBuyerTable() {
      if (buyerName == null || buyerAddress == null || buyerNip == null) {
         throw new IllegalArgumentException("Buyer data not set");
      }

      final String seller = sellerName + "\n" + sellerAddress + "\nNIP: " + sellerNip;
      final String buyer = buyerName + "\n" + buyerAddress + "\nNIP: " + buyerNip;
      final PdfPTable table = new PdfPTable(2);

      final PdfPCell sellerHeaderCell = new PdfPCell(new Phrase("Sprzedawca:", fontBold));
      final PdfPCell buyerHeaderCell = new PdfPCell(new Paragraph("Nabywca:", fontBold));
      buyerHeaderCell.setBorder(Rectangle.NO_BORDER);
      sellerHeaderCell.setBorder(Rectangle.NO_BORDER);
      buyerHeaderCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
      table.addCell(sellerHeaderCell);
      table.addCell(buyerHeaderCell);

      final PdfPCell sellerCell = new PdfPCell(new Paragraph(seller, font));
      final PdfPCell buyerCell = new PdfPCell(new Paragraph(buyer, font));
      sellerCell.setBorder(Rectangle.NO_BORDER);
      buyerCell.setBorder(Rectangle.NO_BORDER);
      buyerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
      table.addCell(sellerCell);
      table.addCell(buyerCell);
      table.setWidthPercentage(100);

      return table;
   }

   /**
    * Creates a table with products from elements.
    *
    * @return PdfPTable
    * @throws DocumentException When table cannot be created
    */
   private PdfPTable productsTable() throws DocumentException {
      if (elements.isEmpty()) {
         throw new IllegalArgumentException("No elements added");
      }

      final PdfPTable table = new PdfPTable(9);
      table.setWidths(new int[]{1, 4, 1, 1, 1, 1, 1, 1, 1});
      table.setWidthPercentage(100);
      table.addCell(new Paragraph("Lp.", fontBold));
      table.addCell(new Paragraph("Nazwa towaru lub usługi", fontBold));
      table.addCell(new Paragraph("Jedn. miary", fontBold));
      table.addCell(new Paragraph("Ilość", fontBold));
      table.addCell(new Paragraph("Cena jedn.", fontBold));
      table.addCell(new Paragraph("Wartość netto", fontBold));
      table.addCell(new Paragraph("Stawka VAT", fontBold));
      table.addCell(new Paragraph("Kwota VAT", fontBold));
      table.addCell(new Paragraph("Wartość brutto", fontBold));
      int lp = 1;
      for (final Element element : elements) {
         table.addCell(new Phrase(String.valueOf(lp++), font));
         table.addCell(new Phrase(element.getName(), font));
         table.addCell(new Phrase(element.getUnit(), font));
         table.addCell(new Phrase(String.valueOf(element.getQuantity()), font));
         table.addCell(new Phrase(String.valueOf(decimalFormat.format(element.getSinglePrice())), font));
         table.addCell(new Phrase(String.valueOf(decimalFormat.format(element.getPrice())), font));
         table.addCell(new Phrase("23%", font));
         table.addCell(new Phrase(String.valueOf(decimalFormat.format(element.getPrice() * 0.23)), font));
         table.addCell(new Phrase(String.valueOf(decimalFormat.format(element.getPrice() * 1.23)), font));
      }

      final PdfPCell fullPriceCell = new PdfPCell(new Paragraph("Razem: ", fontBold));
      fullPriceCell.setColspan(5);
      fullPriceCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
      fullPriceCell.setBorder(Rectangle.NO_BORDER);
      table.addCell(fullPriceCell);
      table.addCell(new PdfPCell(new Paragraph(decimalFormat.format(elements.stream().mapToDouble(Element::getPrice).sum()), fontBold)));
      table.addCell(new Paragraph("23%", fontBold));
      table.addCell(new Paragraph(decimalFormat.format(elements.stream().mapToDouble(Element::getPrice).sum() * 0.23), fontBold));
      table.addCell(new Paragraph(decimalFormat.format(elements.stream().mapToDouble(Element::getPrice).sum() * 1.23), fontBold));
      return table;
   }

   /**
    * Saves data to database.
    *
    * @param connection Database connection
    */
   private void saveToDatabase(final Connection connection) {
      try {
         connection.setAutoCommit(false);
         PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO faktury (faktura_id, seller_name, seller_address, seller_nip, buyer_name, buyer_address, buyer_nip) VALUES (?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setInt(1, fakturaId);
         preparedStatement.setString(2, sellerName);
         preparedStatement.setString(3, sellerAddress);
         preparedStatement.setString(4, sellerNip);
         preparedStatement.setString(5, buyerName);
         preparedStatement.setString(6, buyerAddress);
         preparedStatement.setString(7, buyerNip);
         preparedStatement.executeUpdate();
         preparedStatement = connection.prepareStatement("INSERT INTO faktury_produkty (faktura_id, product_id, quantity) VALUES (?, ?, ?)");
         for (final Element element : elements) {
            preparedStatement.setInt(1, fakturaId);
            preparedStatement.setInt(2, element.getProductId());
            preparedStatement.setInt(3, element.getQuantity());
            preparedStatement.executeUpdate();
         }
         connection.commit();
         preparedStatement.close();
      } catch (SQLException e) {
         System.out.println("Nie udało się zapisać faktury do bazy danych: " + e.getMessage());
      }

   }
}
