package com.seventh7.mybatis.intention;

import com.google.common.base.Function;

import com.intellij.javaee.dataSource.DataSource;
import com.intellij.javaee.dataSource.DataSourceManager;
import com.intellij.javaee.dataSource.DatabaseTableData;
import com.intellij.javaee.dataSource.DatabaseTableFieldData;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.GroupFour;
import com.seventh7.mybatis.generate.PropertyGenerator;
import com.seventh7.mybatis.intention.chooser.GeneratePropertyChooser;
import com.seventh7.mybatis.setting.MybatisSetting;
import com.seventh7.mybatis.ui.ListSelectionItemListener;
import com.seventh7.mybatis.ui.UiComponentFacade;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
  public void invoke(@NotNull final Project project,
                     Editor editor,
                     PsiFile file) throws IncorrectOperationException {

    final PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    final DomElement domElement = DomUtil.getDomElement(element);
    if (!(domElement instanceof GroupFour)) {
      return;
    }

    DataSourceManager dataSourceManager = DataSourceManager.getInstance(project);

    String dlftDataSourceId = MybatisSetting.getInstance().getDefaultDataSourceId();
    DataSource defaultDataSource = dataSourceManager.getDataSourceByID(dlftDataSourceId);
    final UiComponentFacade uiFacade = UiComponentFacade.getInstance(project);
    if (defaultDataSource == null) {
      selectDataSourceAndGenerate((GroupFour) domElement, dataSourceManager, uiFacade);
    } else {
      selectTableAndGenerate(uiFacade, defaultDataSource.getTables(), (GroupFour) domElement);
    }
  }

  private void selectDataSourceAndGenerate(final GroupFour domElement,
                                           DataSourceManager dataSourceManager,
                                           final UiComponentFacade uiFacade) {
    uiFacade.selectItems("[Select Data Source]",
                         dataSourceManager.getDataSources(),
                         new ListSelectionItemListener<DataSource>() {
                           @Override public void apply(DataSource dataSource) {
                             MybatisSetting.getInstance().setDefaultDataSourceId(dataSource.getUniqueId());
                             selectTableAndGenerate(uiFacade, dataSource.getTables(), domElement);
                           }
                         }, new Function<DataSource, String>() {
                           @Override public String apply(DataSource dataSource) {
                             return dataSource.getName();
                           }
                         }
    );
  }

  private void selectTableAndGenerate(final UiComponentFacade uiFacade,
                                      List<DatabaseTableData> table,
                                      final GroupFour groupFour) {
    uiFacade.selectItems("[Select Table]",
                         table,
                         new ListSelectionItemListener<DatabaseTableData>() {
                           @Override public void apply(DatabaseTableData dataSource) {
                             selectColumnsAndGenerate(uiFacade, dataSource.getColumns(), groupFour);
                           }

                         }, new Function<DatabaseTableData, String>() {
                           @Override public String apply(DatabaseTableData table) {
                             return table.getName();
                           }
                         }
    );
  }

  private void selectColumnsAndGenerate(UiComponentFacade uiFacade,
                                        List<DatabaseTableFieldData> columns,
                                        final GroupFour groupFour) {
    uiFacade.selectItems("[Select Columns]",
                         columns,
                         new ListSelectionItemListener<DatabaseTableFieldData>() {
                           @Override
                           public void apply(Collection<DatabaseTableFieldData> columns) {
                             PropertyGenerator.generateProperties(columns, groupFour);
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
