package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.SubTagList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yanglin
 */
public interface Configuration extends MyBatisElement {

  @NotNull
  @SubTagList("typeAliases")
  public List<TypeAliases> getTypeAliases();

}
