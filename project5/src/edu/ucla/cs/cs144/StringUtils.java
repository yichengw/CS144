package edu.ucla.cs.cs144;

public final class StringUtils {
   private StringUtils() {}

   public static String escapeStringJS(final String text) {
      String result = "";
      for (int i = 0; i < text.length(); i++) {
         result += escapeCharJS(text.charAt(i));
      }
      return result;
   }

   private static String escapeCharJS(final char c) {
      switch (c) {
      case '\\':
         return "\\\\";
      case '\'':
         return "\\\'";
      case '\"':
         return "\\\"";
      default:
         return ""+c;
      }
   }
}