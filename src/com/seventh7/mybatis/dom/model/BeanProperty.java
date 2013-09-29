package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author yanglin
 */
public interface BeanProperty extends DomElement {

  @Attribute("name")
  public GenericAttributeValue<String> getName();

  @Attribute("value")
  public GenericAttributeValue<String> getValue();
}
