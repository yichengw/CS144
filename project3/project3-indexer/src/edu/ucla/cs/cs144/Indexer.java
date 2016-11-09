package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    private IndexWriter indexWriter = null;
    private IndexWriter indexWriter2 = null;
	/** Creates a new instance of Indexer */
	public Indexer() {
		try{
			indexWriter=getIndexWriter("/var/lib/lucene/index1/");
			indexWriter2=getIndexWriter("/var/lib/lucene/index2/");
		}
		catch(IOException ex){
			System.out.println(ex);
		}
	}
    
    private IndexWriter getIndexWriter(String dir) throws IOException {
            Directory indexDir = FSDirectory.open(new File(dir));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            return new IndexWriter(indexDir, config);
        
    }   
 
    public void rebuildIndexes() throws IOException{
        Connection conn = null;
		try {
			conn = DbManager.getConnection(true);
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		try{
			Statement stmt = conn.createStatement();
			ResultSet  rs= stmt.executeQuery("select ItemID,Description,Name from Item ");
			int x=0,y=0;
			while(rs.next()){
				String id=rs.getString(1);
				String description=rs.getString(2);
				String name=rs.getString(3);
				Document doc = new Document();
				doc.add(new StringField("id",id,Field.Store.YES) );
				doc.add(new TextField("content",name+" "+description,Field.Store.NO) );
				doc.add(new TextField("name",name,Field.Store.YES) );
				indexWriter.addDocument(doc);
				x++;
			}
			System.out.println("Item Entries: "+x+"\n# docs: "+indexWriter.maxDoc() );
			indexWriter.close();
			ResultSet  rs2= stmt.executeQuery("select ItemID,Category from ItemCategory ");
			while(rs2.next()){
				String idC=rs2.getString(1);
				String cat=rs2.getString(2);
				Document doc = new Document();
				doc.add(new StringField("id",idC,Field.Store.YES) );
				doc.add(new TextField("category",cat,Field.Store.NO) );
				indexWriter2.addDocument(doc);
				y++;
			}
			indexWriter2.close();
			//System.out.println("Item Entries: "+x+"\n# docs: "+indexWriter.maxDoc() );
			//System.out.println("ItemCategory Entries: "+y+"\n# docs: "+indexWriter2.maxDoc() );
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
		
		try {
			conn.close();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
    }   
/********************************************************/	
	public static void main(String args[]) {
	Indexer idx = new Indexer();
	try{
		idx.rebuildIndexes();	
	}
	catch(IOException ex){
		System.out.println(ex);
	}
	}

}
