package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author yanglin
 */
public interface Association extends GroupFour, ResultMapGroup {

  @Attribute("javaType")
  public GenericAttributeValue<PsiClass> getJavaType();
}
