package com.seventh7.mybatis.intention;

import com.intellij.javaee.dataSource.DatabaseTableFieldData;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.db.ColumnsSelector;
import com.seventh7.mybatis.intention.chooser.ColumnIntentionChooser;
import com.seventh7.mybatis.ui.ListSelectionItemListener;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @author yanglin
 */
public class GenerateColumnIntentionAction extends GenericIntention {

  private static final Pattern PATTERN = Pattern.compile("\\s*");;

  public GenerateColumnIntentionAction() {
    super(ColumnIntentionChooser.INSTANCE);
  }

  @NotNull @Override
  public String getText() {
    return "[Mybatis] Generate columns with alias";
  }

  @Override
  public void invoke(@NotNull final Project project,
                     final Editor editor,
                     final PsiFile file) throws IncorrectOperationException {
    ColumnsSelector.selectColumns(project, new ListSelectionItemListener<DatabaseTableFieldData>() {
      @Override public void apply(Collection<DatabaseTableFieldData> columns) {
        String alias = Messages.showInputDialog(project, "Select a alias", "Generate Columns", Messages.getQuestionIcon());
        if (StringUtils.isNotBlank(alias)) {
          generateColumns(editor, alias, columns);
        }
      }
    });
  }

  private static void generateColumns(Editor editor, String alias, Collection<DatabaseTableFieldData> columns) {
    alias = PATTERN.matcher(alias).replaceAll("");
    boolean beginningDot = false;
    boolean endDot = false;
    if (alias.indexOf(',') == 0) {
      beginningDot = true;
      alias = alias.substring(1, alias.length());
    }

    if (alias.lastIndexOf(',') == alias.length() - 1) {
      endDot = true;
      alias = alias.substring(0, alias.length() - 1);
    }

    if (StringUtils.isBlank(alias)) {
      return;
    }

    StringBuilder sb = new StringBuilder(beginningDot ? ", " : "");
    for (DatabaseTableFieldData column : columns) {
      sb.append(alias);
      sb.append('.');
      sb.append(column.getName());
      sb.append(", ");
    }

    if (!endDot) {
      sb.replace(sb.length() - 2, sb.length() - 1, "");
    }

    editor.getDocument().insertString(editor.getCaretModel().getOffset(), sb.toString());
  }

}
