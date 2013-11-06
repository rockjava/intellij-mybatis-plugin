package com.seventh7.mybatis.provider;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.CommonProcessors;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.Icons;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author yanglin
 */
public class MapperLineMarkerProvider extends RelatedItemLineMarkerProvider {

  private static final Function<IdDomElement, XmlTag> FUN = new Function<IdDomElement, XmlTag>() {
    @Override
    public XmlTag apply(IdDomElement idDomElement) {
      return idDomElement.getXmlTag();
    }
  };

  @Override
  protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
    if (element instanceof PsiMethod && JavaUtils.isElementWithinInterface(element)) {
      CommonProcessors.CollectProcessor<IdDomElement> processor = new CommonProcessors.CollectProcessor<IdDomElement>();
      JavaService.getInstance(element.getProject()).process(element, processor);
      Collection<IdDomElement> results = processor.getResults();
      if (!results.isEmpty()) {
        NavigationGutterIconBuilder<PsiElement> builder  =
            NavigationGutterIconBuilder.create(Icons.MAPPER_LINE_MARKER_ICON)
                .setAlignment(GutterIconRenderer.Alignment.CENTER)
                .setTargets(Collections2.transform(results, FUN))
                .setTooltipTitle("Navigation to statement in mapper xml");
        result.add(builder.createLineMarkerInfo(((PsiMethod) element).getNameIdentifier()));
      }
    }
  }

}
