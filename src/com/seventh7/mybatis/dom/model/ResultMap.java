package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.AliasConverter;
import com.seventh7.mybatis.dom.converter.ResultMapConverter;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface ResultMap extends GroupFour, IdDomElement{

  @NotNull
  @Attribute("extends")
  @Convert(ResultMapConverter.class)
  public GenericAttributeValue<XmlAttributeValue> getExtends();

  @NotNull
  @Attribute("type")
  @Convert(AliasConverter.class)
  public GenericAttributeValue<PsiClass> getType();

}