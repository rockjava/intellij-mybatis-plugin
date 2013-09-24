package com.seventh7.mybatis.provider;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.Delete;
import com.seventh7.mybatis.dom.model.GroupTwo;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.Insert;
import com.seventh7.mybatis.dom.model.Select;
import com.seventh7.mybatis.dom.model.Update;
import com.seventh7.mybatis.util.Icons;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author yanglin
 */
public class StatementLineMarkerProvider extends SimpleLineMarkerProvider<XmlTag, PsiMethod>{

  private ImmutableList<Class<? extends GroupTwo>> targetTypes = ImmutableList.of(
      Select.class,
      Update.class,
      Insert.class,
      Delete.class
  );

  @Override
  public boolean isTheElement(@NotNull PsiElement element) {
    return element instanceof XmlTag
           && MapperUtils.isElementWithinMybatisFile(element)
           && isTargetType(element);
  }

  @NotNull @Override
  public Optional<PsiMethod> apply(@NotNull XmlTag from) {
      DomElement domElement = DomUtil.getDomElement(from);
    return JavaUtils.findMethod(from.getProject(), (IdDomElement)domElement);
  }

  private boolean isTargetType(PsiElement element) {
    DomElement domElement = DomUtil.getDomElement(element);
    for (Class<?> clzz : targetTypes) {
      if(clzz.isInstance(domElement))
        return true;
    }
    return false;
  }

  @NotNull @Override
  public Navigatable getNavigatable(@NotNull XmlTag from, @NotNull PsiMethod target) {
    return (Navigatable)target.getNavigationElement();
  }

  @NotNull @Override
  public String getTooltip(@NotNull XmlTag from, @NotNull PsiMethod target) {
    return "Data access object found - " + target.getContainingClass().getQualifiedName();
  }

  @NotNull @Override
  public Icon getIcon() {
    return Icons.STATEMENT_LINE_MARKER_ICON;
  }

}
