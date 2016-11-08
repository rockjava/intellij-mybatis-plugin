package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTag;
import com.intellij.util.xml.SubTagList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yanglin
 */
public interface Choose extends MyBatisElement {

  @NotNull
  @Required
  @SubTagList("when")
  public List<When> getWhens();

  @SubTag("otherwise")
  public Otherwise getOtherwise();

}
