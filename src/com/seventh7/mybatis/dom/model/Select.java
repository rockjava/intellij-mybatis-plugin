package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author yanglin
 */
public interface Select extends GroupTwo, ResultMapGroup{

  @Attribute("resultType")
  public GenericAttributeValue<String> getResultType();
}
