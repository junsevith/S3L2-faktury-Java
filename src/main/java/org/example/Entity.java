package org.example;

import java.util.Map;

/*
 * Klasa reprezentująca podmiot z faktury tzn. sprzedawcę lub nabywcę.
 * Aby zwiększyć spójność, wydzielono tę klasę z Faktura
 */
public class Entity {
   private final String name;
   private final String Adress;
   private final String City;
   private final String NIP;

   public Entity(String name, String Adress, String AddressCity, String NIP) {
      this.name = name;
      this.Adress = Adress;
      this.City = AddressCity;
      this.NIP = NIP;
   }

   public static Entity milkCorp() {
      return new Entity("Milk Corp.", "ul. Mleczna 1", "00-000 Mleczno", "000-000-00-00");
   }

   public String getName() {
      return name;
   }

   public String getAdress() {
      return Adress;
   }

   public String getCity() {
      return City;
   }

   public String getNip() {
      return NIP;
   }

   public Map<String, String> getData() {
      return Map.of(
            "name", name,
            "address", Adress,
            "city", City,
            "nip", NIP
      );
   }
}
