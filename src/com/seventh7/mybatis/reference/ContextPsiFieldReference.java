package com.seventh7.mybatis.reference;

import com.google.common.base.Optional;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttributeValue;
import com.seventh7.mybatis.dom.MapperBacktrackingFacade;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MybatisConstants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class ContextPsiFieldReference extends PsiReferenceBase<XmlAttributeValue>{

  private ContextReferenceSetResolver resolver;

  private int index;

  public ContextPsiFieldReference(XmlAttributeValue element, TextRange range, int index) {
    super(element, range, false);
    resolver = ReferenceSetResolverFactory.createPsiFieldResolver(element);
    this.index = index;
  }

  @Nullable @Override
  public PsiElement resolve() {
    Optional<PsiElement> resolved = resolver.resolve(index);
    return resolved.orNull();
  }

  @NotNull @Override
  public Object[] getVariants() {
    Optional<PsiClass> clzz = getTargetClzz();
    return clzz.isPresent() ? JavaUtils.findSettablePsiFields(myElement.getProject(), clzz.get()) : PsiReference.EMPTY_ARRAY;
  }

  private Optional<PsiClass> getTargetClzz() {
    if (getElement().getValue().contains(MybatisConstants.DOT_SEPARATOR)) {
      int ind = 0 == index ? 0 : index - 1;
      Optional<PsiElement> resolved = resolver.resolve(ind);
      if (resolved.isPresent()) {
        return JavaService.getInstance(myElement.getProject()).getReferenceClzzOfPsiField(resolved.get());
      }
    } else {
      return MapperBacktrackingFacade.getResultPropertyClzz(myElement);
    }
    return Optional.absent();
  }

}
