package webCrawler.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import webCrawler.bean.Content;
import webCrawler.bean.SpiderSetting;
import webCrawler.client.HttpClient;
import webCrawler.client.JsoupClient;
import webCrawler.index.SpiderIndex;
import webCrawler.index.SpiderIndexImpl;
import webCrawler.parser.HttpParser;
import webCrawler.parser.JsoupParser;
import webCrawler.search.HttpSearch;
import webCrawler.search.HttpSearchImpl;
import webCrawler.spider.Spider;

@Controller
@EnableAutoConfiguration
public class AdvanceController {
	
	
	private static Logger logger = LoggerFactory.getLogger(AdvanceController.class);
	
	private static Spider spider;
	private HttpSearch searcher;
	
	List<Content> contentList = new ArrayList<Content>();
	
	public AdvanceController(){
		spider = getSpider();
		searcher = getSearcher();
	}
	
	@Bean
	public HttpSearch getSearcher()
	{
		return new HttpSearchImpl();
	}
	

	@Bean
	public Spider getSpider()
	{
		return new Spider(getHttpClient(), getHttpParser());
	}
	
	@Bean
	public HttpClient getHttpClient()
	{
		return new JsoupClient();
	}
	
	@Bean
	public HttpParser getHttpParser()
	{
		return new JsoupParser(getSpiderIndex());
	}
	
	@Bean
	public SpiderIndex getSpiderIndex()
	{
		return new SpiderIndexImpl();
	}
	
	public static void main(String[] args)
	{
        SpringApplication.run(AdvanceController.class, args);

	}
	
	/**
	 * Start the searching process, called when value submitted to search
	 * Return page with search results.
	 * @param content
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/search")
	public String display(Content content, Map<String, Object> model) {
		logger.info("Keyword is: {}", content.getSearchString());
		//Start search
		contentList = searcher.search(content.getSearchString());

		// Display the search result
		model.put("Content", contentList);
		logger.info("Search successful, page displayed.");
		
		return "searcher";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/crawl")
	public String crawl(SpiderSetting setting) {		
		try {
			logger.info("URI is: {}, depth is: {}", setting.getUrl(), setting.getDepth());
			spider.startCrawl(new URI(setting.getUrl()),setting.getDepth());
			//spider.startCrawl(new URI("http://www.unitec.ac.nz"),1);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.error("Invalid link: {}", setting.getUrl());
		}
		logger.info("Crawl successful, go to the search page.");
		return "searcher";
	}
	
	/**
	 * Set the crawl url and depth
	 * @param model
	 * @return
	 */
	@RequestMapping (method=RequestMethod.GET, value="/spider")	
	public String startSpider(Map<String, Object> model)
	{
		return "spider";
	}
	
	/**
	 * Search form contetn
	 * @param model
	 * @return
	 */
	@RequestMapping (method=RequestMethod.GET, value="/search")	
	public String search(Map<String, Object> model)
	{
		return "searcher";
	}
}
