package com.seventh7.mybatis.util;

import com.google.common.base.Optional;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTypeParameterListOwner;
import com.intellij.psi.search.GlobalSearchScope;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public final class JavaUtils {

  private JavaUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean isWithinInterface(@NotNull PsiTypeParameterListOwner owner) {
    if (owner instanceof PsiClass)
      return ((PsiClass)owner).isInterface();

    PsiClass clzz = owner.getContainingClass();
    return null != clzz && clzz.isInterface();
  }

  public static boolean isWithinInterface(@NotNull PsiElement element) {
    return element instanceof PsiTypeParameterListOwner && isWithinInterface((PsiTypeParameterListOwner)element);
  }

  public static Optional<PsiMethod> findMethod(@NotNull Project project, @Nullable String clzzName, @Nullable String methodName) {
    if (StringUtils.isBlank(clzzName) && StringUtils.isBlank(methodName)) {
      return Optional.absent();
    }
    PsiClass clzz = JavaPsiFacade.getInstance(project).findClass(clzzName, GlobalSearchScope.allScope(project));
    if (null != clzz) {
      PsiMethod[] methods = clzz.findMethodsByName(methodName, true);
      return ArrayUtils.isEmpty(methods) ? Optional.<PsiMethod>absent() : Optional.of(methods[0]);
    }
    return Optional.absent();
  }

}
