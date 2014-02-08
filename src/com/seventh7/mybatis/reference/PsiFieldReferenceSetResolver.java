package com.seventh7.mybatis.reference;

import com.google.common.base.Optional;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.xml.XmlAttributeValue;
import com.seventh7.mybatis.dom.MapperBacktrackingUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class PsiFieldReferenceSetResolver extends ContextReferenceSetResolver<XmlAttributeValue, PsiField> {

  protected PsiFieldReferenceSetResolver(XmlAttributeValue from) {
    super(from);
  }

  @NotNull @Override
  public String getText() {
    return getElement().getValue();
  }

  @NotNull @Override
  public Optional<PsiField> resolve(@NotNull PsiField current, @NotNull String text) {
    PsiType type = current.getType();
    if (type instanceof PsiClassReferenceType && !((PsiClassReferenceType) type).hasParameters()) {
      PsiClass clazz = ((PsiClassReferenceType) type).resolve();
      return findPropertyField(clazz, text);
    }
    return Optional.absent();
  }

  @NotNull @Override
  public Optional<PsiField> getStartElement(@Nullable String firstText) {
    Optional<PsiClass> clazz = MapperBacktrackingUtils.getPropertyClazz(getElement());
    return findPropertyField(clazz.orNull(), firstText);
  }

  private Optional<PsiField> findPropertyField(@Nullable PsiClass psiClass, @Nullable String propertyName) {
    return psiClass != null && propertyName != null
           ? Optional.fromNullable(psiClass.findFieldByName(propertyName, true))
           : Optional.<PsiField>absent();
  }
}