package com.seventh7.mybatis.dom.model;

import com.google.common.collect.ImmutableSet;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;
import com.seventh7.mybatis.dom.converter.PlainTextConverter;
import com.seventh7.mybatis.dom.converter.TrueOrFalseConverter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yanglin
 */
public interface Cache extends DomElement {

  @SubTagList("property")
  public List<Property> getProperties();

  @NotNull
  @Attribute("eviction")
  @Convert(CacheEvictionConverter.class)
  public GenericAttributeValue<String> getEviction();

  @NotNull
  @Attribute("readOnly")
  @Convert(TrueOrFalseConverter.class)
  public GenericAttributeValue<String> getReadOnly();

  public static class CacheEvictionConverter extends PlainTextConverter{

    private static final java.util.Set<String> EVICTIONS = ImmutableSet.of("LRU", "FIFO", "SOFT", "WEAK");

    @NotNull @Override
    public java.util.Collection<? extends String> getVariants(ConvertContext context) {
      return EVICTIONS;
    }

  }

}
