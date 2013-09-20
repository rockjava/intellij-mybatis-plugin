package com.seventh7.mybatis.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.seventh7.mybatis.Annotation;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class AnnotationManager {

  private Project project;

  public AnnotationManager(Project project) {
    this.project = project;
  }

  public static final AnnotationManager getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, AnnotationManager.class);
  }

  public void addAnnotation(@NotNull PsiElement target, @NotNull Annotation annotation) {
    if (target instanceof PsiParameter) {
      addAnnotation((PsiParameter)target, annotation);
    } else if (target instanceof PsiMethod) {
      addAnnotation((PsiMethod)target, annotation);
    }
  }

  private void addAnnotation(@NotNull PsiMethod method, @NotNull Annotation annotation) {
    PsiParameter[] parameters = method.getParameterList().getParameters();
    for (PsiParameter psiParameter : parameters) {
      addAnnotation(psiParameter, annotation);
    }
  }

  private void addAnnotation(@NotNull PsiParameter parameter, @NotNull Annotation annotation) {
    if (JavaUtils.isAnnotationPresent(parameter, annotation)) {
      return;
    }
    JavaService javaService = JavaService.getInstance(parameter.getProject());
    javaService.importClzz((PsiJavaFile) parameter.getContainingFile(), annotation.getQualifiedName());
    
    PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
    PsiAnnotation psiAnnotation = elementFactory.createAnnotationFromText(annotation.toString(), parameter);
    parameter.getModifierList().add(psiAnnotation);
    JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiAnnotation.getParent());
  }

}
