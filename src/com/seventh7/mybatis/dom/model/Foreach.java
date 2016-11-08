package com.seventh7.mybatis.dom.model;

import com.google.common.collect.ImmutableSet;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.converter.PlainTextConverter;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author yanglin
 */
public interface Foreach extends GroupOne {

  @NotNull
  @Attribute("collection")
  @Convert(ForEachCollectionConverter.class)
  public GenericAttributeValue<String> getCollection();

  public static class ForEachCollectionConverter extends PlainTextConverter {

    private static final java.util.Set<String> TYPES_KNOWN = ImmutableSet.of("list", "array");

    @NotNull @Override
    public Collection<? extends String> getVariants(ConvertContext context) {
      return TYPES_KNOWN;
    }
  }
}
