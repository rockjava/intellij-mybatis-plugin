package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.AliasConverter;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface JavaTypeGroup extends MyBatisElement {

  @NotNull
  @Attribute("javaType")
  @Convert(AliasConverter.class)
  public GenericAttributeValue<PsiClass> getJavaType();

}
