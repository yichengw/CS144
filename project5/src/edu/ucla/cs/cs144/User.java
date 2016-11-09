package edu.ucla.cs.cs144;

public class User {
   private String name;
   private String rating;
   private Location location;
   private String country;
   
   public User(final String name, final String rating) {
      this.name = name;
      this.rating = rating;
      this.location = null;
      this.country = null;
   }
   
   public User(final String name, final String rating, final Location location, final String country) {
      this.name = name;
      this.rating = rating;
      this.location = location;
      this.country = country;
   }
   
   public String getName() {
      return this.name;
   }
   
   public String getRating() {
      return this.rating;
   }
   
   public Location getLocation() {
      return this.location;
   }
   
   public String getCountry() {
      return this.country;
   }
}