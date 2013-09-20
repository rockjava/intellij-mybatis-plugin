package com.seventh7.mybatis;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateParamAnnotationIntention implements IntentionAction {

  @NotNull @Override
  public String getText() {
    return "Generate @Param for DAO of Mybatis";
  }

  @NotNull @Override
  public String getFamilyName() {
    return getText();
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    if (!(file instanceof PsiJavaFile))
      return false;
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    if (!JavaUtils.isElementWithinInterface(element)) {
      return false;
    }
    PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
    if (null != parameter) {
      return true;
    }
    PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    if (null != method) {
          return true;
    }
    return false;
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
    if (null != parameter) {
      addAnnotationWithParameterValue(project, parameter);
    } else {
      PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
      PsiParameter[] parameters = method.getParameterList().getParameters();
      for (PsiParameter param : parameters) {
        addAnnotationWithParameterValue(project, param);
      }
    }
  }

  private void addAnnotationWithParameterValue(Project project, PsiParameter parameter) {
    AnnotationManager annotationManager = AnnotationManager.getInstance(project);
    annotationManager.addAnnotation(parameter, Annotation.PARAM.withValue(new Annotation.StringValue(parameter.getName())));
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

}
