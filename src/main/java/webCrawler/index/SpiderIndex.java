package webCrawler.index;

import java.net.URI;

/**
 * Interface for creating index
 * @author Yang Zhang (Lucas)
 *
 */
public interface SpiderIndex {

	public void createIndex(URI uri, String content);
}
