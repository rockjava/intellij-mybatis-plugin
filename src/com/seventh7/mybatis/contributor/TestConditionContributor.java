package com.seventh7.mybatis.contributor;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.XmlPatterns;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class TestConditionContributor extends CompletionContributor {

  private CompletionProvider provider = new CompletionProvider() {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  ProcessingContext context,
                                  @NotNull CompletionResultSet result) {
      System.out.println("..................");
    }
  };

  public TestConditionContributor() {
    extend(CompletionType.BASIC, XmlPatterns.psiElement().inside(XmlPatterns.xmlAttributeValue().inside(XmlPatterns.xmlAttribute().withName("test"))), provider);
  }
}