package com.seventh7.mybatis.service;

import com.google.common.base.Optional;

import com.intellij.formatting.FormatTextRanges;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.impl.source.codeStyle.CodeFormatterFacade;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Processor;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.JavaUtils;
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

  public static JavaService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, JavaService.class);
  }

  @SuppressWarnings("unchecked")
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

  @SuppressWarnings("unchecked")
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

  public void importClzz(PsiJavaFile file, String clzzName) {
    if (!JavaUtils.hasImportClzz(file, clzzName)) {
      Optional<PsiClass> clzz = JavaUtils.findClzz(project, clzzName);
      PsiImportList importList = file.getImportList();
      if (clzz.isPresent() && null != importList) {
        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiImportStatement statement = elementFactory.createImportStatement(clzz.get());
        importList.add(statement);

        CodeFormatterFacade formatter = new CodeFormatterFacade(new CodeStyleSettings());
        formatter.processText(file, new FormatTextRanges(statement.getTextRange(), true), true);
      }
    }
  }
}

