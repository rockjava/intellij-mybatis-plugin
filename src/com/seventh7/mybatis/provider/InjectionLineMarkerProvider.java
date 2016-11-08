package com.seventh7.mybatis.provider;

import com.google.common.base.Optional;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.Icons;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author yanglin
 */
@Deprecated
public class InjectionLineMarkerProvider extends RelatedItemLineMarkerProvider {

  @Override
  protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
    if (!(element instanceof PsiField))  return;
    PsiField field = (PsiField) element;
    if (!isSimpleNamedBean(field))  return;
    PsiType type = field.getType();
    if (!(type instanceof PsiClassReferenceType)) return;
    Optional<PsiClass> clazz = JavaUtils.findClazz(element.getProject(), type.getCanonicalText());
    if (!clazz.isPresent()) return;

    PsiClass psiClass = clazz.get();
    Optional<Mapper> mapper = MapperUtils.findFirstMapper(element.getProject(), psiClass);
    if (!mapper.isPresent()) return;

    NavigationGutterIconBuilder<PsiElement> builder  =
        NavigationGutterIconBuilder.create(Icons.SPRING_INJECTION_ICON)
        .setAlignment(GutterIconRenderer.Alignment.CENTER)
        .setTarget(psiClass)
        .setTooltipTitle("Data access object found - " + psiClass.getQualifiedName());
    result.add(builder.createLineMarkerInfo(field.getNameIdentifier()));
  }

  private boolean isSimpleNamedBean(PsiField field) {
    if (JavaUtils.isAnnotationPresent(field, Annotation.AUTOWIRED)) {
      return true;
    }
    Optional<PsiAnnotation> resourceAnnotation = JavaUtils.getPsiAnnotation(field, Annotation.RESOURCE);
    if (resourceAnnotation.isPresent()) {
      PsiAnnotationMemberValue nameValue = resourceAnnotation.get().findAttributeValue("name");
      String name = null;
      if (nameValue != null) {
        name = nameValue.getText().replaceAll("\"", "");
      }
      return StringUtils.isBlank(name) || name.equals(field.getName());
    }
    return false;
  }

}
