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
import com.seventh7.mybatis.service.AnnotationService;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateParamAnnotationIntention extends GenericJavaFileIntention {

  @NotNull @Override
  public String getText() {
    return "[Mybatis] Generate @Param";
  }

  @Override
  public boolean isAvailable(@NotNull PsiElement element) {
    PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
    PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    return (null != parameter && !JavaUtils.isAnnotationPresent(parameter, Annotation.PARAM)) ||
           (null != method && !JavaUtils.isAllParameterWithAnnotation(method, Annotation.PARAM));
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
    if (null != parameter) {
      addAnnotationWithParameterName(project, parameter);
    } else {
      PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
      PsiParameter[] parameters = method.getParameterList().getParameters();
      for (PsiParameter param : parameters) {
        addAnnotationWithParameterName(project, param);
      }
    }
  }

  private void addAnnotationWithParameterName(Project project, PsiParameter parameter) {
    AnnotationService annotationManager = AnnotationService.getInstance(project);
    annotationManager.addAnnotation(parameter, Annotation.PARAM.withValue(new Annotation.StringValue(parameter.getName())));
  }

}
