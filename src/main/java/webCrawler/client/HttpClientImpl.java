package webCrawler.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;



/**
 * Get html content using httpClient
 * reference to Httplient Quick start
 * @author Yang Zhang (Lucas)
 *
 */
//@Component
public class HttpClientImpl implements HttpClient
{
	private static Logger logger = LoggerFactory.getLogger(HttpClientImpl.class);
	
	@Override
	public boolean get(URI uri, StringBuilder response)
	{
		//TODO implement org.apache.http.client.HttpClient
			
		//set time out to 1000, or it will take too much time
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        HttpGet httpgets = new HttpGet(uri);    
        HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpgets);
			//Check if there is broken link
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				logger.error("Invalid link: {}", uri.toString());
				return false;
			}
			
			HttpEntity entity = httpResponse.getEntity();    
	        if (entity != null) {    
	            InputStream inputStream = entity.getContent();    
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
	        }else{
	        	logger.debug("Empty content at: {}", uri.toString());
	        	return false;
	        }
	        //System.out.println(response);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			logger.error("ClientProtocolException: {}", uri.toString());
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.debug("IOException: {}", uri.toString());
			return false;
		} finally{
			if(httpgets != null){
				httpgets.abort(); 	
			}
		}
		return true;
		
	}

}
