package com.seventh7.mybatis.intention;

import com.google.common.base.Optional;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.Annotation;
import com.seventh7.mybatis.generate.GeneratorDialog;
import com.seventh7.mybatis.generate.StatementGenerator;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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
  public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    StatementGenerator[] generators = StatementGenerator.getGenerators(method);
    if (1 == generators.length) {
      generators[0].execute(method);
    } else {
      GeneratorDialog dialog = new GeneratorDialog(project, Arrays.asList(generators));
      dialog.show();
      if (dialog.isOK()) {
        Optional<StatementGenerator> generatorOptional = dialog.getSelected();
        if (generatorOptional.isPresent()) {
          generatorOptional.get().execute(method);
        }
      }
    }
  }

}
