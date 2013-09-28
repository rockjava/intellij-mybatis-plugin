package com.seventh7.mybatis.contributor;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.injected.editor.DocumentWindow;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.util.MapperUtils;


/**
 * @author yanglin
 */
public class SqlParamCompletionContributor extends CompletionContributor {

  @Override
  public void fillCompletionVariants(CompletionParameters parameters, final CompletionResultSet result) {
    PsiElement position = parameters.getPosition();
    PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(position);
    if (MapperUtils.isMybatisFile(topLevelFile)) {
      if (shouldAddElement(position.getContainingFile(), parameters.getOffset())) {
        process(topLevelFile, result, position);
      }
    }
  }

  private void process(PsiFile xmlFile, CompletionResultSet result, PsiElement position) {
    DocumentWindow documentWindow = InjectedLanguageUtil.getDocumentWindow(position);
    if (null != documentWindow) {
      int offset = documentWindow.injectedToHost(position.getTextOffset());
      PsiElement elementAt = xmlFile.findElementAt(offset);
      XmlTag xmlTag = PsiTreeUtil.getParentOfType(elementAt, XmlTag.class);
      if (null != xmlTag) {
        DomElement domElement = DomUtil.getDomElement(xmlTag);
        if (domElement instanceof IdDomElement) {
          TestConditionContributor.addElementForPsiParameter(position.getProject(), result, (IdDomElement) domElement);
        }
      }
    }
  }

  private boolean shouldAddElement(PsiFile file, int offset) {
    String text = file.getText();
    for (int i = offset - 1; i > 0; i--) {
      char c = text.charAt(i);
      if (c == '{' && text.charAt(i - 1) == '#') return true;
      if (!Character.isLetterOrDigit(c) && c != ' ') return false;
    }
    return false;
  }
}