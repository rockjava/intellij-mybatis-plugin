package com.seventh7.mybatis.dom.converter;

import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.CustomReferenceConverter;
import com.intellij.util.xml.GenericDomValue;
import com.seventh7.mybatis.reference.ResultPropertyReferenceSet;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class PropertyConverter extends FakeConverter<XmlAttributeValue> implements CustomReferenceConverter<XmlAttributeValue> {

  @NotNull @Override
  public PsiReference[] createReferences(GenericDomValue<XmlAttributeValue> value, PsiElement element, ConvertContext context) {
    final String s = value.getStringValue();
    if (s == null) {
      return PsiReference.EMPTY_ARRAY;
    }
    return new ResultPropertyReferenceSet(s, element, ElementManipulators.getOffsetInElement(element)).getPsiReferences();
  }

}
