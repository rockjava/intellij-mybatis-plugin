package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.GenericAttributeValue;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface Association extends GroupFour, ResultMapGroup {

  @NotNull
  @Attribute("javaType")
  public GenericAttributeValue<PsiClass> getJavaType();
}
