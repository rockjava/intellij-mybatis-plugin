package com.seventh7.mybatis.intention.chooser;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.sql.psi.SqlFile;
import com.seventh7.mybatis.util.DomUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class ColumnIntentionChooser implements IntentionChooser {

  public static final IntentionChooser INSTANCE = new ColumnIntentionChooser();

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return file instanceof SqlFile && DomUtils.isMybatisFile(InjectedLanguageUtil.getTopLevelFile(file));
  }

}
