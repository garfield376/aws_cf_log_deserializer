package com.snowplowanalytics.hive.serde;

import org.apache.hadoop.hive.serde2.SerDeException;

public final class WebCfLogDeserializer extends CfLogDeserializer {
	
	// For performance reasons we reuse the same object to deserialize all of our rows
	private static final CfLogStruct cachedStruct = new WebCfLogStruct();

	public WebCfLogDeserializer() throws SerDeException {
		super(WebCfLogStruct.class);
	}

	@Override
	protected Object parse(String row) throws SerDeException {
		cachedStruct.parse(row);
	    return cachedStruct;
	}
}
