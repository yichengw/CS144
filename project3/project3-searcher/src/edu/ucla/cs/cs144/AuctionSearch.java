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

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		Connection conn = null;
		try {
			conn = DbManager.getConnection(true);
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		int sum=numResultsToSkip+numResultsToReturn;
		SearchResult[] srs=null;
		ArrayList<SearchResult> srList = new ArrayList<SearchResult>();
		SearchEngine_basic seb=null;
		try{ seb=new SearchEngine_basic();}
		catch(IOException ex){System.out.println(ex);}
		ArrayList<String> idCount=null;
		try{idCount=seb.bSearch(query,sum);}
		catch(IOException ex){System.out.println(ex);}
		//Collection.sort(idCount);
		String itemId;
		//System.out.println("Size of Kw: "+idCount.size());
		for(int i=0;i<numResultsToReturn;i++){
			if((i+numResultsToSkip)>=idCount.size()) break;
			itemId=idCount.get(i+numResultsToSkip);
			SearchResult sr=null;
			try{
				Statement stmt = conn.createStatement();
				String q="select Name from Item where ItemID = " + itemId;
				ResultSet  rs= stmt.executeQuery(q);
				rs.next();
				sr=new SearchResult(itemId,rs.getString(1));
					
			} catch (SQLException ex){
				System.out.println("SQLException caught");
				System.out.println("---");
				while ( ex != null ){
					System.out.println("Message   : " + ex.getMessage());
					System.out.println("SQLState  : " + ex.getSQLState());
					System.out.println("ErrorCode : " + ex.getErrorCode());
					System.out.println("---");
					ex = ex.getNextException();
				}
			}
			
			srList.add(sr);
		}
		srs=srList.toArray(new SearchResult[srList.size()]);
		try {
			conn.close();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		return srs;
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		SearchResult[] srs= null;
		ArrayList<SearchResult> srList = new ArrayList<SearchResult>();
		Connection conn = null;
        // create a connection to the database to retrieve Items from MySQL
		try {
	    	conn = DbManager.getConnection(true);
		} catch (SQLException ex) {
	    	System.out.println(ex);
		}
		SearchEngine_basic seb=null;
		try{ seb=new SearchEngine_basic();}
		catch(IOException ex){System.out.println(ex);}
		ArrayList<String> idCount=null;
		try{idCount=seb.bSearch(query,Integer.MAX_VALUE);}
		catch(IOException ex){System.out.println(ex);}
/*********ArrayList<String> idCount********************************************************************/
		
		String brx = String.valueOf(region.getRx());
		String bry = String.valueOf(region.getRy());
		String tlx = String.valueOf(region.getLx());
		String tly = String.valueOf(region.getLy());

		String leftUp = tlx + " " + bry;
		String leftLow = tlx + " " + tly;
		String rightUp = brx + " " + bry;
		String rightLow = brx + " " + tly;

		try{
			String sRegion = "Polygon((" + leftUp + "," + leftLow + "," + rightLow + "," + rightUp + "," + leftUp + "))";
			String q = "select ItemID from ItemLocation where MBRContains(GeomFromText('" + sRegion + "'), g)";
			Statement stmt = conn.createStatement();
			ResultSet inRegionIds = stmt.executeQuery(q);
			ArrayList<String> inRegionIdList = new ArrayList<String>();
			while (inRegionIds.next()){ 
				inRegionIdList.add(inRegionIds.getString("ItemID"));
			}
			int total=0;
			ArrayList<String> ans = new ArrayList<String>();
			int sum=numResultsToReturn+numResultsToSkip;
			for(int i =0;i<inRegionIdList.size();i++){
				if(idCount.contains(inRegionIdList.get(i))){
					ans.add(inRegionIdList.get(i));
					total++;
				}
				if(total==sum){break;}				
			}
		String itemId;
		for(int i=0;i<numResultsToReturn;i++){
			if((i+numResultsToSkip)>=ans.size()) break;
			itemId=ans.get(i+numResultsToSkip);
			SearchResult sr=null;
			try{
				Statement stmt2 = conn.createStatement();
				String q2="select Name from Item where ItemID = " + itemId;
				ResultSet  rs= stmt2.executeQuery(q2);
				rs.next();
				sr=new SearchResult(itemId,rs.getString(1));
					
			} catch (SQLException ex){
				System.out.println("SQLException caught");
				System.out.println("---");
				while ( ex != null ){
					System.out.println("Message   : " + ex.getMessage());
					System.out.println("SQLState  : " + ex.getSQLState());
					System.out.println("ErrorCode : " + ex.getErrorCode());
					System.out.println("---");
					ex = ex.getNextException();
				}
			}
			
			srList.add(sr);
		}			
			
			srs=srList.toArray(new SearchResult[srList.size()]);
			
		} 
		catch(SQLException sqlException) {
            System.out.println(sqlException);
        }
		try {
			conn.close();
		} catch(SQLException ex) {
			System.out.println(ex);
		}
		return srs;
	}

	public String getXMLDataForItemId(String itemId) {
		
		GXML gXML = new GXML();
		
		
		return gXML.generate(itemId);
	}
	
	public String echo(String message) {
		return message;
	}
	/* public int matchCount(String query){
		SearchEngine_basic seb=null;
		int c=0;
		try{ seb=new SearchEngine_basic();
			c=seb.matchCount(query);
		}catch(IOException ex){System.out.println(ex);}
		return c; 
	} */

}
