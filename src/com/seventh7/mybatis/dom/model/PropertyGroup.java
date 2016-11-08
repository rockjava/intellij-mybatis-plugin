package com.seventh7.mybatis.dom.model;

import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.PropertyConverter;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface PropertyGroup extends JdbcGroup, JavaTypeGroup {

  @Attribute("property")
  @Convert(PropertyConverter.class)
  GenericAttributeValue<XmlAttributeValue> getProperty();

  @NotNull
  @Attribute("column")
  public GenericAttributeValue<String> getColumn();

}
