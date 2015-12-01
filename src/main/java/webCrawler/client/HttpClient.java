package webCrawler.client;

import java.net.URI;

public interface HttpClient {
	public boolean get(URI request, StringBuilder messageResponse);
}
