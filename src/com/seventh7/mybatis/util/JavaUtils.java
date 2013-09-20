package com.seventh7.mybatis.util;

import com.google.common.base.Optional;

import com.intellij.formatting.FormatTextRanges;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.impl.source.codeStyle.CodeFormatterFacade;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.seventh7.mybatis.Annotation;

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

  public static boolean isElementWithinInterface(@Nullable PsiElement element) {
    PsiClass type = PsiTreeUtil.getParentOfType(element, PsiClass.class);
    return Optional.fromNullable(type).isPresent() && type.isInterface();
  }

  @NotNull
  public static Optional<PsiClass> findClzz(@NotNull Project project, @NotNull String clzzName) {
    return Optional.fromNullable(JavaPsiFacade.getInstance(project).findClass(clzzName, GlobalSearchScope.allScope(project)));
  }

  public static Optional<PsiMethod> findMethod(@NotNull Project project, @Nullable String clzzName, @Nullable String methodName) {
    if (StringUtils.isBlank(clzzName) && StringUtils.isBlank(methodName)) {
      return Optional.absent();
    }
    Optional<PsiClass> clzz = findClzz(project, clzzName);
    if (clzz.isPresent()) {
      PsiMethod[] methods = clzz.get().findMethodsByName(methodName, true);
      return ArrayUtils.isEmpty(methods) ? Optional.<PsiMethod>absent() : Optional.of(methods[0]);
    }
    return Optional.absent();
  }

  public static boolean isAnnotationPresent(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
    PsiAnnotation[] annotations = target.getModifierList().getAnnotations();
    for (PsiAnnotation ann : annotations) {
      if (ann.getQualifiedName().equals(annotation.getQualifiedName())) {
        return true;
      }
    }
    return false;
  }

  public static boolean hasImportClzz(PsiJavaFile file, String clzzName) {
    PsiImportStatement[] statements = file.getImportList().getImportStatements();

    for (PsiImportStatement tmp : statements) {
      if (tmp.getQualifiedName().equals(clzzName)) {
        return true;
      }
    }
    return false;
  }

  public static void importClzz(Project project, PsiJavaFile file, String clzzName) {
    if (hasImportClzz(file, clzzName)) {
      return;
    }
    Optional<PsiClass> clzz = findClzz(project, clzzName);
    if (!clzz.isPresent()) {
      return;
    }
    PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
    PsiImportStatement statement = elementFactory.createImportStatement(clzz.get());
    file.getImportList().add(statement);

    CodeFormatterFacade formatter = new CodeFormatterFacade(new CodeStyleSettings());
    formatter.processText(file, new FormatTextRanges(statement.getTextRange(), true), true);
  }

}
