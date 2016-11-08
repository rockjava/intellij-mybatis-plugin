package com.seventh7.mybatis.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.intention.chooser.GenerateParamChooser;
import com.seventh7.mybatis.service.AnnotationService;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateParamAnnotationIntention extends GenericIntention {

  public GenerateParamAnnotationIntention() {
    super(GenerateParamChooser.INSTANCE);
  }

  @NotNull @Override
  public String getText() {
    return "[Mybatis] Generate @Param";
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
    AnnotationService annotationService = AnnotationService.getInstance(project);
    if (null != parameter) {
      annotationService.addAnnotationWithParameterName(parameter);
    } else {
      PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
      if (null != method) {
        annotationService.addAnnotationWithParameterNameForMethodParameters(method);
      }
    }
  }

}
