package com.seventh7.mybatis.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.generate.StatementGenerator;
import com.seventh7.mybatis.ui.ListSelectionListener;
import com.seventh7.mybatis.ui.UiComponentFacade;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateStatementIntention extends GenericIntention {

  @NotNull @Override
  public String getText() {
    return "[Mybatis] Generage new statement";
  }

  @Override
  public void invoke(@NotNull final Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    final PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    final StatementGenerator[] generators = StatementGenerator.getGenerators(method);
    if (1 == generators.length) {
      generators[0].execute(method);
    } else {
      UiComponentFacade.getInstance(project).showListPopup("[ Select target statement ]", new ListSelectionListener() {
        @Override
        public void selected(int index) {
          generators[index].execute(method);
        }

        @Override
        public boolean isWriteAction() {
          return true;
        }

      }, generators);
    }
  }

  @Override @NotNull
  public IntentionChooser getIntentionChooser() {
    return JavaFileIntentionChooser.GENERATE_STATEMENT_CHOOSER;
  }
}
