package com.seventh7.mybatis.util;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.dom.model.IdDomElement;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author yanglin
 */
public final class JavaUtils {

  private JavaUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean isModelClazz(@Nullable PsiClass clazz) {
    return null != clazz && !clazz.isAnnotationType() && !clazz.isInterface() && !clazz.isEnum() && clazz.isValid();
  }

  public static boolean isElementWithinInterface(@Nullable PsiElement element) {
    if (element instanceof PsiClass && ((PsiClass) element).isInterface()) {
      return true;
    }
    PsiClass type = PsiTreeUtil.getParentOfType(element, PsiClass.class);
    return type != null && type.isInterface();
  }

  public static List<PsiMethod> findAllMethodsWithoutRootParent(@NotNull PsiClass clazz) {
    Optional<PsiClass> objClazzOpt = findClazz(clazz.getProject(), "java.lang.Object");
    if (!objClazzOpt.isPresent()) {
      return Collections.emptyList();
    }
    PsiClass objClazz = objClazzOpt.get();
    PsiMethod[] methods = clazz.getAllMethods();
    ArrayList<PsiMethod> res = Lists.newArrayList();
    for (int i = 0; i < methods.length; i++) {
      if (objClazz.findMethodsByName(methods[i].getName(), false).length == 0) {
        res.add(methods[i]);
      }
    }
    return res;
  }

  @NotNull
  public static Optional<PsiClass> findClazz(@NotNull Project project, @Nullable String clazzName) {
    return clazzName == null
           ? Optional.<PsiClass>absent()
           : Optional.fromNullable(JavaPsiFacade.getInstance(project).findClass(clazzName, GlobalSearchScope.allScope(project)));
  }

  @NotNull
  public static Optional<PsiMethod> findMethod(@NotNull Project project, @Nullable String clazzName, @Nullable String methodName) {
    if (StringUtils.isBlank(clazzName) && StringUtils.isBlank(methodName)) {
      return Optional.absent();
    }
    Optional<PsiClass> clazz = findClazz(project, clazzName);
    if (clazz.isPresent()) {
      PsiMethod[] methods = clazz.get().findMethodsByName(methodName, true);
      return ArrayUtils.isEmpty(methods) ? Optional.<PsiMethod>absent() : Optional.of(methods[0]);
    }
    return Optional.absent();
  }

  @NotNull
  public static Optional<PsiMethod> findMethod(@NotNull Project project, @NotNull IdDomElement element) {
    return findMethod(project, MapperUtils.getNamespace(element), MapperUtils.getId(element));
  }

  public static boolean isAnnotationPresent(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
    PsiModifierList modifierList = target.getModifierList();
    return null != modifierList && null != modifierList.findAnnotation(annotation.getQualifiedName());
  }

  @NotNull
  public static Optional<PsiAnnotation> getPsiAnnotation(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
    PsiModifierList modifierList = target.getModifierList();
    return null == modifierList ? Optional.<PsiAnnotation>absent() : Optional.fromNullable(modifierList.findAnnotation(annotation.getQualifiedName()));
  }

  @NotNull
  public static Optional<PsiAnnotationMemberValue> getAnnotationAttributeValue(@NotNull PsiModifierListOwner target,
                                                                               @NotNull Annotation annotation,
                                                                               @NotNull String attrName) {
    if (!isAnnotationPresent(target, annotation)) {
      return Optional.absent();
    }
    Optional<PsiAnnotation> psiAnnotation = getPsiAnnotation(target, annotation);
    return psiAnnotation.isPresent() ? Optional.fromNullable(psiAnnotation.get().findAttributeValue(attrName)) : Optional.<PsiAnnotationMemberValue>absent();
  }

  @NotNull
  public static Optional<PsiAnnotationMemberValue> getAnnotationValue(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
    return getAnnotationAttributeValue(target, annotation, "value");
  }

  public static Optional<String> getAnnotationValueText(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
    Optional<PsiAnnotationMemberValue> annotationValue = getAnnotationValue(target, annotation);
    return annotationValue.isPresent() ? Optional.of(annotationValue.get().getText().replaceAll("\"", "")) : Optional.<String>absent();
  }

  public static boolean isAnyAnnotationPresent(@NotNull PsiModifierListOwner target, @NotNull Set<Annotation> annotations) {
    for (Annotation annotation : annotations) {
      if (isAnnotationPresent(target, annotation)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isAllParameterWithAnnotation(@NotNull PsiMethod method, @NotNull Annotation annotation) {
    PsiParameter[] parameters = method.getParameterList().getParameters();
    for (PsiParameter parameter : parameters) {
      if (!isAnnotationPresent(parameter, annotation)) {
        return false;
      }
    }
    return true;
  }

  public static boolean hasImportClazz(@NotNull PsiJavaFile file, @Nullable String clazzName) {
    PsiImportList importList = file.getImportList();
    if (null == importList || clazzName == null) {
      return false;
    }
    PsiImportStatement[] statements = importList.getImportStatements();
    for (PsiImportStatement tmp : statements) {
      if (null != tmp &&
          tmp.getQualifiedName() != null &&
          tmp.getQualifiedName().equals(clazzName)) {
        return true;
      }
    }
    return false;
  }

}
