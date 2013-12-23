package com.seventh7.mybatis.intention.chooser;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface IntentionChooser {

  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file);

}
