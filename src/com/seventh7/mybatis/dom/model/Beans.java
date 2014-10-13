package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.SubTagList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yanglin
 */
public interface Beans extends MyBatisElement {

  @NotNull
  @SubTagList("bean")
  public List<Bean> getBeans();

}
