package com.snowplowanalytics.hive.serde;

import org.apache.hadoop.hive.serde2.SerDeException;

public final class RtmpCfLogDeserializer extends CfLogDeserializer {

	private static final CfLogStruct cachedStruct = new RtmpCfLogStruct();

	public RtmpCfLogDeserializer() throws SerDeException {
		super(RtmpCfLogStruct.class);
	}

	@Override
	protected Object parse(String row) throws SerDeException {
		cachedStruct.parse(row);
	    return cachedStruct;
	}

}
