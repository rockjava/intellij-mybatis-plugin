package com.seventh7.mybatis.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.psi.xml.XmlAttributeValue;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class ResultPropertyReferenceSet extends ReferenceSetBase<PsiReference>{

  public ResultPropertyReferenceSet(String text, @NotNull PsiElement element, int offset) {
    super(text, element, offset, DOT_SEPARATOR);
  }

  @Nullable @NonNls @Override
  protected PsiReference createReference(TextRange range, int index) {
    XmlAttributeValue element = (XmlAttributeValue)getElement();
    if (null == element) {
      return null;
    }
    return new ContextPsiFieldReference(element, range, index);
  }

}