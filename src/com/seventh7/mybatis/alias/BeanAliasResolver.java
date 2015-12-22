package com.seventh7.mybatis.alias;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.CommonSpringModel;
import com.intellij.spring.SpringManager;
import com.intellij.spring.model.SpringBeanPointer;
import com.intellij.spring.model.SpringModelSearchParameters;
import com.intellij.spring.model.utils.SpringModelSearchers;
import com.intellij.spring.model.utils.SpringPropertyUtils;
import com.intellij.spring.model.xml.beans.SpringPropertyDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class BeanAliasResolver extends PackageAliasResolver{

  private static final String MAPPER_ALIAS_PACKAGE_CLASS = "org.mybatis.spring.SqlSessionFactoryBean";
  private static final String MAPPER_ALIAS_PROPERTY = "typeAliasesPackage";
    private final SpringModelSearchParameters.BeanClass searchParameters;
    private ModuleManager moduleManager;
  private SpringManager springManager;

  public BeanAliasResolver(Project project) {
    super(project);
    this.moduleManager = ModuleManager.getInstance(project);
    this.springManager = SpringManager.getInstance(project);
    this.searchParameters = createBeanSearchParameters(project);
  }

    private SpringModelSearchParameters.BeanClass createBeanSearchParameters(Project project) {
        final JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
        final PsiClass psiClass = psiFacade.findClass(MAPPER_ALIAS_PACKAGE_CLASS, GlobalSearchScope.projectScope(project));
        return psiClass == null? null: SpringModelSearchParameters.byClass(psiClass);
    }

    @NotNull @Override
  public Collection<String> getPackages(@Nullable PsiElement element) {
    Set<String> res = Sets.newHashSet();
    if (searchParameters == null) {
      return res;
    }
    for (Module module : moduleManager.getModules()) {
      for (CommonSpringModel springModel : springManager.getCombinedModel(module).getModelsToProcess()) {
        addPackages(res, springModel);
      }
    }
    return res;
  }

  private void addPackages(Set<String> res, CommonSpringModel springModel) {
    for (SpringBeanPointer springBaseBeanPointer : SpringModelSearchers.findBeans(springModel, searchParameters)) {
      SpringPropertyDefinition basePackages = SpringPropertyUtils.findPropertyByName(springBaseBeanPointer.getSpringBean(), MAPPER_ALIAS_PROPERTY);
      if (basePackages != null) {
        final String value = basePackages.getValueElement().getStringValue();
        if (value != null) {
          res.add(value);
        }
      }
    }
  }

}
