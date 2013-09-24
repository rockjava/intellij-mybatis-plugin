package com.seventh7.mybatis.contributor;

import com.google.common.base.Optional;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.util.PlatformIcons;
import com.seventh7.mybatis.Annotation;
import com.seventh7.mybatis.util.Icons;
import com.seventh7.mybatis.util.JavaUtils;

/**
 * @author yanglin
 */
public abstract class ParameterCompletionContributor extends CompletionContributor {

  protected void setupCompletion(CompletionResultSet result, PsiMethod method) {
    for (PsiParameter parameter : method.getParameterList().getParameters()) {
      Optional<PsiAnnotation> psiAnnotation = JavaUtils.getPsiAnnotation(parameter, Annotation.PARAM);
      if (psiAnnotation.isPresent()) {
        PsiAnnotationMemberValue value = psiAnnotation.get().findDeclaredAttributeValue("value");
        if (null != value) {
          LookupElementBuilder builder = LookupElementBuilder.create(value.getText().replaceAll("\"", ""))
              .setIcon(Icons.PARAM_COMPLECTION_ICON);
          result.addElement(PrioritizedLookupElement.withPriority(builder, 5000.0));
        }
      }
    }
  }

}