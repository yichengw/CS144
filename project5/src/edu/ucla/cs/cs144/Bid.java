package edu.ucla.cs.cs144;

public class Bid {
   User buyer;
   String when;
   String price;
   
   public Bid(final User buyer,  final String price,final String when) {
      this.buyer = buyer;
      this.when = when;
      this.price = price;
   }
      public String getTime() {
      return this.when;
   }
   
   public String getAmount() {
      return this.price;
   }
   public User getBidder() {
      return this.buyer;
   }
   

}