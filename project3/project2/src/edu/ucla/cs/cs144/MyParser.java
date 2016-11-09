/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import java.lang.Object;

/**
 * MyParser
 * Parses .xml files provided via command line into .csv files
 * for loading into MySQL.
 */
class MyParser {


    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
    "none",
    "Element",
    "Attr",
    "Text",
    "CDATA",
    "EntityRef",
    "Entity",
    "ProcInstr",
    "Comment",
    "Document",
    "DocType",
    "DocFragment",
    "Notation",
    };

    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }


    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
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
    static Element getElementByTagNameNR(Element e, String tagName) {
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
    static String getElementText(Element e) {
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
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    /* Returns the date from the xml files' format (Dec-04-01 04:03:12)
     * converted to MySQL timestamp readable format ().
     */
    static String convertDate(String dateString) {
        String old_format = "MMM-dd-yy HH:mm:ss";
        String new_format = "yyyy-MM-dd HH:mm:ss";
        
        SimpleDateFormat d_format = new SimpleDateFormat(old_format);
        String out_date = "";
        try {
            Date date = d_format.parse(dateString);
            d_format.applyPattern(new_format);
            out_date = d_format.format(date);
        } catch (ParseException pe) {
            System.out.println("error");
        }
        
        return out_date;
    }
 
    public static class Item { 
        String item_id, item_name, item_description, item_started, item_ends, item_currently,
        item_firstbid, item_buyprice,item_numberofbids,item_seller,item_location,item_country;


        Item(String id, String name, String description, String started, String ends, String currently, 
                String firstbid, String buyprice, String numberofbids,  String seller, String location, String country) { 

            item_id = id;  
            item_name = name; 
            item_description = description;
            item_started = started;
            item_ends = ends;
            item_currently = currently; 
            item_firstbid = firstbid;
            item_buyprice = buyprice;
            item_numberofbids = numberofbids;
            item_seller = seller;
			item_location = location;
			item_country = country;
        } 
    }
	public static class ItemLocation{
		String itemId,latitude,longitude;
		public ItemLocation(String id, String lat,String lg){
			itemId=id;
			latitude=lat;
			longitude=lg;
		}
	}
	
    public static class Bid
    { 
        String bid_userid, bid_time, bid_item, bid_amount;


        Bid(String userID, String time, String itemID, String amount)
        { 
          
            bid_userid = userID;
            bid_time = time; 
            bid_item = itemID; 
            bid_amount = amount;
        } 
    }
    public static class User { 
        String user_name, user_location, user_rating, user_country;

        User(String name, String rating, String location, String country) { 
            user_name = name; 
            user_rating = rating;
            user_location = location;
            user_country = country;
        } 
    }
    
    public static class ItemCategory {
        String ic_itemID;
        String ic_name;

        public ItemCategory(String itemID, String name) {
            ic_itemID = itemID;
            ic_name = name;
        }
    }	 

    // Maps which will hold entries for .csv files while parsing    
    static Map<String, Item> itemMap = new HashMap<String, Item>();
	static Map<String, ItemLocation> itemLocationMap = new HashMap<String,ItemLocation>();
    static Map<String, Bid> bidMap = new HashMap<String, Bid>();
    static Map<String, User> userMap = new HashMap<String, User>();
    static Map<String, ItemCategory> itemcategoriesMap = new HashMap<String, ItemCategory>();     
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }


        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
    	
        //item
        Element[] items = getElementsByTagNameNR(doc.getDocumentElement(), "Item");
        for(int i=0; i<items.length; i++) 
		{
            String itemID = items[i].getAttribute("ItemID");
            String name =  getElementTextByTagNameNR(items[i], "Name");
            String currently = strip(getElementTextByTagNameNR(items[i], "Currently"));
            String buy_price = strip(getElementTextByTagNameNR(items[i], "Buy_Price"));
            if (buy_price.isEmpty())buy_price = "";

            String first_bid = strip(getElementTextByTagNameNR(items[i], "First_Bid"));
            String number_of_bids = getElementTextByTagNameNR(items[i], "Number_of_Bids");
            
            String i_location = getElementTextByTagNameNR(items[i], "Location");
            if (i_location.isEmpty())i_location = "";	
            
			Element loc=getElementByTagNameNR(items[i],"Location");
			String lat=loc.getAttribute("Latitude");
			String lg=loc.getAttribute("Longitude");
			if(lat==null) lat="";
			if(lg==null) lg="";
			itemLocationMap.put(itemID,new ItemLocation(itemID,lat,lg));
			
            String i_country = getElementTextByTagNameNR(items[i], "Country");
			if (i_country.isEmpty())i_country = "";
			
            String start_time = convertDate(getElementTextByTagNameNR(items[i], "Started"));
            String end_time = convertDate(getElementTextByTagNameNR(items[i], "Ends"));
            
            // seller
            Element seller = getElementByTagNameNR(items[i], "Seller");
            String s_userid = seller.getAttribute("UserID");
            String s_rating = seller.getAttribute("Rating");
            String s_location = getElementTextByTagNameNR(items[i], "Location");
            String s_country = getElementTextByTagNameNR(items[i], "Country");          
            // Add the user to the userMap if not presentely identifies a user
            if (!userMap.containsKey(s_userid)) {
                //User seller_element = new User(s_userid, s_rating, s_location, s_country);
                userMap.put(s_userid, new User(s_userid, s_rating, s_location, s_country));
            }
            String description = getElementTextByTagNameNR(items[i], "Description");

            if (description.length() > 4000)
                    description = description.substring(0, 4000);
			
            Element[] categories = getElementsByTagNameNR(items[i], "Category");

			for(int k=0;k<categories.length;k++)
			{
				String categoryName = getElementText(categories[k]);
                //ItemCategory category_element = new ItemCategory(itemID, categoryName);
				itemcategoriesMap.put(itemID + categoryName, new ItemCategory(itemID, categoryName));
            }
		
		    //bids
            Element BidsParent = getElementByTagNameNR(items[i], "Bids");
            Element[] Bids = getElementsByTagNameNR(BidsParent, "Bid");
            for (int j=0; j<Bids.length; j++) {

                Element bid = Bids[j];
                String b_time = convertDate(getElementTextByTagNameNR(bid, "Time"));
                String b_amount = strip(getElementTextByTagNameNR(bid, "Amount"));

                // Get info about the bidder to add to userMap
                Element bidder = getElementByTagNameNR(bid, "Bidder");
                String b_userid =  bidder.getAttribute("UserID");
                String b_rating = bidder.getAttribute("Rating");
                String b_location = getElementTextByTagNameNR(bidder, "Location");
                if (b_location.isEmpty())
                    b_location = "";

                String b_country = getElementTextByTagNameNR(bidder, "Country");
                if (b_country.isEmpty())
                    b_country = "";

                // Add the bid to the bidMap
                //Bid bid_element = new Bid(b_userid, b_time, itemID, b_amount);
                bidMap.put(b_userid + b_time, new Bid(b_userid, b_time, itemID, b_amount));

                if (!userMap.containsKey(b_userid)) {
                    //User bidder_element = new User(b_userid, b_rating, b_location, b_country);
                    userMap.put(b_userid, new User(b_userid, b_rating, b_location, b_country));
                }
            }


            //Item item_element = new Item(itemID, name, description, start_time, end_time, currently, first_bid, buy_price, number_of_bids, s_userid, i_location, i_country); 
            itemMap.put(itemID, new Item(itemID, name, description, start_time, end_time, currently, first_bid, buy_price, number_of_bids, s_userid, i_location, i_country));
        }        
    }
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }

        // Write maps to .dat files.
        // Write itemsMap to Item.dat
        try {
            BufferedWriter itemfilewriter= new BufferedWriter(new FileWriter("Item.dat"));
            BufferedWriter userfilewriter= new BufferedWriter(new FileWriter("User.dat"));
            BufferedWriter itemcategoryfilewriter= new BufferedWriter(new FileWriter("Item_Category.dat"));
            BufferedWriter bidsfilewriter= new BufferedWriter(new FileWriter("Bid.dat"));
			BufferedWriter itemLocationWriter= new BufferedWriter(new FileWriter("ItemLocation.dat"));
			
            for (Map.Entry<String, Item> entry : itemMap.entrySet()) {
                Item itemEntry = entry.getValue();
                  
                String currentfile = String.format("%s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s\n", 
                itemEntry.item_id,itemEntry.item_name,itemEntry.item_description,itemEntry.item_started,itemEntry.item_ends,itemEntry.item_currently,
                itemEntry.item_firstbid,itemEntry.item_buyprice,itemEntry.item_numberofbids,itemEntry.item_seller,itemEntry.item_location,itemEntry.item_country
					   );
                
                itemfilewriter.write(currentfile); 
				
            }
			for(Map.Entry<String, ItemLocation> entry : itemLocationMap.entrySet()){
				ItemLocation il=entry.getValue();
				String currentfile = String.format
				("%s |*| %s |*| %s\n", il.itemId,il.latitude,il.longitude);
				itemLocationWriter.write(currentfile);
			}
			
			for (Map.Entry<String, User> entry : userMap.entrySet()) {
                User userEntry = entry.getValue();

                String currentfile = String.format
				("%s |*| %s |*| %s |*| %s\n", userEntry.user_name,userEntry.user_rating,userEntry.user_location,userEntry.user_country);

                userfilewriter.write(currentfile); 
            }
			for (Map.Entry<String, ItemCategory> entry : itemcategoriesMap.entrySet()) {
                ItemCategory icEntry = entry.getValue();
               
                String currentfile = String.format
				("%s |*| %s\n", icEntry.ic_itemID,icEntry.ic_name);

                itemcategoryfilewriter.write(currentfile); 
            }
			for (Map.Entry<String, Bid> entry : bidMap.entrySet()) {

                Bid bidEntry = entry.getValue();
                   
                String currentfile = String.format
				("%s |*| %s |*| %s |*| %s\n", bidEntry.bid_userid,bidEntry.bid_time,bidEntry.bid_item,bidEntry.bid_amount);
                
                bidsfilewriter.write(currentfile); 
            }
            itemfilewriter.close();
            userfilewriter.close();
            itemcategoryfilewriter.close();			
            bidsfilewriter.close();	
			itemLocationWriter.close();			
        } catch (IOException e) {
                e.printStackTrace();
        }
		
	}
}