package com.seventh7.mybatis.provider;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.CommonProcessors;
import com.intellij.util.xml.DomElement;
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

  private static final Function<DomElement, XmlTag> FUN = new Function<DomElement, XmlTag>() {
    @Override
    public XmlTag apply(DomElement domElement) {
      return domElement.getXmlTag();
    }
  };

  @Override
  protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
    if (element instanceof PsiNameIdentifierOwner && JavaUtils.isElementWithinInterface(element)) {
      CommonProcessors.CollectProcessor<IdDomElement> processor = new CommonProcessors.CollectProcessor<IdDomElement>();
      JavaService.getInstance(element.getProject()).processMapperInterfaceElements(element, processor);
      Collection<IdDomElement> results = processor.getResults();
      if (!results.isEmpty()) {
        PsiElement nameIdentifier = ((PsiNameIdentifierOwner) element).getNameIdentifier();
        if (nameIdentifier != null) {
          result.add(setupBuilder(results).createLineMarkerInfo(nameIdentifier));
        }
      }
    }
  }

  private NavigationGutterIconBuilder<PsiElement> setupBuilder(Collection<IdDomElement> results) {
    return NavigationGutterIconBuilder.create(Icons.MAPPER_LINE_MARKER_ICON)
                                      .setAlignment(GutterIconRenderer.Alignment.CENTER)
                                      .setTargets(Collections2.transform(results, FUN))
                                      .setTooltipTitle("Navigation to target in mapper xml");
  }

}
