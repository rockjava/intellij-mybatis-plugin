package com.seventh7.mybatis.contributor;

import com.google.common.base.Optional;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.injected.editor.DocumentWindow;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.util.DomUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;


/**
 * @author yanglin
 */
public class SqlParamCompletionContributor extends CompletionContributor {

  @Override
  public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull final CompletionResultSet result) {
    if (parameters.getCompletionType() != CompletionType.BASIC) {
      return;
    }

    PsiElement position = parameters.getPosition();
    PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(position);
    if (DomUtils.isMybatisFile(topLevelFile)) {
      if (shouldAddElement(position.getContainingFile(), parameters.getOffset())) {
        process(topLevelFile, result, position);
      }
    }
  }

  private void process(PsiFile xmlFile, CompletionResultSet result, PsiElement position) {
    DocumentWindow documentWindow = InjectedLanguageUtil.getDocumentWindow(position);
    if (null != documentWindow) {
      int offset = documentWindow.injectedToHost(position.getTextOffset());
      Optional<IdDomElement> idDomElement = MapperUtils.findParentIdDomElement(xmlFile.findElementAt(offset));
      if (idDomElement.isPresent()) {
        TestParamContributor.addElementForPsiParameter(position.getProject(), result, idDomElement.get());
        result.stopHere();
      }
    }
  }

  private boolean shouldAddElement(PsiFile file, int offset) {
    String text = file.getText();
    for (int i = offset - 1; i > 0; i--) {
      char c = text.charAt(i);
      if (c == '{' && (text.charAt(i - 1) == '#' || text.charAt(i - 1) == '$')) {
        return true;
      }
    }
    return false;
  }
}