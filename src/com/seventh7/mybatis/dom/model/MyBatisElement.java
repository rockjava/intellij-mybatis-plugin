package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author yanglin
 */
public interface MyBatisElement extends DomElement {

  @Attribute("databaseId")
  public GenericAttributeValue<String> getDatabaseId();

}
