package com.seventh7.mybatis.action;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.editorActions.CompletionAutoPopupHandler;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.sql.psi.SqlFile;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;

/**
 * Simple handle it
 * @author yanglin
 */
public class MybatisTypedHandler extends TypedHandlerDelegate {

  /**
   * TODO make the condition stronger
   */
  @Override
  public Result checkAutoPopup(char charTyped, Project project, Editor editor, PsiFile file) {
    if (charTyped == '.' && MapperUtils.isMybatisFile(file)) {
      handlePopup(file, editor);
      return Result.STOP;
    }
    return super.checkAutoPopup(charTyped, project, editor, file);
  }

  private void handlePopup(final PsiFile file, final Editor editor) {
    CompletionAutoPopupHandler.runLaterWithCommitted(file.getProject(), editor.getDocument(), new Runnable() {
      @Override
      public void run() {
        CompletionAutoPopupHandler.invokeCompletion(CompletionType.BASIC, true, file.getProject(), editor, 0, false);
      }
    });
  }

  @Override
  public Result charTyped(char c, Project project, Editor editor, @NotNull PsiFile file) {
    PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(file);
    if (file instanceof SqlFile && MapperUtils.isMybatisFile(topLevelFile)) {
      CaretModel caretModel = editor.getCaretModel();
      int offset = caretModel.getOffset();
      if (editor.getDocument().getText().charAt(offset - 2) == '#') {
        editor.getDocument().insertString(offset, "}");
        caretModel.moveToOffset(offset);
      }
      return Result.STOP;
    }
    return super.charTyped(c, project, editor, file);
  }
}