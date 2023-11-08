package org.example;

public class Entity {
   private final String name;
   private final String AddressStreet;
   private final String AddressCity;
   private final String NIP;

   public Entity(String name, String AddressStreet, String AddressCity, String NIP) {
      this.name = name;
      this.AddressStreet = AddressStreet;
      this.AddressCity = AddressCity;
      this.NIP = NIP;
   }

   public static Entity milkCorp(){
      return new Entity("Milk Corp.", "ul. Mleczna 1", "00-000 Mleczno", "000-000-00-00");
   }

   public String getName() {
      return name;
   }

   public String getAddressStreet() {
      return AddressStreet;
   }

   public String getAddressCity() {
      return AddressCity;
   }

   public String getNip() {
      return NIP;
   }
}
