package com.seventh7.mybatis.provider;

import com.google.common.base.Optional;

import com.intellij.openapi.util.IconLoader;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTypeElement;
import com.intellij.util.xml.DomElement;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author yanglin
 */
public class MapperLineMarkerProvider extends SimpleLineMarkerProvider<PsiTypeElement, DomElement> {

  private final Icon icon = IconLoader.getIcon("/gutter/implementedMethod.png");

  @Override
  public boolean isTheElement(@NotNull PsiElement element) {
    return element instanceof PsiTypeElement
           && element.getParent() instanceof PsiMethod
           && JavaUtils.isElementWithinInterface(element);
  }

  @NotNull @Override
  public Optional<DomElement> apply(@NotNull PsiTypeElement from) {
    return JavaService.getInstance(from.getProject()).findWithFindFristProcessor(from.getParent());
  }

  @NotNull @Override
  public Navigatable getNavigatable(@NotNull PsiTypeElement from, @NotNull DomElement target) {
    return (Navigatable)target.getXmlElement();
  }

  @NotNull @Override
  public String getTooltip(@NotNull PsiTypeElement from, @NotNull DomElement target) {
    return "Statement found in - " + target.getXmlElement().getContainingFile().getName();
  }

  @NotNull @Override
  public Icon getIcon() {
    return icon;
  }

}
