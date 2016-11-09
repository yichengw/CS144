package edu.ucla.cs.cs144;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public final class XMLParser {
   private static DocumentBuilder builder;

   private XMLParser () {}

   private static class MyErrorHandler implements ErrorHandler {
      public void warning(final SAXParseException exception)
            throws SAXException {
         fatalError(exception);
      }

      public void error(final SAXParseException exception)
            throws SAXException {
         fatalError(exception);
      }

      public void fatalError(final SAXParseException exception)
            throws SAXException {
         exception.printStackTrace();
         System.out.println("There should be no errors " +
               "in the supplied XML files.");
         System.exit(3);
      }
   }

   /* Non-recursive (NR) version of Node.getElementsByTagName(...)
    */
   private static Element[] getElementsByTagNameNR(final Element e, final String tagName) {
      Vector< Element > elements = new Vector< Element >();
      Node child = e.getFirstChild();
      while (child != null) {
         if (child instanceof Element && child.getNodeName().equals(tagName))
         {
            elements.add( (Element)child );
         }
         child = child.getNextSibling();
      }
      Element[] result = new Element[elements.size()];
      elements.copyInto(result);
      return result;
   }

   /* Returns the first subelement of e matching the given tagName, or
    * null if one does not exist. NR means Non-Recursive.
    */
   private static Element getElementByTagNameNR(final Element e, final String tagName) {
      Node child = e.getFirstChild();
      while (child != null) {
         if (child instanceof Element && child.getNodeName().equals(tagName))
            return (Element) child;
         child = child.getNextSibling();
      }
      return null;
   }

   /* Returns the text associated with the given element (which must have
    * type #PCDATA) as child, or "" if it contains no text.
    */
   private static String getElementText(final Element e) {
      if (e.getChildNodes().getLength() == 1) {
         Text elementText = (Text) e.getFirstChild();
         return elementText.getNodeValue();
      }
      else
         return "";
   }

   /* Returns the text (#PCDATA) associated with the first subelement X
    * of e with the given tagName. If no such X exists or X contains no
    * text, "" is returned. NR means Non-Recursive.
    */
   private static String getElementTextByTagNameNR(final Element e, final String tagName) {
      Element elem = getElementByTagNameNR(e, tagName);
      if (elem != null)
         return getElementText(elem);
      else
         return "";
   }

   /*
    * Convert item xml to item bean object
    */
   public static Item convertXMLToItem(final String xml_data) {
      Document doc = null;
      try {
         doc = XMLParser.builder.parse(new ByteArrayInputStream(xml_data.getBytes()));
         //doc = XMLParser.builder.parse(new InputSource(new StringReader(xml_data)));
      }
      catch (final IOException e) {
         e.printStackTrace();
         System.exit(3);
      }
      catch (final SAXException e) {
         System.out.println("Parsing error on " + xml_data);
         e.printStackTrace();
         System.exit(3);
      }
      return getItem(doc.getDocumentElement());
   }

   private static Item getItem(final Element tree) {
      final Item item = new Item(tree.getAttribute("ItemID"));
      final LinkedList<String> categories = new LinkedList<String>();
      final LinkedList<Bid> bids = new LinkedList<Bid>();

      //Translate each of item's elements
      final org.w3c.dom.NodeList nlist = tree.getChildNodes();
      for (int i=0; i<nlist.getLength(); i++) {
         final Node n = nlist.item(i);
         switch (n.getNodeName()) {
         case "Name":
            item.setName(getElementText((Element) n));
            break;
         case "Category":
            categories.add(getElementText((Element) n));
            break;
         case "Currently":
            item.setCurrently(getElementText((Element) n));
            break;
         case "Buy_Price":
            item.setBuyPrice(getElementText((Element) n));
            break;
         case "First_Bid":
            item.setFirstBid(getElementText((Element) n));
            break;
         case "Number_of_Bids":
            item.setNumberOfBids(getElementText((Element) n));
            break;
         case "Bids":
            Element[] bids_list = getElementsByTagNameNR((Element) n, "Bid");
            for (int j=0; j<bids_list.length; j++) {
               bids.add(getBid(bids_list[j]));
            }
            break;
         case "Location":
            item.setLocation(new Location(getElementText((Element) n), 
                  ((Element) n).getAttribute("Latitude"), ((Element) n).getAttribute("Longitude")));
            break;
         case "Country":
            item.setCountry(getElementText((Element) n));
            break;
         case "Started":
            item.setStarted(getElementText((Element) n));
            break;
         case "Ends":
            item.setEnds(getElementText((Element) n));
            break;
         case "Seller":
            item.setSeller(getUser((Element) n));
            break;
         case "Description":
            item.setDescription(getElementText((Element) n));
            break;
         }
         item.setCategories(categories.toArray(new String[categories.size()]));
         Collections.sort(bids, Collections.reverseOrder(new BidComparator()));
         item.setBids(bids.toArray(new Bid[bids.size()]));
      }
      return item;
   }

   private static Bid getBid(final Element bid) {
      return new Bid(getUser(getElementByTagNameNR(bid, "Bidder")), getElementTextByTagNameNR(bid, "Time"), 
            getElementTextByTagNameNR(bid, "Amount"));
   }

   private static User getUser(final Element user) {
      String location_name = getElementTextByTagNameNR(user, "Location");
      String country = getElementTextByTagNameNR(user, "Country");
      if (location_name == null) {location_name = null;}
      if (country == null) {country = null;}

      return new User(user.getAttribute("UserID"), user.getAttribute("Rating"), new Location(location_name), country);
   }

   static {
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setValidating(false);
         factory.setIgnoringElementContentWhitespace(true);      
         XMLParser.builder = factory.newDocumentBuilder();
         XMLParser.builder.setErrorHandler(new MyErrorHandler());
      }
      catch (final FactoryConfigurationError e) {
         System.out.println("unable to get a document builder factory");
         System.exit(2);
      } 
      catch (final ParserConfigurationException e) {
         System.out.println("parser was unable to be configured");
         System.exit(2);
      }
   }
}