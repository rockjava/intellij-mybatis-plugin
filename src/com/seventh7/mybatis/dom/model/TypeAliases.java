package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * @author yanglin
 */
public interface TypeAliases extends DomElement {

  @SubTagList("typeAlias")
  public List<TypeAlias> getTypeAlias();

  @SubTagList("package")
  public List<Package> getPackages();

}
