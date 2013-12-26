package com.seventh7.mybatis.dom.model;

import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.ReferenceToSelectConverter;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface ReferenceToSelectGroup extends DomElement{

  @NotNull
  @Attribute("select")
  @Convert(ReferenceToSelectConverter.class)
  public GenericAttributeValue<XmlAttributeValue> getExtends();

}
