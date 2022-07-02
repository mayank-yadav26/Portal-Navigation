package com.spring.agent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.spring.model.NavigationDetails;

public class NavigationAgent {
	static CloseableHttpClient httpclient;
	private static final String GET = "GET";
	private static final String POST = "POST";
	
	public String doNavigation(ArrayList<NavigationDetails> navigationDetailsList) {
		String baseUrl = "";
		String requestType = "";
		String parameters = "";
		String requestHeaders = "";
		String browserResp = "";
		String parentPath = "/home/mayank/Documents/";
		String fileName = "responce.html";
		try {
			// Creating a HttpClient object
			httpclient = HttpClients.createDefault();
			for(NavigationDetails navigationDetails : navigationDetailsList) {
				baseUrl = navigationDetails.getBaseUrl();
				requestType = navigationDetails.getRequestType();
				parameters = navigationDetails.getParameters();
				requestHeaders = navigationDetails.getRequestHeaders();
				if(requestType.equals(GET)) {
					String finalGetUrl=baseUrl;
					String queryParameterUrl = createGetUrl(parameters);
					if(!queryParameterUrl.isEmpty()) {
						finalGetUrl+="?"+queryParameterUrl;
					}
					HttpGet httpget = new HttpGet(finalGetUrl);
					// add request headers
					addRequestHeaders(httpget,requestHeaders);
					// Executing the Get request
					HttpResponse httpresponse = httpclient.execute(httpget);
					System.out.println(httpresponse.getStatusLine().toString()); 
					browserResp = writeToFile(httpresponse,parentPath+fileName);
				}else {
					HttpPost httpPost = new HttpPost(baseUrl);
					List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
					addNameValuePairs(nameValuePairs,parameters);
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse httpresponsePost = httpclient.execute(httpPost);
					String browserRespPost = writeToFile(httpresponsePost,parentPath+fileName);
				}
			}
		}catch(Exception e) {
			System.out.println("Error in doNavigation method : "+e.getMessage());
			e.printStackTrace();
		}
		return fileName;
	}
	private static void addNameValuePairs(List<BasicNameValuePair> nameValuePairs, String inputStr) {
		String strArr[] = inputStr.split("\n");
		int tempNum = 0;
		String key="";
		String value="";
		for(String tempLine : strArr) {
			String tempLineArr[] = tempLine.split(": ");
			for(String temp : tempLineArr) {
				tempNum=tempNum^1;
				if(tempNum==1) {
					key=temp;
				}else {
					value=temp;
				}
			}
			nameValuePairs.add(new BasicNameValuePair(key,value));
		}
	}

	private static void addRequestHeaders(HttpGet httpget, String inputStr) {
		String strArr[] = inputStr.split("\n");
		int tempNum = 0;
		String key="";
		String value="";
		for(String tempLine : strArr) {
			String tempLineArr[] = tempLine.split(": ");
			for(String temp : tempLineArr) {
				tempNum=tempNum^1;
				if(tempNum==1) {
					key=temp;
				}else {
					value=temp;
				}
			}
			if(!key.isEmpty()) {
				httpget.addHeader(key,value);
			}
		}
	}

	private static String writeToFile(HttpResponse httpresponse,String filePath) {
		// saving pod doc
		String browserResponce="";
		//read in chunks of 2KB
		byte[] buffer = new byte[2048];
		int bytesRead = 0;
		DataOutputStream os = null;
		try{
			InputStream is = httpresponse.getEntity().getContent();
			InputStream bufferedIs = new BufferedInputStream(is);
			try{
				os = new DataOutputStream(new FileOutputStream(filePath));
				while((bytesRead = bufferedIs.read(buffer)) != -1){
					os.write(buffer, 0, bytesRead);
				}     
			}
			catch(Exception e) {
				System.out.println("Exception : "+e.getMessage());
			}
			File file = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line="";
			while((line=br.readLine())!=null) {
				browserResponce+=line+"\n";
			}
		}
		catch(Exception ex){
			System.out.println("Exception : "+ex.getMessage());
			ex.printStackTrace();
		}
		return browserResponce;
	}

	private static String createGetUrl(String parameters) {
		String strArr[] = parameters.split("\n");
		int tempNum = 0;
		String url = "";
		for(String tempLine : strArr) {
			String tempLineArr[] = tempLine.split(": ");
			for(String temp : tempLineArr) {
				tempNum=tempNum^1;
				if(tempNum==1) {
					url+="&"+temp.trim();
				}else {
					url+="="+temp.trim().replace(" ", "+");
				}
			}
		}
		url=url.replaceAll("^&", "");
		return url;
	}
}
