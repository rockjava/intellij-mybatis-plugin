package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * @author yanglin
 */
public interface Beans extends DomElement {

  @SubTagList("bean")
  public List<Bean> getBeans();

}
