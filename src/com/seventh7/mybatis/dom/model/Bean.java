package com.seventh7.mybatis.dom.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * @author yanglin
 */
public interface Bean extends DomElement {

  @Attribute("class")
  public GenericAttributeValue<PsiClass> getClzz();

  @SubTagList("property")
  public List<BeanProperty> getBeanProperties();

}
