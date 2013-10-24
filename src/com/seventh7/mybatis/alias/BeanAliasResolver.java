package com.seventh7.mybatis.alias;

import com.google.common.collect.Sets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.spring.CommonSpringModel;
import com.intellij.spring.model.utils.SpringModelUtils;
import com.intellij.spring.model.utils.SpringPropertyUtils;
import com.intellij.spring.model.xml.beans.SpringBaseBeanPointer;
import com.intellij.spring.model.xml.beans.SpringPropertyDefinition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author yanglin
 */
public class BeanAliasResolver extends PackageAliasResolver{

  public static String MAPPER_ALIASE_PACKAGE_CLZZ = "org.mybatis.spring.SqlSessionFactoryBean";

  public BeanAliasResolver(Project project) {
    super(project);
  }

  @NotNull @Override
  public Collection<String> getPackages(@Nullable PsiElement element) {
    CommonSpringModel springModel = SpringModelUtils.getSpringModel(element);
    if (null == springModel) {
      return Collections.emptyList();
    }
    Set<String> res = Sets.newHashSet();
    for (SpringBaseBeanPointer springBaseBeanPointer : springModel.findBeansByPsiClassWithInheritance(MAPPER_ALIASE_PACKAGE_CLZZ)) {
      SpringPropertyDefinition basePackages = SpringPropertyUtils.findPropertyByName(springBaseBeanPointer.getSpringBean(), "typeAliasesPackage");
      if (basePackages != null) {
        final String value = basePackages.getValueElement().getStringValue();
        if (value != null) {
          res.add(value);
        }
      }
    }
    return res;
  }


}
