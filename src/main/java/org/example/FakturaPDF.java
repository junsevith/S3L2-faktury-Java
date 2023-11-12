package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

/**
 * Klasa obsługująca zapis faktury do pliku pdf.
 * Aby zwiększyć spójność, wydzielono tę klasę z Faktura, wykorzystując Pure Fabrication.
 */
public class FakturaPDF implements FakturaWriter {

   /**
    * Font used in document.
    */
   private Font font;
   /**
    * Bold font used in document.
    */
   private Font fontBold;

   private String filename;


   /**
    * Decimal format used in document.
    */
   private final DecimalFormat decimalFormat = new DecimalFormat("0.00zł");

   public FakturaPDF() {
      InitializeFonts();
   }
   public FakturaPDF(String filename) {
      InitializeFonts();
      this.filename = filename;
   }

   /**
    * Initializes fonts.
    */
   private void InitializeFonts() {
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
         System.out.println("Cannot load bold font");
         fontBold = FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD);
      }
      this.font = font;
      this.fontBold = fontBold;
   }

   /**
    * Creates a pdf file from the data in the object.
    */
   @Override
   public void write(Faktura faktura) {
      final Document document = new Document();
      if (filename == null){
         filename = "faktura" + faktura.getId() + ".pdf";
      }
      try {
         PdfWriter.getInstance(document, Files.newOutputStream(java.nio.file.Paths.get(filename)));
      } catch (IOException | DocumentException e) {
         System.out.println("Cannot create pdf file: " + e.getMessage());
         return;
      }

      document.open();
      try {
         document.add(new Paragraph("Milk Corp. ///\n\n", FontFactory.getFont(FontFactory.TIMES, 20, Font.BOLD)));
         document.add(new Paragraph("Faktura VAT nr. " + faktura.getId() + "\n", fontBold));
         document.add(new Paragraph("   Data wystawienia: " + LocalDate.now() + "\n\n", font));
         document.add(sellerBuyerTable(faktura.getSeller(), faktura.getBuyer()));
         document.add(new Paragraph("\n\n"));
         document.add(productsTable(faktura.getElements()));
      } catch (DocumentException e) {
         System.out.println("Cannot write to pdf file: " + e.getMessage());
      }
      document.close();
   }

   /**
    * Creates a table with seller and buyer data.
    *
    * @return PdfPTable
    */
   private PdfPTable sellerBuyerTable(Entity seller, Entity buyer) {

      if (seller == null || buyer == null) {
         throw new IllegalArgumentException("Seller or buyer is not set");
      }

      final String sellerString = seller.getName() + "\n" + seller.getAdress() + "\n" + seller.getCity() + "\nNIP: " + seller.getNip();
      final String buyerString = buyer.getName() + "\n" + buyer.getAdress() + "\n" + buyer.getCity() + "\nNIP: " + buyer.getNip();
      final PdfPTable table = new PdfPTable(2);

      final PdfPCell sellerHeaderCell = new PdfPCell(new Phrase("Sprzedawca:", fontBold));
      final PdfPCell buyerHeaderCell = new PdfPCell(new Paragraph("Nabywca:", fontBold));
      buyerHeaderCell.setBorder(Rectangle.NO_BORDER);
      sellerHeaderCell.setBorder(Rectangle.NO_BORDER);
      buyerHeaderCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
      table.addCell(sellerHeaderCell);
      table.addCell(buyerHeaderCell);

      final PdfPCell sellerCell = new PdfPCell(new Paragraph(sellerString, font));
      final PdfPCell buyerCell = new PdfPCell(new Paragraph(buyerString, font));
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
   private PdfPTable productsTable(List<Element> elements) throws DocumentException {

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

      double suma = elements.stream().mapToDouble(Element::getPrice).sum();
      table.addCell(new PdfPCell(new Paragraph(decimalFormat.format(suma), fontBold)));
      table.addCell(new Paragraph("23%", fontBold));
      table.addCell(new Paragraph(decimalFormat.format(suma * 0.23), fontBold));
      table.addCell(new Paragraph(decimalFormat.format(suma * 1.23), fontBold));
      return table;
   }
}
