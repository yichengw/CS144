package edu.ucla.cs.cs144;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class BidComparator implements Comparator<Bid>{
   public BidComparator() {}

   @Override
   public int compare(final Bid bid1, final Bid bid2) {
      final DateFormat format = new SimpleDateFormat("MMM-dd-yy kk:mm:ss");
      Date date1 = null;
      Date date2 = null;
      try {
         date1 = format.parse(bid1.getTime());
         date2 = format.parse(bid2.getTime());
      } catch (ParseException e) {
         e.printStackTrace();
         System.exit(2);
      }
      return date1.compareTo(date2);
   }
}