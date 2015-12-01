package webCrawler.client;

import java.io.IOException;
import java.net.URI;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupClient implements HttpClient{
	
	private static Logger logger = LoggerFactory.getLogger(JsoupClient.class);

	@Override
	public boolean get(URI uri, StringBuilder response) {
		// TODO Auto-generated method stub
		Connection connection = Jsoup.connect(uri.toString());
		
		connection.timeout(1000);

		try {
			Document htmlDocument = connection.get();
			response.append(htmlDocument.toString());
		} catch (IOException e) {
			logger.debug("IOException: {}", uri.toString());
			return false;
		}		
		return true;
	}

}
