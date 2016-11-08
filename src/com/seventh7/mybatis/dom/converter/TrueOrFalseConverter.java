package com.seventh7.mybatis.dom.converter;

import com.google.common.collect.ImmutableSet;

import com.intellij.util.xml.ConvertContext;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * @author yanglin
 */
public class TrueOrFalseConverter extends PlainTextConverter {

  private static final Set<String> RESULTS = ImmutableSet.of("true", "false");

  @NotNull @Override
  public Collection<? extends String> getVariants(ConvertContext context) {
    return RESULTS;
  }
}
