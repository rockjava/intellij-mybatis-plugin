package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface Package extends MyBatisElement {

  @NotNull
  @Attribute("name")
  public GenericAttributeValue<String> getName();

}
