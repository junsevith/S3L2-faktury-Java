package org.example;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main {


   public static void main(String[] args) throws DocumentException, IOException, SQLException {
      final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/milk_corp", "root", "");
      final ConsoleHandler consoleHandler = new ConsoleHandler(connection, new java.io.InputStreamReader(System.in));
      consoleHandler.start();
      connection.close();


   }


}