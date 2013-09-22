package com.seventh7.mybatis.contributor;

import com.google.common.base.Optional;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.util.ProcessingContext;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class TestConditionContributor extends ParameterCompletionContributor{

  private CompletionProvider provider = new CompletionProvider() {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
      PsiElement position = parameters.getPosition();
      DomElement domElement = DomUtil.getDomElement(position);
      if (null != domElement) {
        IdDomElement element = DomUtil.getParentOfType(domElement, IdDomElement.class, true);
        if (null != element) {
          Optional<PsiMethod> method = JavaUtils.findMethod(position.getProject(), element);
          if (method.isPresent()) {
            setupCompletion(result, method.get());
          }
        }
      }
    }
  };

  public TestConditionContributor() {
    extend(CompletionType.BASIC, XmlPatterns.psiElement().inside(XmlPatterns.xmlAttributeValue().inside(XmlPatterns.xmlAttribute().withName("test"))), provider);
  }
}
