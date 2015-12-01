package webCrawler.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import webCrawler.index.SpiderIndex;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.filters.NodeClassFilter;

/**
 * Parse html content to get valid urls.
 * @author Yang Zhang (Lucas)
 *
 */
public class HttpParserImpl implements HttpParser{
	
	private static Logger logger = LoggerFactory.getLogger(HttpParserImpl.class);
	private SpiderIndex indexer;

	
	public HttpParserImpl(SpiderIndex indexer) {
		this.indexer = indexer;
	}

	@Override
	public List<URI> parseLinks(final String content, final URI parentUri) {

		final List<URI> uris = new ArrayList <URI> ();
		String url;
	
		String host = parentUri.getScheme() + "://" + parentUri.getHost();
		
		try {
			Parser parser = new Parser(content);
			
			NodeFilter filter = new NodeClassFilter(LinkTag.class);       
	        NodeList list = parser.extractAllNodesThatMatch(filter);
			
	        for (int i = 0; i < list.size(); i++) {
	        	Node node = list.elementAt(i);

	        	//get link from a tag
	        	if (node instanceof LinkTag) {
	        		url = ((LinkTag) node).extractLink();

	        		//filter invalid urls
	        		if (url.length() <= 1) {
	        			continue;
	        		}else if (url.charAt(0) == '#') {
	        			continue;
	        		}else if (url.contains("mailto:")) {
	        			continue;
	        		}else  if (url.toLowerCase().contains("javascript")) {
	        			continue;
	        		}else if (url.endsWith(".pdf")) {
	        			continue;
	        		}
	        		
	        		// Standardize the urls
	        		if (url.startsWith("http://") || url.startsWith("https://")) {
//	        			if(url.startsWith("http://www.") || url.startsWith("https://www.")){
//	        				url = url.substring(0, url.indexOf('.')-3) + url.substring(url.indexOf('.')+1);
//	        				
//	        			}

					} else if (url.startsWith("/")) {
						url = host + url;
					}else{
						logger.debug("Invalid link: {}", url);
						continue;
					}
	        		
	        		
	        		try {
	        			URI uri = new URI(url);
	        			if (uris.contains(uri)) {
	        				logger.debug("Link existed: {}", uri);
	        				continue;
	        			}else{
	        				uris.add(uri);
	        			}						
	        		} catch (URISyntaxException e) {
	        			logger.debug("URISyntaxException: {}", url);
	        			continue;
	        		}
	        		
	        		
	        		
	        	}
	        	
	        }
	        //extract page content
	        extractText(content, parentUri);
	        logger.info("Found {} links in {}.", uris.size(), parentUri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("ParserException: {}", parentUri);
			//return Collections.unmodifiableList(uris);
		}
		//TODO parse HTML content using JSoup or Jerry HTML Parsers

		return Collections.unmodifiableList(uris);
	}


	private void extractText(String content, URI parentUri) {
		// TODO Auto-generated method stub		
		try {
			Parser parser = new Parser(content);
			StringBean stringBean = new StringBean();
			parser.visitAllNodesWith(stringBean);
			String contentText = stringBean.getStrings();
			indexer.createIndex(parentUri, contentText);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			logger.error("ParserException: {}", parentUri);
		}
		
	}

}