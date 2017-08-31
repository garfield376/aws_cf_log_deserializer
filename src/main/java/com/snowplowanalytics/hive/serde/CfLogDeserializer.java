/* 
 * Copyright (c) 2012 Orderly Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.snowplowanalytics.hive.serde;

// Java
import java.nio.charset.CharacterCodingException;
import java.util.List;
import java.util.Properties;

// Commons Logging
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// Hadoop
import org.apache.hadoop.conf.Configuration;

// Hive
import org.apache.hadoop.hive.serde2.Deserializer;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.ReflectionStructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * CfLogDeserializer reads CloudFront download distribution file access log data into Hive.
 * 
 * For documentation please see the introductory README.md in the project root.
 */
public abstract class CfLogDeserializer implements Deserializer {

  // -------------------------------------------------------------------------------------------------------------------
  // Initial setup
  // -------------------------------------------------------------------------------------------------------------------

  // Setup logging
  public static final Log LOG = LogFactory.getLog(CfLogDeserializer.class.getName());

  // Voodoo taken from Zemanta's S3LogDeserializer
  static {
    StackTraceElement[] sTrace = new Exception().getStackTrace();
    sTrace[0].getClassName();
  }
  
  private java.lang.reflect.Type structType;

  // We'll initialize our object inspector below
  private ObjectInspector cachedObjectInspector;

  // -------------------------------------------------------------------------------------------------------------------
  // Constructor & initializer
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Empty constructor
   */
  public CfLogDeserializer(java.lang.reflect.Type structType) throws SerDeException {
	  this.structType = structType;
  }

  /**
   * Initialize the CfLogDeserializer.
   *
   * @param conf System properties
   * @param tbl Table properties
   * @throws SerDeException For any exception during initialization
   */
  public void initialize(Configuration conf, Properties tbl) throws SerDeException {

    cachedObjectInspector = ObjectInspectorFactory.getReflectionObjectInspector(structType, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
    LOG.debug(this.getClass().getName() + " initialized");
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Deserializer
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Deserialize an object out of a Writable blob. In most cases, the return
   * value of this function will be constant since the function will reuse the
   * returned object. If the client wants to keep a copy of the object, the
   * client needs to clone the returned value by calling
   * ObjectInspectorUtils.getStandardObject().
   * 
   * @param blob The Writable object containing a serialized object
   * @return A Java object representing the contents in the blob.
   * @throws SerDeException For any exception during initialization
   */
  public Object deserialize(Writable field) throws SerDeException {
    String row = null;
    if (field instanceof BytesWritable) {
      BytesWritable b = (BytesWritable) field;
      try {
        row = Text.decode(b.getBytes(), 0, b.getLength());
      } catch (CharacterCodingException e) {
        throw new SerDeException(e);
      }
    } else if (field instanceof Text) {
      row = field.toString();
    }
    try {
      return parse(row);
    } catch (ClassCastException e) {
      throw new SerDeException(this.getClass().getName() + " expects Text or BytesWritable", e);
    } catch (Exception e) {
      throw new SerDeException(e);
    }
  }
  
  protected abstract Object parse(String row) throws SerDeException;

  // -------------------------------------------------------------------------------------------------------------------
  // Getters
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Retrieve statistics for this SerDe. Returns null
   * because we don't support statistics (yet).
   *
   * @return The SerDe's statistics (null in this case)
   */
  public SerDeStats getSerDeStats() { return null; }

  /**
   * Get the object inspector that can be used to navigate through the internal
   * structure of the Object returned from deserialize(...).
   *
   * @return The ObjectInspector for this Deserializer 
   * @throws SerDeException For any exception during initialization
   */
  public ObjectInspector getObjectInspector() throws SerDeException { return cachedObjectInspector; }
}
