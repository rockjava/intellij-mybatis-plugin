package com.seventh7.mybatis.dom.converter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import com.intellij.util.xml.ConvertContext;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author yanglin
 */
public abstract class AcrossMapperXmlConverter extends IdBasedTagConverter {

  @NotNull @Override
  public Collection<? extends IdDomElement> getComparisons(@Nullable Mapper mapper, ConvertContext context) {
    if (mapper == null) {
      return Collections.emptyList();
    }
//    return isContextElement(mapper, context) ? doFilterResultMapItself(mapper, )
    return Collections.emptyList();
  }

  public abstract <T extends IdDomElement> Collection<T> doGetComparisons(@NotNull Mapper mapper);

  public abstract boolean isContextElement(Mapper mapper, ConvertContext convert);

  private <T extends IdDomElement> Collection<? extends IdDomElement> doFilterResultMapItself(Mapper mapper, final T element) {
    return Collections2.filter(doGetComparisons(mapper), new Predicate<IdDomElement>() {
      @Override public boolean apply(IdDomElement input) {
        return !MapperUtils.getId(input).equals(MapperUtils.getId(element));
      }
    });
  }

}
