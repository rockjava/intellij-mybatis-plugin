package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;

/**
 * @author yanglin
 */
public interface SelectKey extends GroupTwo, ResultTypeGroup {

  @Required(value = false)
  public GenericAttributeValue<String> getId();

}
