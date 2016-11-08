package com.seventh7.mybatis.dom.converter;

import com.google.common.collect.Sets;

import com.intellij.util.xml.ConvertContext;
import com.seventh7.mybatis.util.SQLConstants;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * @author yanglin
 */
public class JdbcTypeConverter extends PlainTextConverter {

  private static final Set<String> TYPE_NAMES = Sets.newHashSet();

  static {
    for (int type : SQLConstants.sqlTypes()) {
      TYPE_NAMES.add(SQLConstants.getJdbcTypeName(type));
    }
  }

  @NotNull @Override
  public Collection<? extends String> getVariants(ConvertContext context) {
    return TYPE_NAMES;
  }

}
