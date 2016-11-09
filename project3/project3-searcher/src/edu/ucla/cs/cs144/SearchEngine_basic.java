package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.ScoreDoc;

public class SearchEngine_basic {
    public IndexSearcher searcher=null,searcher2 = null;
    private QueryParser parser = null,parser2=null;

    /** Creates a new instance of SearchEngine */
    public SearchEngine_basic() throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1/"))));
        parser = new QueryParser( "content", new StandardAnalyzer());
		searcher2= new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index2/"))));
        parser2 = new QueryParser("category", new StandardAnalyzer());
    }

    public TopDocs performSearch1(String queryString, int n)
    throws IOException, ParseException {
        Query query = parser.parse(queryString);
        return searcher.search(query, n);
    }
	public TopDocs performSearch2(String queryString, int n)
    throws IOException, ParseException {
        Query query = parser2.parse(queryString);
        return searcher2.search(query, n);
    }
	public ArrayList<String> bSearch(String queryString, int n)throws IOException{
		ArrayList<String> idCount=new ArrayList<String>();
		Query query=null;
		Query query2=null;
		try{
		query = parser.parse(queryString);
		query2 = parser2.parse(queryString);
		}
		catch(ParseException ex){
			System.out.println(ex);
		}TopDocs a=searcher.search(query, Integer.MAX_VALUE);
		TopDocs b=searcher2.search(query2,Integer.MAX_VALUE);
		ScoreDoc[] hits1=a.scoreDocs;
		ScoreDoc[] hits2=b.scoreDocs;
		String t;
		 for(int i=0;i<hits1.length;i++){
			t=searcher.doc(hits1[i].doc).get("id");
			idCount.add(t);
		 }	
		for(int i=0;i<hits2.length;i++){
			t=searcher2.doc(hits2[i].doc).get("id");
			if(!idCount.contains(t)){
				idCount.add(t);
			}
		}
		return idCount;
	}
	
	public int matchCount(String queryString)throws IOException{
		ArrayList<String> idCount=new ArrayList<String>();
		Query query=null;
		Query query2=null;
		try{
		query = parser.parse(queryString);
		query2 = parser2.parse(queryString);
		}
		catch(ParseException ex){
			System.out.println(ex);
		}TopDocs a=searcher.search(query, Integer.MAX_VALUE);
		TopDocs b=searcher2.search(query2, Integer.MAX_VALUE);
		ScoreDoc[] hits1=a.scoreDocs;
		ScoreDoc[] hits2=b.scoreDocs;
		String t;
		 for(int i=0;i<hits1.length;i++){
			t=searcher.doc(hits1[i].doc).get("id");
			idCount.add(t);
		 }	
		for(int i=0;i<hits2.length;i++){
			t=searcher2.doc(hits2[i].doc).get("id");
			if(!idCount.contains(t)){
				idCount.add(t);
			}
		}
		return idCount.size();
	}
   /*  public Document getDocument(int docId)
    throws IOException {
        return searcher.doc(docId);
    } */
}