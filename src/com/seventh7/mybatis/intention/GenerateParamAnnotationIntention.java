package com.seventh7.mybatis.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.Annotation;
import com.seventh7.mybatis.service.AnnotationManager;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateParamAnnotationIntention extends GenericJavaFileIntention {

  @NotNull @Override
  public String getText() {
    return "Generate @Param for DAO of Mybatis";
  }

  @Override
  public boolean isAvailable(@NotNull PsiElement element) {
    PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
    PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    return (parameter != null || method != null);
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

}
