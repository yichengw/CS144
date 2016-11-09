package edu.ucla.cs.cs144;

public class Location {
   private String name;
   private String latitude;
   private String longitude;

   public Location(final String name) {
      this.name = name;
      this.latitude = null;
      this.longitude = null;
   }

   public Location(final String name, final String latitude, final String longitude) {
      this.name = name;
      this.latitude = latitude;
      this.longitude = longitude;
   }

   public String getName() {
      return this.name;
   }

   public String getLatitude() {
      if (this.latitude != null && this.latitude != "") {
         return this.latitude; 
      } else {
         return "null";
      }
   }

   public String getLongitude() {
      if (this.longitude != null && this.longitude != "") {
         return this.longitude; 
      } else {
         return "null";
      }
   }

   public void setLatitude(final String latitude) {
      this.latitude = latitude;
   }

   public void setLongitude(final String longitude) {
      this.longitude = longitude;
   }
}