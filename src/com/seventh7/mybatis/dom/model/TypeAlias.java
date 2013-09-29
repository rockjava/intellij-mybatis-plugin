package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author yanglin
 */
public interface TypeAlias extends DomElement {

  @Attribute("type")
  public GenericAttributeValue<PsiClass> getType();

  @Attribute("alias")
  public GenericAttributeValue<String> getAlias();

}
