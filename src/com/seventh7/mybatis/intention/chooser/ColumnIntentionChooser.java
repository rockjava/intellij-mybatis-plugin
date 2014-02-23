package com.seventh7.mybatis.intention.chooser;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlText;
import com.intellij.sql.psi.SqlFile;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.Sql;
import com.seventh7.mybatis.util.DomUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class ColumnIntentionChooser implements IntentionChooser {

  public static final IntentionChooser INSTANCE = new ColumnIntentionChooser();

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    boolean sqlFileCase = file instanceof SqlFile && DomUtils.isMybatisFile(InjectedLanguageUtil.getTopLevelFile(file));
    if (sqlFileCase) {
      return true;
    }

    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    XmlText text = PsiTreeUtil.getParentOfType(element, XmlText.class);
    if (text == null) {
      return false;
    }

    DomElement domElement = DomUtil.getDomElement(element);
    if (domElement == null) {
      return false;
    }
    return DomUtil.getParentOfType(domElement, Sql.class, true) != null;
  }

}
