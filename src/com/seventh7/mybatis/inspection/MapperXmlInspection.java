package com.seventh7.mybatis.inspection;

import com.intellij.ide.IdeBundle;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.intellij.util.xml.ElementPresentationManager;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.highlighting.BasicDomElementsInspection;
import com.intellij.util.xml.highlighting.DomElementAnnotationHolder;
import com.intellij.util.xml.highlighting.DomHighlightingHelper;
import com.seventh7.mybatis.dom.model.MyBatisElement;
import com.seventh7.mybatis.util.MapperUtils;

import java.util.List;

/**
 * @author yanglin
 */
public class MapperXmlInspection extends BasicDomElementsInspection<DomElement> {

  @SuppressWarnings("unchecked")
  public MapperXmlInspection() {
    super(DomElement.class);
  }

  @Override
  protected void checkDomElement(DomElement element,
                                 DomElementAnnotationHolder holder,
                                 DomHighlightingHelper helper) {
    if (!(element instanceof GenericAttributeValue) && !GenericDomValue.class.equals(ReflectionUtil.getRawType(
        element.getDomElementType()))) {
      List<? extends MyBatisElement> duplicateElements = MapperUtils.findDuplicateElements(element);
      for (MyBatisElement ele : duplicateElements) {
        final String typeName = ElementPresentationManager.getTypeNameForObject(element);
        final GenericDomValue genericDomValue = ele.getGenericInfo().getNameDomElement(element);
        if (genericDomValue != null) {
          holder.createProblem(genericDomValue,
                               DomUtil.getFile(element).equals(DomUtil.getFile(element)) ?
                               IdeBundle.message("model.highlighting.identity", typeName) :
                               IdeBundle.message("model.highlighting.identity.in.other.file",
                                                 typeName,
                                                 ele.getXmlTag().getContainingFile().getName()));
        }
      }
    } else {
      super.checkDomElement(element, holder, helper);
    }
  }

}