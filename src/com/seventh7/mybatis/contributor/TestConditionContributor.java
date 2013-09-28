package com.seventh7.mybatis.contributor;

import com.google.common.base.Optional;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.util.ProcessingContext;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.util.Icons;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MybatisConstants;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class TestConditionContributor extends CompletionContributor {

  @SuppressWarnings("unchecked")
  public TestConditionContributor() {
    extend(CompletionType.BASIC,
           XmlPatterns.psiElement().inside(XmlPatterns.xmlAttributeValue().inside(XmlPatterns.xmlAttribute().withName("test"))),
           new CompletionProvider<CompletionParameters>() {
              @Override
              protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                PsiElement position = parameters.getPosition();
                DomElement domElement = DomUtil.getDomElement(position);
                if (null != domElement) {
                  IdDomElement element = DomUtil.getParentOfType(domElement, IdDomElement.class, true);
                  if (null != element) {
                    Optional<PsiMethod> method = JavaUtils.findMethod(position.getProject(), element);
                    if (method.isPresent()) {
                      addElementForPsiParameter(result, method.get());
                    }
                  }
                }
             }
    });
  }

  public static void addElementForPsiParameter(@NotNull CompletionResultSet result, @NotNull PsiMethod method) {
    for (PsiParameter parameter : method.getParameterList().getParameters()) {
      Optional<PsiAnnotation> psiAnnotation = JavaUtils.getPsiAnnotation(parameter, Annotation.PARAM);
      if (psiAnnotation.isPresent()) {
        PsiAnnotationMemberValue value = psiAnnotation.get().findDeclaredAttributeValue("value");
        if (null != value) {
          LookupElementBuilder builder = LookupElementBuilder.create(value.getText().replaceAll("\"", ""))
              .setIcon(Icons.PARAM_COMPLECTION_ICON);
          result.addElement(PrioritizedLookupElement.withPriority(builder, MybatisConstants.PRIORITY));
        }
      }
    }
  }
}
