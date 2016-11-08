package com.seventh7.mybatis.dom.model;

import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * @author yanglin
 */
public interface GroupFour extends MyBatisElement {

  @SubTagList("constructor")
  public Constructor getConstructor();

  @SubTagList("id")
  public List<Id> getIds();

  @SubTagList("result")
  public List<Result> getResults();

  @SubTagList("association")
  public List<Association> getAssociations();

  @SubTagList("collection")
  public List<Collection> getCollections();

  @SubTagList("discriminator")
  public Discriminator getDiscriminator();

  @SubTagList("id")
  public Id addId();

  @SubTagList("result")
  public Result addResult();
}
