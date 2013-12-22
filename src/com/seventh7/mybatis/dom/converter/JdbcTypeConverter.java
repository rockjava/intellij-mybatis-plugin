package com.seventh7.mybatis.dom.converter;

import com.google.common.collect.Sets;

import com.intellij.javaee.dataSource.SQLUtil;
import com.intellij.util.xml.ConvertContext;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Types;
import java.util.Collection;
import java.util.Set;

/**
 * @author yanglin
 */
public class JdbcTypeConverter extends ConverterAdaptor<String> {

  private static final Set<String> TYPE_NAMES = Sets.newHashSet();

  private static final int[] TYPES = {
      Types.BIT,          Types.TINYINT,        Types.SMALLINT,         Types.INTEGER,          Types.BIGINT,
      Types.FLOAT,        Types.REAL,           Types.DOUBLE,           Types.NUMERIC,          Types.DECIMAL,
      Types.CHAR,         Types.VARCHAR,        Types.LONGVARCHAR,      Types.DATE,             Types.TIME,
      Types.TIMESTAMP,    Types.BINARY,         Types.VARBINARY,        Types.LONGVARBINARY,    Types.NULL,
      Types.OTHER,        Types.JAVA_OBJECT,    Types.DISTINCT,         Types.STRUCT,           Types.ARRAY,
      Types.BLOB,         Types.CLOB,           Types.REF,              Types.DATALINK,         Types.BOOLEAN,
      Types.ROWID,        Types.NCHAR,          Types.NVARCHAR,         Types.LONGNVARCHAR,     Types.NCLOB,
      Types.SQLXML
  };

  static {
    for (int type : TYPES) {
      TYPE_NAMES.add(SQLUtil.getJdbcTypeName(type));
    }
  }

  @NotNull @Override
  public Collection<? extends String> getVariants(ConvertContext context) {
    return TYPE_NAMES;
  }

  @Nullable @Override
  public  String toString(@Nullable String s, ConvertContext context) {
    return s;
  }

  @Nullable @Override
  public String fromString(@Nullable @NonNls String s, ConvertContext context) {
    return s;
  }
}
