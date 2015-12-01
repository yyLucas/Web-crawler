package webCrawler.index;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Index URI and text content, write them to documents using lucene
 * Reference to IndexWriterTest example
 * @author Yang Zhang (Lucas)
 *
 */
public class SpiderIndexImpl implements SpiderIndex{

	private static Logger logger = LoggerFactory.getLogger(SpiderIndexImpl.class);

	@Override
	public void createIndex(URI uri, String content) {
		// TODO Auto-generated method stub
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36 , analyzer);
		SimpleFSDirectory simpleFSDirectory;
		try {
			simpleFSDirectory = new SimpleFSDirectory(new File("./lucene"));
			IndexWriter writer = new IndexWriter(simpleFSDirectory, conf);
			
			Document doc = new Document();	
			doc.add(new Field("URI", uri.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));			
			doc.add(new Field("Content", content, Field.Store.YES, Field.Index.ANALYZED));
			
			writer.addDocument(doc);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.debug("IOException - LockObtainFailedException: {}", uri.toString());
		}
		
	}


}
