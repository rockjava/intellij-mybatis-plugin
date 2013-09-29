package com.seventh7.mybatis.dom.model;

import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.PropertyConverter;

/**
 * @author yanglin
 */
public interface PropertyGroup extends DomElement {

  @Attribute("property")
  @Convert(PropertyConverter.class)
  GenericAttributeValue<XmlAttributeValue> getProperty();
}
