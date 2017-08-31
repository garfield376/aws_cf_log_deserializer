package com.snowplowanalytics.hive.serde;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RtmpCfLogStruct extends CfLogStruct {
	
	  public String dt; 
	  public String tm; 
	  public String edgelocation; 
	  public Integer bytessent;  
	  public String ipaddress; 
	  public String operation;
	  public String domain;
	  public String object;
	  public Integer httpstatus;
	  public String referrer;
	  public String useragent;
	  public String querystring;
	  
	  private static final String w = "[\\s]+"; // Whitespace regex
	  private static final Pattern cfRegex = Pattern.compile("([\\S]+)"  // Date          / date
	                                                   + w + "([\\S]+)"  // Time          / time
	                                                   + w + "([\\S]+)"  // EdgeLocation  / x-edge-location
	                                                   + w + "([\\S]+)"  // BytesSent     / sc-bytes
	                                                   + w + "([\\S]+)"  // IPAddress     / c-ip
	                                                   + w + "([\\S]+)"  // Operation     / cs-method
	                                                   + w + "([\\S]+)"  // Domain        / cs(Host)
	                                                   + w + "([\\S]+)"  // Object        / cs-uri-stem
	                                                   + w + "([\\S]+)"  // HttpStatus    / sc-status
	                                                   + w + "([\\S]+)"  // Referrer      / cs(Referer)
	                                                   + w + "([\\S]+)"  // UserAgent     / cs(User Agent)
	                                                   + w + "(.+)");    // Querystring   / cs-uri-query

	public RtmpCfLogStruct() {
		regex = cfRegex;
	}

	@Override
	protected void parseCore(Matcher m) throws Exception {
		this.dt = m.group(1);
		  this.tm = m.group(2); // No need for toHiveDate any more - CloudFront date format matches Hive's
		  this.edgelocation = m.group(3);
		  this.bytessent = toInt(m.group(4));
		  this.ipaddress = m.group(5);
		  this.operation = m.group(6);
		  this.domain = m.group(7);
		  this.object = m.group(8);
		  this.httpstatus = toInt(m.group(9));
		  this.referrer = nullifyHyphen(m.group(10));
		  this.useragent = m.group(11);
		  this.querystring = nullifyHyphen(m.group(12));

	}

}
