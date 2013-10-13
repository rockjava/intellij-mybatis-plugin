package com.seventh7.mybatis.provider;

import com.google.common.base.Optional;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.util.Function;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.Icons;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

/**
 * Simple Purpose, Simple Implementation
 * @author yanglin
 */
public class AutowiredLineMarkerProvider extends GenericLineMarkerProvider {

  @Nullable @Override
  public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
    if (element instanceof PsiField ) {
      PsiField field = (PsiField) element;
      if (JavaUtils.isAnyAnnotationPresent(field, Annotation.SPRING_INJECT)) {
        PsiType type =  field.getType();
        if (type instanceof PsiClassReferenceType) {
          final PsiClass clzz = ((PsiClassReferenceType) type).resolve();
          Optional<Object> res = JavaService.getInstance(element.getProject()).findWithFindFristProcessor(clzz);
          if (res.isPresent()) {
            return new LineMarkerInfo( field.getNameIdentifier(),
                                       element.getTextRange(),
                                       Icons.AUTO_WIRED_LINE_MARKER_ICON,
                                       Pass.UPDATE_ALL,
                                       new Function<PsiIdentifier, String>() {
                                         @Override
                                         public String fun(PsiIdentifier o) {
                                           return "Data access object found - " + clzz.getQualifiedName();
                                         }
                                       },
                                       new GutterIconNavigationHandler<PsiIdentifier>() {
                                         @Override
                                         public void navigate(MouseEvent e, PsiIdentifier ele) {
                                           clzz.navigate(true);
                                         }
                                       },
                                       GutterIconRenderer.Alignment.CENTER);
          }
        }
      }
    }
    return null;
  }

}
