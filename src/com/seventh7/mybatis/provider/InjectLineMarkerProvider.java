package com.seventh7.mybatis.provider;

import com.google.common.base.Optional;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Function;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.util.Icons;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

/**
 * Simple Purpose, Simple Implementation
 * Hack unsuccessfully
 * @author yanglin
 */
public class InjectLineMarkerProvider extends MarkerProviderAdaptor {

  @Override
  public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
    for (PsiElement element : elements) {
      LineMarkerInfo lineMarker = getLineMarker(element);
      if (null != lineMarker) {
        result.add(lineMarker);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public LineMarkerInfo getLineMarker(@NotNull PsiElement ele) {
    if (!(ele instanceof PsiField))  return null;
    PsiField field = (PsiField) ele;
    if (!isTargetField(field))  return null;

    PsiType type = field.getType();
    if (!(type instanceof PsiClassReferenceType)) return null;

    Project project = ele.getProject();
    final PsiClass clzz = JavaPsiFacade.getInstance(project).findClass(type.getCanonicalText(), GlobalSearchScope.allScope(project));
    if (null == clzz) return null;

    if (MapperUtils.findFirstMapper(project, clzz).isPresent()) {
      return new LineMarkerInfo(field.getNameIdentifier(),
                                ele.getTextRange(),
                                Icons.AUTO_WIRED_LINE_MARKER_ICON,
                                Pass.UPDATE_ALL,
                                new Function<PsiIdentifier, String>() {
                                  @Override
                                  public String fun(PsiIdentifier o) {
                                    return "Data access object found - " + clzz.getQualifiedName();
                                  }
                                }, new GutterIconNavigationHandler<PsiIdentifier>() {
                                  @Override
                                  public void navigate(MouseEvent e, PsiIdentifier ele) {
                                    clzz.navigate(true);
                                  }
                                }, GutterIconRenderer.Alignment.CENTER
      );
    }
    return null;
  }

  private boolean isTargetField(PsiField field) {
    Optional<PsiAnnotation> wutowired = JavaUtils.getPsiAnnotation(field, Annotation.AUTOWIRED);
    if (wutowired.isPresent()) {
      return true;
    }
    Optional<PsiAnnotation> resource = JavaUtils.getPsiAnnotation(field, Annotation.RESOURCE);
    if (resource.isPresent()) {
      PsiAnnotationMemberValue nameValue = resource.get().findAttributeValue("name");
      String name = nameValue.getText().replaceAll("\"", "");
      return StringUtils.isBlank(name) || name.equals(field.getName());
    }
    return false;
  }

}
