package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author yanglin
 */
public interface IdDomElement extends DomElement{

  @Attribute("id")
  public GenericAttributeValue<String> getId();

  public void setValue(String content);
}
