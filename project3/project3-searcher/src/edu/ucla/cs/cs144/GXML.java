package edu.ucla.cs.cs144;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import javax.xml.transform.*;
import java.io.StringWriter;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import edu.ucla.cs.cs144.DbManager;


public class GXML{
public String generate(String itemId){
	String xmlstore = "";                
	
        Connection conn = null;
        // Create a connection to the database
        try 
        {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db          = dbf.newDocumentBuilder();
			org.w3c.dom.Document XMLStructure   = db.newDocument();
            // Connection to db manager
            conn = DbManager.getConnection(true);
            Statement statement = conn.createStatement();
            ResultSet itemQans = statement.executeQuery("SELECT * FROM Item "
                                                    + "WHERE Item.ItemID = " + itemId);                
            itemQans.first();
            if (itemQans.getRow() != 0) 
            {                                              

                // root with attr
                Element root = XMLStructure.createElement("Item");
                root.setAttribute("ItemID", itemId);
                XMLStructure.appendChild(root);
				
				//name
                Element element_name = XMLStructure.createElement("Name");
                element_name.appendChild(XMLStructure.createTextNode(itemQans.getString("Name"))); // Need to replace special
                root.appendChild(element_name);
                	
                // Category 
                Statement qCat = conn.createStatement();
                ResultSet catQans = qCat.executeQuery("SELECT Category FROM ItemCategory WHERE ItemCategory.ItemID =" + itemId );
                Element catE;
                while (catQans.next()) {
                    catE = XMLStructure.createElement("Category");
                    catE.appendChild(XMLStructure.createTextNode(catQans.getString("Category"))); //replace spical
                    root.appendChild(catE);
                }
                catQans.close();
                qCat.close();
				
                //firstBid
                Element start_element = XMLStructure.createElement("First_Bid");
                start_element.appendChild(XMLStructure.createTextNode("$" + itemQans.getString("First_Bid")));
                root.appendChild(start_element);
				
                //currently
                Element curE = XMLStructure.createElement("Currently");
                curE.appendChild(XMLStructure.createTextNode("$" + itemQans.getString("Currently")));
                root.appendChild(curE);

				//Buy_Price
                if (!itemQans.getString("Buy_Price").equalsIgnoreCase("0.00") ) 
                {
                	System.out.println(itemQans.getString("Buy_Price"));
                    Element buyprice_element = XMLStructure.createElement("Buy_Price");
                    buyprice_element.appendChild(XMLStructure.createTextNode("$" + itemQans.getString("BuyPrice")));
                    root.appendChild(buyprice_element);
                }
				
				//Number_of_Bids
                Statement cbq = conn.createStatement();
                ResultSet cba = cbq.executeQuery("SELECT * FROM Bid WHERE ItemID = " + itemId + " ");
                
                Element numberbids_elements = XMLStructure.createElement("Number_of_Bids");
                cba.last();
                int c = cba.getRow();
                numberbids_elements.appendChild(XMLStructure.createTextNode(""+c));              
                root.appendChild(numberbids_elements);
                
                //Bids
                Statement bdsq = conn.createStatement();
				String condition="WHERE Bid.ItemId = " + itemId + " AND Bid.UserID = User.UserID";
                ResultSet bdsa = bdsq.executeQuery("SELECT * FROM Bid, User "+ condition);
                Element bdsE = XMLStructure.createElement("Bids");
				root.appendChild(bdsE);
                while (bdsa.next()) {
                    try {//user id rate
                        Element bidE = XMLStructure.createElement("Bid");
                        Element buyerE = XMLStructure.createElement("Bidder");
                        buyerE.setAttribute("UserID", bdsa.getString("UserID"));
                        buyerE.setAttribute("Rating", bdsa.getString("Rating"));
                        //location  country
                        if (!bdsa.getString("Location").equals("")) {
                            Element lcE = XMLStructure.createElement("Location");
                            lcE.appendChild(XMLStructure.createTextNode(bdsa.getString("Location")));
                            buyerE.appendChild(lcE);
                        }
                        if (!bdsa.getString("Country").equals("")) {
                            Element ctE = XMLStructure.createElement("Country");
                            ctE.appendChild(XMLStructure.createTextNode(bdsa.getString("Country")));
                            buyerE.appendChild(ctE);
                        }
                        bidE.appendChild(buyerE);
                        bdsE.appendChild(bidE);
                    }
                    catch(Exception e)
                    {
                    	System.out.println(e.getMessage());
                    }
                }
                
                Statement locStmt = conn.createStatement();
                ResultSet locResult = locStmt.executeQuery("SELECT * FROM User,Item WHERE User.UserID = Item.Seller AND Item.ItemID = " + itemId);
                
                locResult.next();
                Element lcE = XMLStructure.createElement("Location");
                lcE.appendChild(XMLStructure.createTextNode(locResult.getString("Location")));
                root.appendChild(lcE);
				
				Statement lcq = conn.createStatement();
                ResultSet lca = lcq.executeQuery("SELECT Latitude,Longitude FROM location WHERE ItemId ="+ itemId);
				lca.first();
				if (lca.getRow() != 0){
				lcE.setAttribute("Latitude", lca.getString("Latitude"));
				lcE.setAttribute("Longitude", lca.getString("Longitude"));}

                
                Element countryElement = XMLStructure.createElement("Country");
                countryElement.appendChild(XMLStructure.createTextNode(locResult.getString("Country")));
                root.appendChild(countryElement);
                
                Element stE = XMLStructure.createElement("Started");
                String st = itemQans.getString("Started");
                
                String stt = mySqlTimeFormat(st);
                stE.appendChild(XMLStructure.createTextNode(stt));
                root.appendChild(stE);
                
                Element edE = XMLStructure.createElement("Ends");
                String ed = itemQans.getString("Ends");
                
                String edt = mySqlTimeFormat(ed);

                edE.appendChild(XMLStructure.createTextNode(edt));
                root.appendChild(edE);
                
                Element sellerE = XMLStructure.createElement("Seller");
                sellerE.setAttribute("UserID", locResult.getString("UserID"));
                sellerE.setAttribute("Rating", locResult.getString("Rating"));
                root.appendChild(sellerE);
                
                Element dE = XMLStructure.createElement("Description");
                dE.appendChild(XMLStructure.createTextNode(itemQans.getString("Description")));
                root.appendChild(dE);
                
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer tfer = tf.newTransformer();
                tfer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                StringWriter sw = new StringWriter();
                tfer.transform(new DOMSource(XMLStructure), new StreamResult(sw));
                xmlstore = sw.getBuffer().toString();
            }
            return xmlstore;

         }
        catch(Exception e)
        {
			System.out.println(e.getMessage());
			return "";
        }	
   }


    
    private static String mySqlTimeFormat(String date)
    {
            SimpleDateFormat format_in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            SimpleDateFormat format_out = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
           
            StringBuffer buffer = new StringBuffer();
               
            try
            {
                    Date parsedDate = format_in.parse(date);
            
                    return "" + format_out.format(parsedDate);
            }
            catch(Exception pe) {
                System.err.println("Parse error");
                return "Parse error";
            }
    }
}