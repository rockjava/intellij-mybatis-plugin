package com.seventh7.mybatis.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.seventh7.mybatis.Annotation;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public abstract class JavaFileIntentionChooser implements IntentionChooser {

  public static final IntentionChooser GENERATE_STATEMENT_CHOOSER = new GenerateStatementChooser();

  public static final IntentionChooser GENERATE_MAPPER_CHOOSER = new GenerateMapperChooser();

  public static final IntentionChooser GENERATE_PARAM_CHOOSER = new GenerateParamChooser();

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    if (!(file instanceof PsiJavaFile))
      return false;
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    return null != element && JavaUtils.isElementWithinInterface(element) && isAvailable(element);
  }

  public abstract boolean isAvailable(@NotNull PsiElement element);

  public boolean isPositionOfParameterDeclaration(@Nullable PsiElement element) {
    return element.getParent() instanceof PsiParameter;
  }

  public boolean isPositionOfMethodDeclaration(@Nullable PsiElement element) {
    return element.getParent() instanceof PsiMethod;
  }

  public boolean isPositionOfInterfaceDeclaration(@Nullable PsiElement element) {
    return element.getParent() instanceof PsiClass;
  }

  public boolean isTargetPresentInXml(PsiElement element) {
    return JavaService.getInstance(element.getProject()).findWithFindFristProcessor(element).isPresent();
  }

  static class GenerateStatementChooser extends JavaFileIntentionChooser {

    @Override
    public boolean isAvailable(@NotNull PsiElement element) {
      if (!isPositionOfMethodDeclaration(element)) {
        return false;
      }
      PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
      PsiClass clzz = PsiTreeUtil.getParentOfType(element, PsiClass.class);
      return !JavaUtils.isAnyAnnotationPresent(method, Annotation.STATEMENT_SYMMETRIES) &&
             !isTargetPresentInXml(method) &&
             isTargetPresentInXml(clzz);
    }

  }

  static class GenerateMapperChooser extends JavaFileIntentionChooser {

    @Override
    public boolean isAvailable(@NotNull PsiElement element) {
      if (!isPositionOfInterfaceDeclaration(element)) {
        return false;
      }
      PsiClass clzz = PsiTreeUtil.getParentOfType(element, PsiClass.class);
      return JavaUtils.isElementWithinInterface(element) && !isTargetPresentInXml(clzz);
    }
  }

  static class GenerateParamChooser extends JavaFileIntentionChooser {

    @Override
    public boolean isAvailable(@NotNull PsiElement element) {
      PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
      PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
      return (null != parameter && !JavaUtils.isAnnotationPresent(parameter, Annotation.PARAM)) ||
             (null != method && !JavaUtils.isAllParameterWithAnnotation(method, Annotation.PARAM));
    }
  }
}
