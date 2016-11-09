package edu.ucla.cs.cs144;

public class Item {
   private String id;
   private String name;
   private String[] categories;
   private String currently;
   private String buy_price;
   private String first_bid;
   private String number_of_bids;
   private Bid[] bids;
   private String started;
   private String ends;
   private Location location;
   private String country;
   private User seller;
   private String description;

   public Item(final String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String[] getCategories() {
      return this.categories;
   }

   public String getCurrently() {
      return this.currently;
   }

   public String getBuyPrice() {
      return this.buy_price;
   }

   public String getFirstBid() {
      return this.first_bid;
   }

   public String getNumberOfBids() {
      return this.number_of_bids;
   }

   public Bid[] getBids() {
      return this.bids;
   }

   public String getStarted() {
      return this.started;
   }

   public String getEnds() {
      return this.ends;
   }

   public Location getLocation() {
      return this.location;
   }

   public String getCountry() {
      return this.country;
   }

   /*
    * Getter only used for retrieving address for google maps, has to be javascript string escaped
    * Also a concatenation of both the location name and country is used as diff cities might be associated with diff countries.
    */
   public String getAddress() {
      return StringUtils.escapeStringJS(this.location.getName() + " " + this.country);
   }

   public User getSeller() { 
      return this.seller;
   }

   public String getDescription() {
      return this.description;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public void setCategories(final String[] categories) {
      this.categories = categories;
   }

   public void setCurrently(final String currently) {
      this.currently = currently;
   }

   public void setBuyPrice(final String buy_price) {
      this.buy_price = buy_price;
   }

   public void setFirstBid(final String first_bid) {
      this.first_bid = first_bid;
   }

   public void setNumberOfBids(final String number_of_bids) {
      this.number_of_bids = number_of_bids;
   }

   public void setBids(final Bid[] bids) {
      this.bids = bids;
   }

   public void setStarted(final String started) {
      this.started = started;
   }

   public void setEnds(final String ends) {
      this.ends = ends;
   }

   public void setLocation(final Location location) {
      this.location = location;
   }

   public void setCountry(final String country) {
      this.country = country;
   }

   public void setSeller(final User seller) { 
      this.seller = seller;
   }

   public void setDescription(final String description) {
      this.description = description;
   }
}