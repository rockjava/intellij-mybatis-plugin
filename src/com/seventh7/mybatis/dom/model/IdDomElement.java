package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface IdDomElement extends DomElement{

  @Required
  @NotNull
  @Attribute("id")
  public GenericAttributeValue<String> getId();

}
