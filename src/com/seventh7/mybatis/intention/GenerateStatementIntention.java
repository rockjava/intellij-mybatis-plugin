package com.seventh7.mybatis.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateStatementIntention extends GenericJavaFileIntention {

  @NotNull @Override
  public String getText() {
    return "Generage new statement for dao method of Mybatis";
  }

  @Override
  public boolean isAvailable(@NotNull PsiElement element) {
    return false;
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
  }

}
