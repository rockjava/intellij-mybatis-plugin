package com.seventh7.mybatis.dom.converter;

import com.intellij.util.xml.ConvertContext;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public abstract class PlainTextConverter extends ConverterAdaptor<String> {

  @Nullable @Override
  public String toString(@Nullable String s, ConvertContext context) {
    return s;
  }

  @Nullable @Override
  public String fromString(@Nullable @NonNls String s, ConvertContext context) {
    return s;
  }
}
