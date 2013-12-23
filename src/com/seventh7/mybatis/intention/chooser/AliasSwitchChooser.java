package com.seventh7.mybatis.intention.chooser;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReference;
import com.seventh7.mybatis.alias.AliasClassReference;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class AliasSwitchChooser implements IntentionChooser{

  public static final AliasSwitchChooser INSTANCE = new AliasSwitchChooser();

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    PsiReference reference = file.findReferenceAt(editor.getCaretModel().getOffset());
    return reference instanceof JavaClassReference || reference instanceof AliasClassReference;
  }

}
