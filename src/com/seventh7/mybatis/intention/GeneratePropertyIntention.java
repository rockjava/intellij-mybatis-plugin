package com.seventh7.mybatis.intention;

import com.google.common.base.Function;

import com.intellij.javaee.dataSource.DataSource;
import com.intellij.javaee.dataSource.DataSourceManager;
import com.intellij.javaee.dataSource.DatabaseTableData;
import com.intellij.javaee.dataSource.DatabaseTableFieldData;
import com.intellij.javaee.dataSource.SQLUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.GroupFour;
import com.seventh7.mybatis.dom.model.PropertyGroup;
import com.seventh7.mybatis.service.EditorService;
import com.seventh7.mybatis.setting.MybatisSetting;
import com.seventh7.mybatis.ui.ListSelectionListener;
import com.seventh7.mybatis.ui.UiComponentFacade;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author yanglin
 */
public class GeneratePropertyIntention extends GenericIntention {

  public GeneratePropertyIntention() {
    super(GeneratePropertyChooser.INSTANCE);
  }

  @NotNull @Override
  public String getText() {
    return "[Mybatis] Generate properties";
  }

  @Override
  public void invoke(@NotNull final Project project, Editor editor, PsiFile file)
      throws IncorrectOperationException {

    final PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    final DomElement domElement = DomUtil.getDomElement(element);
    if (!(domElement instanceof GroupFour)) {
      return;
    }

    DataSourceManager dataSourceManager = DataSourceManager.getInstance(project);

    String dlftDataSourceId = MybatisSetting.getInstance().getDlftDataSourceId();
    DataSource defaultDataSource = dataSourceManager.getDataSourceByID(dlftDataSourceId);
    if (defaultDataSource == null) {
      selectDataSource(project, file, dataSourceManager.getDataSources(), (GroupFour)domElement);
    } else {
      selectTable(project, file, defaultDataSource, (GroupFour)domElement);
    }
  }

  private void selectColumns(final Project project, final PsiFile file, final List<DatabaseTableFieldData> columns, final GroupFour groupFour) {
    final UiComponentFacade uiFacade = UiComponentFacade.getInstance(project);
    uiFacade.showListPopup("[Select columns to generate]", new ListSelectionListener() {
                             @Override public void selected(int[] indexes) {
                               for (int index : indexes) {
                                 final DatabaseTableFieldData column = columns.get(index);
                                 PropertyGroup property = null;
                                 if (column.isPrimary()) {
                                   property = groupFour.addId();
                                 } else {
                                   property = groupFour.addResult();
                                 }
                                 property.getJdbcType().setStringValue(SQLUtil.getJdbcTypeName(column.getJdbcType()));
                                 property.getProperty().setStringValue("aaaaa");
                                 property.getColumn().setStringValue(column.getName());
                               }
                               EditorService.getInstance(project).format(file, groupFour.getXmlElement());
                             }

                             @Override public boolean isWriteAction() {
                               return true;
                             }
                           }, columns, new Function<DatabaseTableFieldData, String>() {
                             @Override
                             public String apply(DatabaseTableFieldData column) {
                               final String name = column.getName();
                               if (column.isPrimary()) {
                                 return name + "              [primary key]";
                               } else if (column.isForeign()) {
                                 return name + "              [foreign key]";
                               } else if (column.isNullable()) {
                                 return name + "              [not null]";
                               } else {
                                 return name;
                               }
                             }
                           }
    );
  }

  public void selectTable(final Project project,
                          final PsiFile file,
                          DataSource dataSource,
                          final GroupFour groupFour) {
    final UiComponentFacade uiFacade = UiComponentFacade.getInstance(project);
    final List<DatabaseTableData> tables = dataSource.getTables();
    uiFacade.showListPopup("[Select Table]", new ListSelectionListener() {
                             @Override public void selected(int index) {
                               selectColumns(project, file, tables.get(index).getColumns(), groupFour);
                             }
                           }, tables, new Function<DatabaseTableData, String>() {
                             @Override public String apply(DatabaseTableData table) {
                               return table.getName();
                             }
                           }
    );
  }

  private void selectDataSource(final Project project,
                                final PsiFile file,
                                final List<DataSource> dataSources,
                                final GroupFour groupFour) {
    final UiComponentFacade uiFacade = UiComponentFacade.getInstance(project);
    uiFacade.showListPopup("[Select Data Source]", new ListSelectionListener() {
                             @Override public void selected(int index) {
                               DataSource dataSource = dataSources.get(index);
                               selectTable(project, file, dataSource, groupFour);
                               MybatisSetting.getInstance().setDlftDataSourceId(dataSource.getUniqueId());
                             }
                           }, dataSources, new Function<DataSource, String>() {
                             @Override public String apply(DataSource dataSource) {
                               return dataSource.getName();
                             }
                           }
    );
  }

}
