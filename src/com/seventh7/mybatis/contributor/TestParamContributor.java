package com.seventh7.mybatis.contributor;

import com.google.common.base.Optional;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;
import com.intellij.util.ProcessingContext;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.util.Icons;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;
import com.seventh7.mybatis.util.MybatisConstants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class TestParamContributor extends CompletionContributor {

  @SuppressWarnings("unchecked")
  public TestParamContributor() {
    extend(CompletionType.BASIC,
           XmlPatterns.psiElement().inside(XmlPatterns.xmlAttributeValue().inside(XmlPatterns.xmlAttribute().withName("test"))),
           new CompletionProvider<CompletionParameters>() {
              @Override
              protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                PsiElement position = parameters.getPosition();
                addElementForPsiParameter(position.getProject(), result, MapperUtils.findParentIdDomElement(position).orNull());
              }
    });
  }

  public static void addElementForPsiParameter(@NotNull Project project, @NotNull CompletionResultSet result, @Nullable IdDomElement element) {
    if (null == element) {
      return;
    }
    for (PsiParameter parameter : JavaUtils.findMethod(project, element).get().getParameterList().getParameters()) {
      Optional<String> valueText = JavaUtils.getAnnotationValueText(parameter, Annotation.PARAM);
      if (valueText.isPresent()) {
        LookupElementBuilder builder = LookupElementBuilder.create(valueText.get()).setIcon(Icons.PARAM_COMPLECTION_ICON);
        result.addElement(PrioritizedLookupElement.withPriority(builder, MybatisConstants.PRIORITY));
      }
    }
  }
}
