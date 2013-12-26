package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.JdbcTypeConverter;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface JdbcGroup extends DomElement {

  @NotNull
  @Attribute("jdbcType")
  @Convert(JdbcTypeConverter.class)
  public GenericAttributeValue<PsiClass> getJdbcType();

}
