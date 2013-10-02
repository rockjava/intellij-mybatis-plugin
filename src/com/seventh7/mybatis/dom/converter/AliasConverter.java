package com.seventh7.mybatis.dom.converter;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.CustomReferenceConverter;
import com.intellij.util.xml.DomJavaUtil;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.PsiClassConverter;
import com.seventh7.mybatis.alias.AliasClassReference;
import com.seventh7.mybatis.alias.AliasFacade;
import com.seventh7.mybatis.util.MybatisConstants;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class AliasConverter extends FakeConverter<PsiClass> implements CustomReferenceConverter<PsiClass> {

  private final static PsiClassConverter DELEGATE = new PsiClassConverter();

  @Nullable @Override
  public PsiClass fromString(@Nullable @NonNls String s, ConvertContext context) {
    if (StringUtil.isEmptyOrSpaces(s)) return null;
    if (!s.contains(MybatisConstants.DOT_SEPARATOR)) {
      return AliasFacade.getInstance(context.getProject()).findPsiClass(s).orNull();
    }
    return DomJavaUtil.findClass(s.trim(), context.getFile(), context.getModule(), GlobalSearchScope.allScope(context.getProject()));
  }

  @Nullable @Override
  public String toString(@Nullable PsiClass psiClass, ConvertContext context) {
/*    Optional<AliasDesc> desc = AliasFacade.getInstance(context.getProject()).findAliasDesc(psiClass);
    return desc.isPresent() ? desc.get().getAlias() : DELEGATE.toString(psiClass, context);*/
    return DELEGATE.toString(psiClass, context);
  }

  @NotNull @Override
  public PsiReference[] createReferences(GenericDomValue<PsiClass> value, PsiElement element, ConvertContext context) {
    if (!(element instanceof XmlAttributeValue)) {
      return PsiReference.EMPTY_ARRAY;
    }
    if (((XmlAttributeValue) element).getValue().contains(MybatisConstants.DOT_SEPARATOR)) {
      return DELEGATE.createReferences(value, element, context);
    } else {
      return new PsiReference[]{new AliasClassReference((XmlAttributeValue) element)};
    }
  }
}
