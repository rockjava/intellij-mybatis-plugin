package com.seventh7.mybatis.provider;

import com.google.common.base.Optional;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTypeParameterListOwner;
import com.intellij.util.CommonProcessors;
import com.intellij.util.xml.DomElement;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author yanglin
 */
public class MapperLineMarkerProvider extends SimpleLineMarkerProvider<PsiTypeParameterListOwner, DomElement> {

  private final Icon icon = IconLoader.getIcon("/gutter/implementedMethod.png");

  @Override
  public boolean isTheElement(@NotNull PsiElement element) {
    return JavaUtils.isWithinInterface(element);
  }

  @NotNull @Override
  public Optional<DomElement> apply(@NotNull PsiTypeParameterListOwner from) {
    CommonProcessors.FindFirstProcessor<DomElement> processor = new CommonProcessors.FindFirstProcessor<DomElement>();
    JavaService javaService = ServiceManager.getService(from.getProject(), JavaService.class);
    javaService.process(from, processor);
    return processor.isFound() ? Optional.of(processor.getFoundValue()) : Optional.<DomElement>absent();
  }

  @NotNull @Override
  public Navigatable getNavigatable(@NotNull PsiTypeParameterListOwner from, @NotNull DomElement target) {
    return (Navigatable)target.getXmlElement();
  }

  @NotNull @Override
  public String getTooltip(@NotNull PsiTypeParameterListOwner from, @NotNull DomElement target) {
    return "Statement found in - " + target.getXmlElement().getContainingFile().getName();
  }

  @NotNull @Override
  public Icon getIcon() {
    return icon;
  }

}
