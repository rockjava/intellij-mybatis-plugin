package com.seventh7.mybatis.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.sql.Types;
import java.util.Arrays;

/**
 * @author yanglin
 */
public final class SQLConstants {

  private SQLConstants() {
    throw new UnsupportedOperationException();
  }

  private static final int[] SQL_TYPES = {
      Types.BIT,          Types.TINYINT,        Types.SMALLINT,         Types.INTEGER,          Types.BIGINT,
      Types.FLOAT,        Types.REAL,           Types.DOUBLE,           Types.NUMERIC,          Types.DECIMAL,
      Types.CHAR,         Types.VARCHAR,        Types.LONGVARCHAR,      Types.DATE,             Types.TIME,
      Types.TIMESTAMP,    Types.BINARY,         Types.VARBINARY,        Types.LONGVARBINARY,    Types.NULL,
      Types.OTHER,        Types.JAVA_OBJECT,    Types.DISTINCT,         Types.STRUCT,           Types.ARRAY,
      Types.BLOB,         Types.CLOB,           Types.REF,              Types.DATALINK,         Types.BOOLEAN,
      Types.ROWID,        Types.NCHAR,          Types.NVARCHAR,         Types.LONGNVARCHAR,     Types.NCLOB,
      Types.SQLXML
  };

  public static int[] sqlTypes() {
    return Arrays.copyOf(SQL_TYPES, SQL_TYPES.length);
  }

  @NotNull
  public static String getJdbcTypeName(final int jdbcType) {
    @NonNls
    final String result;
    switch (jdbcType) {
      case Types.BIT:
        result = "BIT";
        break;
      case Types.TINYINT:
        result = "TINYINT";
        break;
      case Types.SMALLINT:
        result = "SMALLINT";
        break;
      case Types.INTEGER:
        result = "INTEGER";
        break;
      case Types.BIGINT:
        result = "BIGINT";
        break;
      case Types.FLOAT:
        result = "FLOAT";
        break;
      case Types.REAL:
        result = "REAL";
        break;
      case Types.DOUBLE:
        result = "DOUBLE";
        break;
      case Types.NUMERIC:
        result = "NUMERIC";
        break;
      case Types.DECIMAL:
        result = "DECIMAL";
        break;
      case Types.CHAR:
        result = "CHAR";
        break;
      case Types.VARCHAR:
        result = "VARCHAR";
        break;
      case Types.LONGVARCHAR:
        result = "LONGVARCHAR";
        break;
      case Types.DATE:
        result = "DATE";
        break;
      case Types.TIME:
        result = "TIME";
        break;
      case Types.TIMESTAMP:
        result = "TIMESTAMP";
        break;
      case Types.BINARY:
        result = "BINARY";
        break;
      case Types.VARBINARY:
        result = "VARBINARY";
        break;
      case Types.LONGVARBINARY:
        result = "LONGVARBINARY";
        break;
      case Types.NULL:
        result = "NULL";
        break;
      case Types.OTHER:
        result = "OTHER";
        break;
      case Types.JAVA_OBJECT:
        result = "JAVA_OBJECT";
        break;
      case Types.DISTINCT:
        result = "DISTINCT";
        break;
      case Types.STRUCT:
        result = "STRUCT";
        break;
      case Types.ARRAY:
        result = "ARRAY";
        break;
      case Types.BLOB:
        result = "BLOB";
        break;
      case Types.CLOB:
        result = "CLOB";
        break;
      case Types.REF:
        result = "REF";
        break;
      case Types.DATALINK:
        result = "DATALINK";
        break;
      case Types.BOOLEAN:
        result = "BOOLEAN";
        break;
      default:
        result = "UNKNOWN";
    }
    return result;
  }

}
