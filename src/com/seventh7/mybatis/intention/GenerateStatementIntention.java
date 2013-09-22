package com.seventh7.mybatis.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBList;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.Annotation;
import com.seventh7.mybatis.generate.StatementGenerator;
import com.seventh7.mybatis.service.EditorService;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author yanglin
 */
public class GenerateStatementIntention extends GenericJavaFileIntention {

  @NotNull @Override
  public String getText() {
    return "[Mybatis] Generage new statement";
  }

  @Override
  public boolean isAvailable(@NotNull PsiElement element) {
    PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
    if (null != parameter) {
      return false;
    }
    PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    if (null == method) {
      return false;
    }
    JavaService javaService = JavaService.getInstance(element.getProject());
    return !JavaUtils.isAnyAnnotationPresent(method, Annotation.STATEMENT_SYMMETRIES)
           && !javaService.findWithFindFristProcessor(method).isPresent();
  }

  @Override
  public void invoke(@NotNull final Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    final PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    final StatementGenerator[] generators = StatementGenerator.getGenerators(method);
    if (1 == generators.length) {
      generators[0].execute(method);
    } else {
      final JList list = new JBList(generators);
      JBPopup popup = EditorService.getInstance(project).createSimpleListPopupChooser("[ Select target statement ]", list, new Runnable() {
        @Override
        public void run() {
          generators[list.getSelectedIndex()].execute(method);
        }
      });
      popup.showInBestPositionFor(editor);
    }
  }

}
