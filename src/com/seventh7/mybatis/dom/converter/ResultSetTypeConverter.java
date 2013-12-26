package com.seventh7.mybatis.dom.converter;

import com.google.common.collect.ImmutableSet;

import com.intellij.util.xml.ConvertContext;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * @author yanglin
 */
public class ResultSetTypeConverter extends PlainTextConverter {

  private static final Set<String> TYPES = ImmutableSet.of("FORWARD_ONLY", "SCROLL_SENSITIVE", "SCROLL_INSENSITIVE");

  @NotNull @Override
  public Collection<? extends String> getVariants(ConvertContext context) {
    return TYPES;
  }

}
