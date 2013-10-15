package com.seventh7.mybatis.action;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.editorActions.CompletionAutoPopupHandler;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.sql.psi.SqlFile;
import com.seventh7.mybatis.util.DomUtils;

import org.jetbrains.annotations.NotNull;

/**
 * Simple handling
 * @author yanglin
 */
public class MybatisTypedHandler extends TypedHandlerDelegate {

  /**
   * TODO[yanglin] make the inspection stronger
   */
  @Override
  public Result checkAutoPopup(char charTyped, Project project, Editor editor, PsiFile file) {
    if (charTyped == '.' && DomUtils.isMybatisFile(file)) {
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
    if (c == '{' && file instanceof SqlFile && DomUtils.isMybatisFile(topLevelFile)) {
      CaretModel caretModel = editor.getCaretModel();
      int offset = caretModel.getOffset();
      Document document = editor.getDocument();
      if (document.getText().charAt(offset - 2) == '#') {
        document.insertString(offset, "}");
        caretModel.moveToOffset(offset);
        return Result.STOP;
      }
    }
    return super.charTyped(c, project, editor, file);
  }
}