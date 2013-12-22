package com.seventh7.mybatis.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

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

  }

}
