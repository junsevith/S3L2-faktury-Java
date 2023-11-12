package org.example;

import java.sql.Connection;
/*
   * Klasa używana do zapisywania faktury w bazie danych
   * Indirection: Klasa jest pośrednikiem do bazy danych.
   * Aby zwiększyć spójność, wydzielono tę klasę z Faktura, wykorzystując Pure Fabrication
 */

public class FakturaBD implements FakturaWriter{

      Connection connection;

      public FakturaBD(Connection connection) {
         this.connection = connection;
      }
      @Override
      public void write(Faktura faktura) {
         System.out.println("Zapisano fakturę w bazie danych");

      }
}
