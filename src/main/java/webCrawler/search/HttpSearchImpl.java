package webCrawler.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.lucene.document.Document;
import webCrawler.bean.Content;

/**
 * Search from lucene directory for key word, store it in Content Bean list
 * reference to QueryTest/LuceneIndexing 
 * @author Yang Zhang (Lucas)
 *
 */
public class HttpSearchImpl implements HttpSearch{

	private static Logger logger = LoggerFactory.getLogger(HttpSearchImpl.class);
	@Override
	public List<Content> search(String word) {
		// TODO Auto-generated method stub
		List<Content> contentList = new ArrayList<>();
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36 , analyzer);
		SimpleFSDirectory simpleFSDirectory;
		int hitsPerPage = 50;
		try {
			Query q = new QueryParser(Version.LUCENE_36, "Content", analyzer).parse(word);
			simpleFSDirectory = new SimpleFSDirectory(new File("./lucene"));
			IndexWriter writer = new IndexWriter(simpleFSDirectory, conf);
			
			IndexReader reader = IndexReader.open(simpleFSDirectory);

			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			for(int i=0;i<hits.length;++i) {
			    ScoreDoc hit = hits[i];
				int docId = hit.doc;
			    Document d = searcher.doc(docId);
			    contentList.add(new Content(d.get("URI"), d.get("Content")));
			}
			writer.close();
			searcher.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IORxception, file not found, search failure");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("ParseException, search failure");
		}
		
		
		return contentList;
	}
	


}
