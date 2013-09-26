package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author yanglin
 */
public interface Collection extends GroupFour, ResultMapGroup {

  @Attribute("ofType")
  public GenericAttributeValue<PsiClass> getOfType();

}
