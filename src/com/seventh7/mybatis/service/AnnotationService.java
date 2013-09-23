package com.seventh7.mybatis.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.seventh7.mybatis.Annotation;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class AnnotationService {

  private Project project;

  public AnnotationService(Project project) {
    this.project = project;
  }

  public static AnnotationService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, AnnotationService.class);
  }

  public void addAnnotation(@NotNull PsiModifierListOwner parameter, @NotNull Annotation annotation) {
    PsiModifierList modifierList = parameter.getModifierList();
    if (JavaUtils.isAnnotationPresent(parameter, annotation) || null == modifierList) {
      return;
    }
    JavaService javaService = JavaService.getInstance(parameter.getProject());
    javaService.importClzz((PsiJavaFile) parameter.getContainingFile(), annotation.getQualifiedName());
    
    PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
    PsiAnnotation psiAnnotation = elementFactory.createAnnotationFromText(annotation.toString(), parameter);
    modifierList.add(psiAnnotation);
    JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiAnnotation.getParent());
  }

}
