package com.seventh7.mybatis.intention;

import com.google.common.base.Optional;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlText;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.ResultMap;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GeneratePropertyChooser implements IntentionChooser {

  public static final GeneratePropertyChooser INSTANCE = new GeneratePropertyChooser();

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    XmlText text = PsiTreeUtil.getParentOfType(element, XmlText.class);
    if (text == null) { return false; }

    Optional<IdDomElement> idDomElement = MapperUtils.findParentIdDomElement(element);
    return idDomElement.isPresent() && idDomElement.get() instanceof ResultMap;
  }

}
