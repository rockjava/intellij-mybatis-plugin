package com.seventh7.mybatis.dom.model;

import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.ResultMapConverter;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface Select extends GroupTwo{

  @NotNull
  @Attribute("resultMap")
  @Convert(ResultMapConverter.class)
  public GenericAttributeValue<XmlTag> getResultMap();

  @Attribute("resultType")
  public GenericAttributeValue<String> getResultType();
}
