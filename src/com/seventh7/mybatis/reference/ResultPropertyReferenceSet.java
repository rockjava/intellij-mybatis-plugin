package com.seventh7.mybatis.reference;

import com.google.common.base.Optional;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.psi.xml.XmlAttributeValue;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class ResultPropertyReferenceSet extends ReferenceSetBase<BatisImmediateReference>{

  public ResultPropertyReferenceSet(String text, @NotNull PsiElement element, int offset) {
    super(text, element, offset, DOT_SEPARATOR);
  }

  @Nullable @NonNls @Override
  protected BatisImmediateReference createReference(TextRange range, int index) {
    XmlAttributeValue element = (XmlAttributeValue)getElement();
    if (null == element) {
      return null;
    }
    Optional<? extends PsiElement> result = ReferenceSetResolverFactory.createPsiFieldResolver(element).resolve(range, index);
    return new BatisImmediateReference(element, range, result.orNull());
  }

}