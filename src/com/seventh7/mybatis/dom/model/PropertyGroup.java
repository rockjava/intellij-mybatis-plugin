package com.seventh7.mybatis.dom.model;

import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.ResultPropertyConverter;

/**
 * @author yanglin
 */
public interface PropertyGroup extends DomElement {

  @Attribute("property")
  @Convert(ResultPropertyConverter.class)
  GenericAttributeValue<XmlAttributeValue> getProperty();
}
