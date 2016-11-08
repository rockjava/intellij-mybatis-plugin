package com.seventh7.mybatis.dom.converter;

import com.google.common.collect.ImmutableSet;

import com.intellij.util.xml.ConvertContext;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * @author yanglin
 */
public class StatementTypeConverter extends PlainTextConverter {

  private static final Set<String> TYPES = ImmutableSet.of("STATEMENT", "PREPARED", "CALLABLE");

  @NotNull @Override
  public Collection<? extends String> getVariants(ConvertContext context) {
    return TYPES;
  }

}
