package com.seventh7.mybatis.inspection;

import com.google.common.collect.Lists;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.highlighting.BasicDomElementsInspection;
import com.intellij.util.xml.highlighting.DomElementAnnotationHolder;
import com.intellij.util.xml.highlighting.DomHighlightingHelper;
import com.seventh7.mybatis.dom.model.Delete;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.Insert;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.dom.model.Select;
import com.seventh7.mybatis.dom.model.Update;
import com.seventh7.mybatis.util.MapperUtils;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author yanglin
 */
public class ConflictingStatementInspection extends BasicDomElementsInspection<DomElement> {

  private Class[] targets = {Select.class, Update.class, Insert.class, Delete.class};

  public ConflictingStatementInspection() {
    super(DomElement.class);
  }

  @Override
  protected void checkDomElement(DomElement element, DomElementAnnotationHolder holder, DomHighlightingHelper helper) {
    if (!isTarget(element)) return;
    String id = ((IdDomElement) element).getId().getStringValue();
    if (StringUtils.isBlank(id)) return;
    String ns = MapperUtils.getNamespace(element);
    if (StringUtils.isBlank(ns)) return;

    Collection<Mapper> mappers = MapperUtils.findMappers(element.getManager().getProject());
    List<IdDomElement> res = Lists.newArrayList();
    for (Mapper mapper : mappers) {
      if (MapperUtils.getNamespace(mapper).equals(ns)) {
        List<IdDomElement> daoElements = mapper.getDaoElements();
        for (IdDomElement idDomElement : daoElements) {
          String tmpId = idDomElement.getId().getStringValue();
          if (StringUtils.isNotBlank(tmpId) && tmpId.equals(id)) {
            res.add(idDomElement);
          }
        }
      }
    }

    if (res.size() > 1) {
      holder.createProblem(element, "Conflicting statement found for id=\"" + id + "\"", new ConflictingStatementQuickFix(res));
    }
  }

  private boolean isTarget(DomElement element) {
    for (Class clzz : targets) {
      if (clzz.isInstance(element)) {
        return true;
      }
    }
    return false;
  }
}
