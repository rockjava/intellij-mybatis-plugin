package com.seventh7.mybatis.dom.model;

import com.google.common.collect.ImmutableSet;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.PlainTextConverter;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author yanglin
 */
public interface LangGroup extends DomElement {

  @NotNull
  @Attribute("lang")
  @Convert(LangConverter.class)
  public GenericAttributeValue<String> getLang();

  public static class LangConverter extends PlainTextConverter {

    private static final java.util.Set<String> RAW_KNOWN = ImmutableSet.of("xml", "raw");

    @NotNull @Override
    public Collection<? extends String> getVariants(ConvertContext context) {
      return RAW_KNOWN;
    }

  }
}
