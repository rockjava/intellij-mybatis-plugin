package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface Collection extends GroupFour, ResultMapGroup {

  @NotNull
  @Attribute("ofType")
  public GenericAttributeValue<PsiClass> getOfType();

}
