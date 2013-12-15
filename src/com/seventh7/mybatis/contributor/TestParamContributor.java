package com.seventh7.mybatis.contributor;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.util.ProcessingContext;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.util.CollectionUtils;
import com.seventh7.mybatis.util.Icons;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;
import com.seventh7.mybatis.util.MybatisConstants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author yanglin
 */
public class TestParamContributor extends CompletionContributor {

  private final static Collection<? extends SimpleParameterLookupHandler> HANDLER_CHAIN = ImmutableSet.of(
      new AnnotationParameterLookupHandler(),
      new ModelParameterLookupHandler(),
      new PrimitiveParameterLookupHandler()
  );

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
    Optional<PsiMethod> method = JavaUtils.findMethod(project, element);
    if (!method.isPresent()) {
      return;
    }
    for (PsiParameter parameter : method.get().getParameterList().getParameters()) {
      for (SimpleParameterLookupHandler handler : HANDLER_CHAIN) {
        handler.handle(result, parameter);
      }
    }
  }

  private abstract static class SimpleParameterLookupHandler {

    public void handle(@NotNull CompletionResultSet result, @NotNull PsiParameter parameter) {
      Collection<String> parameterStrings = getParameterString(parameter);
      if (CollectionUtils.isEmpty(parameterStrings)) {
        return;
      }
      for (String parameterString : parameterStrings) {
        LookupElementBuilder builder = LookupElementBuilder.create(parameterString).withIcon(Icons.PARAM_COMPLETION_ICON);
        result.addElement(PrioritizedLookupElement.withPriority(builder, MybatisConstants.PRIORITY));
      }
    }

    @NotNull
    public abstract Collection<String> getParameterString(@NotNull PsiParameter parameter);
  }

  private final static class AnnotationParameterLookupHandler extends SimpleParameterLookupHandler {

    @NotNull
    @Override
    public Collection<String> getParameterString(@NotNull PsiParameter parameter) {
      Optional<String> valueText = JavaUtils.getAnnotationValueText(parameter, Annotation.PARAM);
      return valueText.isPresent() ? Lists.newArrayList(valueText.get()) : Collections.<String>emptyList();
    }

  }

  private abstract static class NoParamAnnotationPresentParameterLookupHandler extends SimpleParameterLookupHandler {

    @NotNull
    @Override
    public Collection<String> getParameterString(@NotNull PsiParameter parameter) {
      Optional<String> valueText = JavaUtils.getAnnotationValueText(parameter, Annotation.PARAM);
      return valueText.isPresent() ? Collections.<String>emptyList() : doHandle(parameter);
    }

    @NotNull
    public abstract Collection<String> doHandle(@NotNull PsiParameter parameter);
  }

  private final static class PrimitiveParameterLookupHandler extends NoParamAnnotationPresentParameterLookupHandler {

    @NotNull
    @Override
    public Collection<String> doHandle(@NotNull PsiParameter parameter) {
      return Lists.newArrayList(parameter.getName());
    }
  }

  private final static class ModelParameterLookupHandler extends NoParamAnnotationPresentParameterLookupHandler {

    @NotNull
    @Override
    public Collection<String> doHandle(@NotNull PsiParameter parameter) {
      PsiType type = parameter.getType();
      if (!(type instanceof PsiClassReferenceType)) { return Collections.emptyList(); }

      PsiClass clazz = ((PsiClassReferenceType) type).resolve();
      if (null == clazz) { return Collections.emptyList(); }

      HashSet<String> res = Sets.newHashSet();
      for (PsiField field : JavaUtils.findSettablePsiFields(clazz)) {
        res.add(field.getName());
      }

      return res;
    }

  }

}
