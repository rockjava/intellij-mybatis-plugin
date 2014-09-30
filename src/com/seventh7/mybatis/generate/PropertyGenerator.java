package com.seventh7.mybatis.generate;

import com.intellij.database.dataSource.DatabaseTableFieldData;
import com.intellij.psi.xml.XmlElement;
import com.seventh7.mybatis.dom.model.GroupFour;
import com.seventh7.mybatis.dom.model.PropertyGroup;
import com.seventh7.mybatis.service.EditorService;
import com.seventh7.mybatis.util.SQLUtil;

import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author yanglin
 */
public class PropertyGenerator {

  public static final PropertyNameStrategy PROPERTY_NAME_STRATEGY = new HumpStrategy();

  public static void generateProperties(@NotNull Collection<DatabaseTableFieldData> columns,
                                        @NotNull GroupFour groupFour) {
    final XmlElement element = groupFour.getXmlElement();
    if (columns.isEmpty() || element == null)  {
      return;
    }

    PropertyGroup property;
    for (DatabaseTableFieldData column : columns) {
      if (column.isPrimary()) {
        property = groupFour.addId();
      } else {
        property = groupFour.addResult();
      }
      setupProperties(column, property);
    }
    EditorService.getInstance(element.getProject()).format(element.getContainingFile(), groupFour.getXmlElement());
  }

  private static void setupProperties(DatabaseTableFieldData column, PropertyGroup property) {
    property.getJdbcType().setStringValue(SQLUtil.getJdbcTypeName(column.getJdbcType()));
    final String columnName = column.getName();
    property.getProperty().setStringValue(PROPERTY_NAME_STRATEGY.apply(columnName));
    property.getColumn().setStringValue(columnName);
  }

  interface PropertyNameStrategy {
    String apply(String columnName);
  }

  /** The only strategy till now */
  public static class HumpStrategy implements PropertyNameStrategy {

    @Override public String apply(String columnName) {
      StringBuilder sb = new StringBuilder();
      final String[] split = columnName.split("_");
      for (int i = 0; i < split.length; i++) {
        if (i == 0) {
          sb.append(WordUtils.uncapitalize(split[i]));
        } else {
          sb.append(WordUtils.capitalize(split[i]));
        }
      }
      return sb.toString();
    }

  }

}
