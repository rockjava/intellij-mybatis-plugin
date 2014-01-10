package com.seventh7.mybatis.intention;

import com.intellij.javaee.dataSource.DatabaseTableFieldData;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.GroupFour;
import com.seventh7.mybatis.db.ColumnsSelector;
import com.seventh7.mybatis.generate.PropertyGenerator;
import com.seventh7.mybatis.intention.chooser.GeneratePropertyChooser;
import com.seventh7.mybatis.ui.ListSelectionItemListener;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;


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

    ColumnsSelector.selectColumns(project, new ListSelectionItemListener<DatabaseTableFieldData>() {
      @Override
      public void apply(Collection<DatabaseTableFieldData> columns) {
        PropertyGenerator.generateProperties(columns, (GroupFour)domElement);
      }
    });
  }

}
