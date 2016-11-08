package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.SubTagList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yanglin
 */
public interface Bean extends MyBatisElement {

  @NotNull
  @SubTagList("property")
  public List<BeanProperty> getBeanProperties();

}
