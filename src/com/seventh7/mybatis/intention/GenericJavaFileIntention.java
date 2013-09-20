package com.seventh7.mybatis.intention;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public abstract class GenericJavaFileIntention implements IntentionAction{

  @NotNull @Override
  public String getFamilyName() {
    return getText();
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    if (!(file instanceof PsiJavaFile))
      return false;
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    return null != element && JavaUtils.isElementWithinInterface(element) && isAvailable(element);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  public abstract boolean isAvailable(@NotNull PsiElement element);
}
