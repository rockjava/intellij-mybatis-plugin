package com.seventh7.mybatis.intention;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.seventh7.mybatis.intention.chooser.IntentionChooser;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public abstract class GenericIntention implements IntentionAction{

  protected IntentionChooser chooser;

  public GenericIntention(@NotNull IntentionChooser chooser) {
    this.chooser = chooser;
  }

  @NotNull @Override
  public String getFamilyName() {
    return getText();
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return chooser.isAvailable(project, editor, file);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

}
