package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * @author yanglin
 */
public interface Discriminator extends JavaTypeGroup, JdbcGroup {

  @Required
  @SubTagList("case")
  public List<Case> getCases();

}
