package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * @author yanglin
 */
public interface Constructor extends MyBatisElement {

  @SubTagList("arg")
  public List<Arg> getArgs();

  @SubTagList("idArg")
  public List<IdArg> getIdArgs();
}
