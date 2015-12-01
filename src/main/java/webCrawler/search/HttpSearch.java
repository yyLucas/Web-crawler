package webCrawler.search;

import java.util.List;

import webCrawler.bean.Content;

public interface HttpSearch {

	public List<Content> search(String word);

}
