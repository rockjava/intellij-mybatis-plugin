package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.CacheRefNamespaceConverter;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface CacheRef extends MyBatisElement {

  @NotNull
  @Attribute("namespace")
  @Convert(CacheRefNamespaceConverter.class)
  public GenericAttributeValue<PsiClass> getNamespace();

}
