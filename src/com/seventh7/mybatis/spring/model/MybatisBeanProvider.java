package com.seventh7.mybatis.spring.model;

import com.google.common.collect.Sets;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spring.CommonSpringModel;
import com.intellij.spring.model.CommonSpringBean;
import com.intellij.spring.model.SpringImplicitBeansProviderBase;
import com.intellij.spring.model.xml.beans.SpringPropertyDefinition;
import com.intellij.util.Processor;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.MapperUtils;
import com.seventh7.mybatis.util.SpringUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author yanglin
 */
public class MybatisBeanProvider extends SpringImplicitBeansProviderBase {

  private static final String SCANNER_BEAN_CLAZZ_NAME = "org.mybatis.spring.mapper.MapperScannerConfigurer";
  private static final String SCANNER_PROP_NAME = "basePackage";
  private static final String MY_NAME = "Mybatis bean provider";
  private static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

  @Override
  protected boolean accepts(@NotNull CommonSpringModel springModel, @NotNull Module module) {
    return findMapperScannerPackage(module.getProject(), springModel) != null;
  }

  @NotNull @Override
  protected String getProviderName() {
    return MY_NAME;
  }

  @Override
  protected Collection<CommonSpringBean> getImplicitBeans(@NotNull CommonSpringModel model, @NotNull Module module) {
    Project project = module.getProject();
    String basePackage = findMapperScannerPackage(module.getProject(), model);
    Collection<Mapper> mappers = MapperUtils.findMappers(project, GlobalSearchScope.moduleScope(module));
    HashSet<CommonSpringBean> res = Sets.newHashSet();
    for (Mapper mapper : mappers) {
      PsiClass clazz = findClassInDependenciesAndLibraries(module, MapperUtils.getNamespace(mapper));
      if (clazz != null) {
        String qualifiedName = clazz.getQualifiedName();
        for (String basePackageItem : tokenizeToStringArray(basePackage, CONFIG_LOCATION_DELIMITERS)) {
          if (qualifiedName != null && qualifiedName.startsWith(basePackageItem)) {
            this.addImplicitBean(res, module, qualifiedName, WordUtils.uncapitalize(clazz.getName()));
          }
        }
      }
    }
    return res;
  }

  public static List<String> tokenizeToStringArray(String str, String delimiters) {
    if(str == null) {
      return Collections.emptyList();
    } else {
      StringTokenizer st = new StringTokenizer(str, delimiters);
      List<String> tokens = new ArrayList<String>();

      while(st.hasMoreTokens()) {
        String token = st.nextToken();
          token = token.trim();

        if(token.length() > 0) {
          tokens.add(token);
        }
      }

      return tokens;
    }
  }

  public static String findMapperScannerPackage(@NotNull Project project, @NotNull CommonSpringModel model) {
    final Ref<String> ref = Ref.create();
    processMapperScannerPackage(project, model, new Processor<SpringPropertyDefinition>() {
      @Override
      public boolean process(SpringPropertyDefinition def) {
        final String value = def.getValueElement().getStringValue();
        if (StringUtils.isNotBlank(value)) {
          ref.set(value);
          return false;
        }
        return true;
      }
    });
    return ref.get();
  }

  public static void processMapperScannerPackage(@NotNull Project project,
                                                 @NotNull CommonSpringModel model,
                                                 @NotNull Processor<SpringPropertyDefinition> processor) {
    SpringUtils.processSpringConfig(project, model, SCANNER_BEAN_CLAZZ_NAME, SCANNER_PROP_NAME, processor);
  }
}
