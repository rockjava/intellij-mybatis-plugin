package com.seventh7.mybatis.service;

import com.google.common.base.Optional;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Processor;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class JavaService {

  private Project project;

  public JavaService(Project project) {
    this.project = project;
  }

  public static final JavaService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, JavaService.class);
  }

  public void process(@NotNull PsiMethod method, @NotNull Processor processor) {
    Optional<Mapper> mapper = MapperUtils.findFirstMapper(project, method);
    if (mapper.isPresent()) {
      for (IdDomElement idDomElement : mapper.get().getDaoElements()) {
        if (MapperUtils.getId(idDomElement).equals(method.getName())) {
          processor.process(idDomElement);
        }
      }
    }
  }

  public void process(@NotNull PsiClass clzz, @NotNull Processor processor) {
    Optional<Mapper> mapper = MapperUtils.findFirstMapper(project, clzz);
    if (mapper.isPresent()) {
      processor.process(mapper.get());
    }
  }

  public void process(@NotNull PsiElement target, @NotNull Processor processor) {
    if (target instanceof PsiMethod) {
      process((PsiMethod) target, processor);
    } else if (target instanceof PsiClass){
      process((PsiClass)target, processor);
    }
  }

  public <T> Optional<T> findWithFindFristProcessor(@NotNull PsiElement target) {
    CommonProcessors.FindFirstProcessor<T> processor = new CommonProcessors.FindFirstProcessor<T>();
    process(target, processor);
    return Optional.fromNullable(processor.getFoundValue());
  }

}

