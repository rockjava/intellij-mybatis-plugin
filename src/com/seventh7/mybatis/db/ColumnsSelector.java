package com.seventh7.mybatis.db;

import com.google.common.base.Function;

import com.intellij.javaee.dataSource.DataSource;
import com.intellij.javaee.dataSource.DataSourceManager;
import com.intellij.javaee.dataSource.DatabaseTableData;
import com.intellij.javaee.dataSource.DatabaseTableFieldData;
import com.intellij.openapi.project.Project;
import com.seventh7.mybatis.setting.MybatisSetting;
import com.seventh7.mybatis.ui.ListSelectionItemListener;
import com.seventh7.mybatis.ui.UiComponentFacade;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * @author yanglin
 */
public class ColumnsSelector {

  public static void selectColumns(@NotNull Project project, @NotNull ListSelectionItemListener<DatabaseTableFieldData> listener) {
    DataSourceManager dataSourceManager = DataSourceManager.getInstance(project);

    String dlftDataSourceId = MybatisSetting.getInstance().getDefaultDataSourceId();
    DataSource defaultDataSource = dataSourceManager.getDataSourceByID(dlftDataSourceId);
    final UiComponentFacade uiFacade = UiComponentFacade.getInstance(project);
    if (defaultDataSource == null) {
      selectDataSourceAndApply(listener, dataSourceManager, uiFacade);
    } else {
      selectTableAndApply(uiFacade, defaultDataSource.getTables(), listener);
    }
  }

  private static void selectDataSourceAndApply(final ListSelectionItemListener<DatabaseTableFieldData> listener,
                                               DataSourceManager dataSourceManager,
                                               final UiComponentFacade uiFacade) {
    uiFacade.selectItems("[Select Data Source]",
                         dataSourceManager.getDataSources(),
                         new ListSelectionItemListener<DataSource>() {
                           @Override public void apply(DataSource dataSource) {
                             MybatisSetting.getInstance().setDefaultDataSourceId(dataSource.getUniqueId());
                             selectTableAndApply(uiFacade, dataSource.getTables(), listener);
                           }
                         }, new Function<DataSource, String>() {
          @Override public String apply(DataSource dataSource) {
            return dataSource.getName();
          }
        }
    );
  }

  private static void selectTableAndApply(final UiComponentFacade uiFacade,
                                          List<DatabaseTableData> table,
                                          final ListSelectionItemListener<DatabaseTableFieldData> listener) {
    uiFacade.selectItems("[Select Table]",
                         table,
                         new ListSelectionItemListener<DatabaseTableData>() {
                           @Override public void apply(DatabaseTableData dataSource) {
                             selectColumnsAndApply(uiFacade, dataSource.getColumns(), listener);
                           }

                         }, new Function<DatabaseTableData, String>() {
          @Override public String apply(DatabaseTableData table) {
            return table.getName();
          }
        }
    );
  }

  private static void selectColumnsAndApply(UiComponentFacade uiFacade,
                                            List<DatabaseTableFieldData> columns,
                                            final ListSelectionItemListener<DatabaseTableFieldData> listener) {
    uiFacade.selectItems("[Select Columns]",
                         columns,
                         new ListSelectionItemListener<DatabaseTableFieldData>() {
                           @Override
                           public void apply(Collection<DatabaseTableFieldData> columns) {
                             listener.apply(columns);
                           }

                           @Override
                           public void apply(DatabaseTableFieldData columns) {
                             listener.apply(columns);
                           }

                           @Override public boolean isWriteAction() {
                             return true;
                           }
                         }, new Function<DatabaseTableFieldData, String>() {
          @Override
          public String apply(DatabaseTableFieldData column) {
            return getColumnText(column);
          }
        }
    );
  }

  private static String getColumnText(DatabaseTableFieldData column) {
    final String name = column.getName();
    if (column.isPrimary()) {
      return name + "              [primary key]";
    } else if (column.isForeign()) {
      return name + "              [foreign key]";
    } else if (!column.isNullable()) {
      return name + "              [not null]";
    } else {
      return name;
    }
  }

}
