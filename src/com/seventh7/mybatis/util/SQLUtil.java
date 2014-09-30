package com.seventh7.mybatis.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.sql.Types;

/**
 * @author yanglin
 */
public final class SQLUtil {

  private SQLUtil() {
    throw new UnsupportedOperationException();
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
