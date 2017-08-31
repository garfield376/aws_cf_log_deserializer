package com.snowplowanalytics.hive.serde;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebCfLogStruct extends CfLogStruct {

	  // -------------------------------------------------------------------------------------------------------------------
	  // Mutable properties for this Hive struct
		
	  //#Fields: date time x-edge-location c-ip x-event sc-bytes x-cf-status x-cf-client-id cs-uri-stem cs-uri-query c-referrer x-page-url​	c-user-agent x-sname x-sname-query x-file-ext x-sid
	  //2010-03-12	 23:56:21	 SEA4	 192.0.2.199	 stop	 429822014	 OK	 bfd8a98bee0840d9b871b7f6ade9908f	 rtmp://shqshne4jdp4b6.cloudfront.net/cfx/st​	key=value	 http://player.longtailvideo.com/player.swf	 http://www.longtailvideo.com/support/jw-player-setup-wizard?example=204	 LNX%2010,0,32,18	 dir/favs/myothervideo	 p=42&q=14	 mp4	 2
	  // -------------------------------------------------------------------------------------------------------------------
  
	  public String dt; //date
	  public String tm; //time
	  public String edgelocation; //x-edge-location
	  public Integer bytessent;  //sc-bytes
	  public String ipaddress; //c-ip
	  public String operation;  //cs-method 
	  public String domain;     //cs(Host)
	  public String object;     //cs-uri-stem
	  public Integer httpstatus; //sc-status
	  public String referrer;    //cs(Referer) 
	  public String useragent;   //cs(User-Agent)
	  public String querystring; //cs-uri-query
	  public String cookie;     //cs(Cookie) 
	  public String resulttype; //x-edge-result-type 
	  public String requestid;  //x-edge-request-id 
	  public String hostheader;  //x-host-header
	  public String protocal;   //cs-protocol 
	  public Integer  bytesback;  //cs-bytes  
	  public Float timetaken;  //time-taken 
	  public String forwardip;  //x-forwarded-for 
	  public String sslprotocal;  //ssl-protocol 
	  public String sslcipher;  //ssl-cipher
	  public String responseresulttype; //x-edge-response-result-type 
	  public String protocalversion;  //cs-protocol-version
	  
	  

	  // -------------------------------------------------------------------------------------------------------------------
	  // Static configuration
	  // -------------------------------------------------------------------------------------------------------------------

	  // Define the regular expression for extracting the fields
	  // Adapted from Amazon's own cloudfront-loganalyzer.tgz
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
	                                                   + w + "([\\S]+)"  // Querystring   / cs-uri-query
	                                                   + w + "([\\S]+)"  // Cookie         / cs(Cookie) 
	                                                   + w + "([\\S]+)"  // Resulttype     / x-edge-result-type
	                                                   + w + "([\\S]+)"  // Requestid      / x-edge-request-id
	                                                   + w + "([\\S]+)"  // Hostheader     / x-host-header
	                                                   + w + "([\\S]+)"  // Protocal       / cs-protocol 
	                                                   + w + "([\\S]+)"  // Bytesback;     / cs-bytes
	                                                   + w + "([\\S]+)"  // timetaken;     / time-taken
	                                                   + w + "([\\S]+)"  // Forwardip;     / x-forwarded-for
	                                                   + w + "([\\S]+)"  // Sslprotocal;   / ssl-protocol
	                                                   + w + "([\\S]+)"  // Sslcipher;     / ssl-cipher
	                                                   + w + "([\\S]+)"  // Responseresulttype;  / x-edge-response-result-type
	                                                   + w + "(.+)" );   // Protocalversion;    /cs-protocol-version
	                                                   		   

	public WebCfLogStruct() {
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
	  this.cookie = nullifyHyphen(m.group(13));
	  this.resulttype = m.group(14);
	  this.requestid = m.group(15);
	  this.hostheader = m.group(16);
	  this.protocal = m.group(17);
	  this.bytesback = toInt(m.group(18));
	  this.timetaken = toFloat(m.group(19));
	  this.forwardip = nullifyHyphen(m.group(20));
	  this.sslprotocal = nullifyHyphen(m.group(21));
	  this.sslcipher = nullifyHyphen(m.group(22));
	  this.responseresulttype = m.group(23);
	  this.protocalversion = m.group(24);
	  
	}

}
