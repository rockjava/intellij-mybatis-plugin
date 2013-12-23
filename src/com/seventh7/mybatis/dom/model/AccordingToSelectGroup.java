package com.seventh7.mybatis.dom.model;

import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.AccordingToSelectConverter;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface AccordingToSelectGroup extends DomElement{

  @NotNull
  @Attribute("select")
  @Convert(AccordingToSelectConverter.class)
  public GenericAttributeValue<XmlAttributeValue> getExtends();

}
