package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;
import com.intellij.util.xml.Required;

/**
 * @author yanglin
 */
public interface IdDomElement extends MyBatisElement{

  @Required
  @NameValue
  @Attribute("id")
  public GenericAttributeValue<String> getId();

  public void setValue(String content);
}
