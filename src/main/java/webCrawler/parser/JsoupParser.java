package webCrawler.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webCrawler.index.SpiderIndex;

public class JsoupParser implements HttpParser{

	private static Logger logger = LoggerFactory.getLogger(JsoupParser.class);
	private SpiderIndex indexer;


	public JsoupParser(SpiderIndex indexer) {
		this.indexer = indexer;
	}

	@Override
	public List<URI> parseLinks(String content, URI parentUri) {
		// TODO Auto-generated method stub
		final List<URI> uris = new ArrayList <URI> ();
		String url;
		String text = "";

		String host = parentUri.getScheme() + "://" + parentUri.getHost();

		Document doc = Jsoup.parse(content);

		Elements links = doc.select("a[href]");


		for (Element link : links) {
			url = link.attr("href");
			text += trim(link.text(), 35);

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

			} else if (url.startsWith("/")) {
				url = host + url;
			}else{
				logger.debug("Invalid link: {}", url);
				continue;
			}


			try {
				URI uri = new URI(url);
				if (uris.contains(uri)) {
					continue;
				}else{
					uris.add(uri);
				}						
			} catch (URISyntaxException e) {
				continue;
			}
		}  
		logger.info("Found {} links in {}.", uris.size(), parentUri);
		indexer.createIndex(parentUri, text);
		return uris;
	}


	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width-1) + ".";
		else
			return s;
	}

}
