package com.spring.agent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.spring.model.NavigationDetails;

public class NavigationAgent {
	public static final Logger LOGGER = LogManager.getLogger(NavigationAgent.class);
	static CloseableHttpClient httpclient;
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static String browserResponce="";

	/**
	 * Main method to do navigation
	 * @param navigationDetailsList
	 * @return
	 */
	public String doNavigation(ArrayList<NavigationDetails> navigationDetailsList) {
		LOGGER.info("Inside doNavigation method");
		String baseUrl = "";
		String requestType = "";
		String parameters = "";
		String requestHeaders = "";
		String browserResp = "";
		String parentPath = "/home/mayank/Documents/";
		String fileName = "";
		String navigationName="";
		try {
			// Creating a HttpClient object
			//httpclient = HttpClients.createDefault();
			//this will help to redirect post requests
			httpclient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
			for(NavigationDetails navigationDetails : navigationDetailsList) {
				navigationName=navigationDetails.getNavigationName();
				fileName = navigationName+".html";
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
					LOGGER.info(httpresponse.getStatusLine().toString()); 
					browserResp = writeToFile(httpresponse,parentPath+fileName);
				}else {
					HttpPost httpPost = new HttpPost(baseUrl);
					List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
					addNameValuePairs(nameValuePairs,parameters);
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse httpresponsePost = httpclient.execute(httpPost);
					LOGGER.info(httpresponsePost.getStatusLine().toString()); 
					String browserRespPost = writeToFile(httpresponsePost,parentPath+fileName);
				}
			}
		}catch(Exception e) {
			LOGGER.error("Error in doNavigation method : "+e.getMessage());
			e.printStackTrace();
		}
		LOGGER.info("Exit doNavigation method");
		return fileName;
	}
	/**
	 * To add BasicNameValuePair from post request parameters
	 * parameters will be like =>
	 * key: value
	 * key: DATE
	 * key: FORMULA: REGEX
	 * key: JSON: 
	 * @param nameValuePairs
	 * @param inputStr
	 */
	private static void addNameValuePairs(List<BasicNameValuePair> nameValuePairs, String postParameters) {
		LOGGER.info("Inside addNameValuePairs method");
		String postParametersArr[] = postParameters.split("\n");
		int tempNum = 0;
		String key="";
		String value="";
		for(String postParameter : postParametersArr) {
			String postParameterArr[] = postParameter.split(": ");
			for(int i=0;i<postParameterArr.length;++i) {
				key = postParameterArr[i];
				value = postParameterArr[++i];
				if(value.equals("DATE")) {
					Date currentDate = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					value = formatter.format(currentDate);
				}else if(value.equals("FORMULA")) {
					String regex = postParameterArr[++i].replace("\\","\\\\");
					Pattern regexPattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE|Pattern.DOTALL|Pattern.MULTILINE);
					Matcher regexMatcher = regexPattern.matcher(browserResponce);
					if(regexMatcher.find()) {
						value = regexMatcher.group(1);
					} 
				}else if(value.equals("JSON")) {
					value = postParameterArr[++i];
					while(i<postParameterArr.length-1) {
						String paramName = postParameterArr[++i];
						String paramValue = postParameterArr[++i];
						if(paramValue.equals("DATE")) {
							Date currentDate = new Date();
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
							paramValue = formatter.format(currentDate);
						}else if(paramValue.equals("FORMULA")) {
							String regex = postParameterArr[++i].replace("\\","\\\\");
							Pattern regexPattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE|Pattern.DOTALL|Pattern.MULTILINE);
							Matcher regexMatcher = regexPattern.matcher(browserResponce);
							if(regexMatcher.find()) {
								paramValue = regexMatcher.group(1);
							}
						}
						value.replace(paramName, paramValue);
					}
				}
			}
			nameValuePairs.add(new BasicNameValuePair(key,value));
		}
		LOGGER.info("Exit addNameValuePairs method");
	}

	/**
	 * To add request headers to HttpGet object
	 * @param httpget
	 * @param requestHeaders
	 */
	private static void addRequestHeaders(HttpGet httpget, String requestHeaders) {
		String requestHeadersArr[] = requestHeaders.split("\n");
		String key="";
		String value="";
		for(String requestHeader : requestHeadersArr) {
			String requestHeaderArr[] = requestHeader.split(": ");
			if(requestHeaderArr!=null) {
				key = requestHeaderArr[0].trim();
				if(requestHeaderArr.length>1) {
					value = requestHeaderArr[1].trim();
				}
			}
			if(!key.isEmpty()) {
				httpget.addHeader(key,value);
			}
		}
	}

	/**
	 * To write response data to file and save to local.
	 * @param httpresponse
	 * @param filePath
	 * @return
	 */
	private static String writeToFile(HttpResponse httpresponse,String filePath) {
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
				LOGGER.info("Exception : "+e.getMessage());
			}
			File file = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line="";
			while((line=br.readLine())!=null) {
				browserResponce+=line+"\n";
			}
			br.close();
		}
		catch(Exception ex){
			LOGGER.error("Exception : "+ex.getMessage());
			ex.printStackTrace();
		}
		return browserResponce;
	}

	/**
	 * To create get URL from get request parameters
	 * @param parameters
	 * @return
	 */
	private static String createGetUrl(String parameters) {
		String url = "";
		String key = "";
		String value = "";
		if(StringUtils.isNotBlank(parameters)) {
			String parametersArr[] = parameters.trim().split("\n");
			for(String parameter : parametersArr) {
				String parameterArr[] = parameter.split(": ");
				key = parameterArr[0].trim();
				if(parameterArr.length>1) {
					value = parameterArr[1].trim().replace(" ", "+");
				}
				url+="&"+key;
				url+="="+value;
			}
			// to remove only starting & sign
			url=url.replaceAll("^&", "");
		}
		return url;
	}
	
	/**
	 * To create and save navigation XML file in local
	 * @param navigationDetailsList
	 * @return
	 */
	public String createNavigationFile(ArrayList<NavigationDetails> navigationDetailsList) {
		String baseUrl = "";
		String requestType = "";
		String parameters = "";
		String requestHeaders = "";
		String browserResp = "";
		String parentPath = "/home/mayank/Documents/";
		String fileName = "NavigationFile.xml";
		String navigationName="";
		StringBuilder xmlFileSb = new StringBuilder();
		try {
			xmlFileSb.append("<navigations>");
			for(NavigationDetails navigationDetails : navigationDetailsList) {
				navigationName=navigationDetails.getNavigationName();
				baseUrl = navigationDetails.getBaseUrl();
				requestType = navigationDetails.getRequestType();
				parameters = navigationDetails.getParameters();
				requestHeaders = navigationDetails.getRequestHeaders();
				xmlFileSb.append("\n\t<"+requestType.toLowerCase()+" name=\""+navigationName+"\" url=\""+baseUrl+"\">");
				addFields(xmlFileSb,parameters);
				xmlFileSb.append("\n\t</"+requestType.toLowerCase()+">\n");
			}
			writeToFile(xmlFileSb.append("</navigations>"));
		}catch(Exception e) {
			LOGGER.error("Error in doNavigation method : "+e.getMessage());
			e.printStackTrace();
		}
		return fileName;
	}
	/**
	 * To write data to file to create XML navigation file.
	 * @param xmlFileSb
	 * @throws IOException
	 */
	private void writeToFile(StringBuilder xmlFileSb) throws IOException {
		BufferedWriter writer = null;
		try {
			File file = new File("/home/mayank/Documents/NavigationFile.xml");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(xmlFileSb.toString());
		}catch(Exception e) {
			LOGGER.info("Error in appendToFile method : "+e.getMessage());
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				writer.close();
			}
		}
	}
	/**
	 * To add fields to XML navigation file.
	 * @param xmlFileSb
	 * @param parameters
	 */
	private void addFields(StringBuilder xmlFileSb,String parameters) {
		try {
			String parametersArr[] = parameters.split("\n");
			String key="";
			String value="";
			for(String parameter : parametersArr) {
				String parameterArr[] = parameter.split(": ");
				if(parameterArr!=null && parameterArr.length>1) {
					key = parameterArr[0].trim();
					xmlFileSb.append("\n\t\t<field name=\""+key+"\" value=");
					value = parameterArr[1].trim();
					xmlFileSb.append("\""+value+"\" />");
				}
			}
		}catch(Exception e) {
			LOGGER.error("Error in addFields method : "+e.getMessage());
			e.printStackTrace();
		}
	}
}
