package com.seventh7.mybatis.action;

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.editorActions.CompletionAutoPopupHandler;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.seventh7.mybatis.util.DomUtils;

/**
 * @author yanglin
 */
public class MybatisTypedHandler extends TypedHandlerDelegate {

  @Override
  public Result checkAutoPopup(char charTyped, final Project project, final Editor editor, PsiFile file) {
    if (charTyped == '.' && DomUtils.isMybatisFile(file)) {
      CompletionAutoPopupHandler.runLaterWithCommitted(project, editor.getDocument(), new Runnable() {
        @Override
        public void run() {
          new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(project, editor, 1);
        }
      });
      return Result.STOP;
    }
    return super.checkAutoPopup(charTyped, project, editor, file);
  }

}