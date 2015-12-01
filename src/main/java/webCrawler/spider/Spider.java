package webCrawler.spider;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webCrawler.client.HttpClient;
import webCrawler.parser.HttpParser;
import webCrawler.parser.JsoupParser;

/**
 * Recursively crawl the website to a certain depth, delete the lucene data.
 * @author Yang Zhang (Lucas)
 *
 */
//@Component
public class Spider {
	
	private static Logger logger = LoggerFactory.getLogger(Spider.class);
	
	private HttpClient client;
	private HttpParser parser;
	
	private Set<URI> alreadyVisited = new HashSet<URI>();
	
	public Spider(HttpClient client, HttpParser parser)
	{
		this.client = client;
		this.parser = parser;
	}
	
	public Spider(HttpClient client, JsoupParser parser)
	{
		this.client = client;
		this.parser = parser;
	}
	

	
	public void crawl(URI uri, int depth)
	{	
		alreadyVisited.add(uri);
		StringBuilder content = new StringBuilder("");
		if (client.get(uri, content)) {
			List<URI> uris = parser.parseLinks(content.toString(), uri);
			if(!uris.isEmpty()){
				for(int i=0; i<uris.size(); i++){
					if(depth >=1){
						if(alreadyVisited.contains(uris.get(i))){
							continue;
						}else{
							crawl(uris.get(i), depth-1);
						}
					}
				}
			}
		}

		//TODO implement web crawler algorithm
	}
	
	private void deleteLuceneData() {
		String path = System.getProperty("user.dir") + "/lucene";
		File directory = new File(path);
		if (directory.exists() && directory.isDirectory()) {
			logger.info("Delete all record in lucene");
			File files[] = directory.listFiles();
			for (File file : files) {
				file.delete();
			}
		}
	}



	public void startCrawl(URI uri, int depth) {
		// TODO Auto-generated method stub
		deleteLuceneData();
		alreadyVisited = new HashSet<URI>();
		crawl(uri, depth);
	}
}
