package webCrawler.bean;

public class Content {
	private String uri;
	private String content;
	private String searchString;
    

	public Content(){
		
	}
	
    public Content(String uri, String content) {
		// TODO Auto-generated constructor stub
    	this.uri = uri;
    	this.content = content;
	}

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
	public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
