package com.seventh7.mybatis.dom.converter;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.PsiClassConverter;
import com.seventh7.mybatis.alias.AliasFacade;
import com.seventh7.mybatis.util.MybatisConstants;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

/**
 * todo fix completion 
 * @author yanglin
 */
public class AliasConverter extends FakeConverter<PsiClass> {

  private final static PsiClassConverter DELEGATE = new PsiClassConverter();

  @Nullable @Override
  public PsiClass fromString(@Nullable @NonNls String s, ConvertContext context) {
    if (null != s && !s.contains(MybatisConstants.DOT_SEPARATOR)) {
      return AliasFacade.getInstance(context.getProject()).findPsiClass(s).orNull();
    }
    return DELEGATE.fromString(s, context);
  }

  @Nullable @Override
  public String toString(@Nullable PsiClass psiClass, ConvertContext context) {
    return DELEGATE.toString(psiClass, context);
  }

}
