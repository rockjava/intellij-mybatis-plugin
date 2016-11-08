package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.SubTagList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yanglin
 */
public interface TypeAliases extends MyBatisElement {

  @NotNull
  @SubTagList("typeAlias")
  public List<TypeAlias> getTypeAlias();

  @NotNull
  @SubTagList("package")
  public List<Package> getPackages();

}
