package com.seventh7.mybatis.dom.converter;

import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.ResolvingConverter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author yanglin
 */
public abstract class FakeConverter<T> extends ResolvingConverter<T> {

  @NotNull @Override
  public Collection<? extends T> getVariants(ConvertContext context) {
    return Collections.emptyList();
  }

  @Nullable @Override
  public String toString(@Nullable T t, ConvertContext context) {
    throw new UnsupportedOperationException();
  }

}
