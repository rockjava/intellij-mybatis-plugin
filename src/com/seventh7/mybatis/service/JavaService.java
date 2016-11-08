package com.seventh7.mybatis.service;

import com.google.common.base.Optional;

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
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomElement;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author yanglin
 */
public class JavaService {

  private Project project;

  private JavaPsiFacade javaPsiFacade;

  private EditorService editorService;

  public JavaService(Project project) {
    this.project = project;
    this.javaPsiFacade = JavaPsiFacade.getInstance(project);
    this.editorService = EditorService.getInstance(project);
  }

  public static JavaService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, JavaService.class);
  }

  public Optional<PsiClass> getReferenceClazzOfPsiField(@NotNull PsiElement field) {
    if (!(field instanceof PsiField)) {
      return Optional.absent();
    }
    PsiType type = ((PsiField)field).getType();
    return type instanceof PsiClassReferenceType ? Optional.fromNullable(((PsiClassReferenceType) type).resolve()) : Optional.<PsiClass>absent();
  }

  public Optional<DomElement> findStatement(@Nullable PsiMethod method) {
    if (method == null) {
      return Optional.absent();
    }
    CommonProcessors.FindFirstProcessor<DomElement> processor = new CommonProcessors.FindFirstProcessor<DomElement>();
    processMapperInterfaceElements(method, processor);
    return processor.isFound() ? Optional.fromNullable(processor.getFoundValue()) : Optional.<DomElement>absent();
  }

  @SuppressWarnings("unchecked")
  public void processMapperMethods(@NotNull PsiMethod psiMethod, @NotNull final Processor<IdDomElement> processor) {
    PsiClass psiClass = psiMethod.getContainingClass();
    if (null == psiClass) {
      return;
    }
    final String methodName = psiMethod.getName();
    processMapperMethod(psiClass, methodName, processor);
    processSubMapperClazz(psiClass, new Processor<PsiClass>() {
      @Override
      public boolean process(PsiClass clazz) {
        processMapperMethod(clazz, methodName, processor);
        return true;
      }
    });
  }

  private void processSubMapperClazz(PsiClass parentClazz, Processor<PsiClass> action) {
    Collection<Mapper> mappers = MapperUtils.findMappers(project);
    for (Mapper mapper : mappers) {
      String ns = mapper.getNamespace().getStringValue();
      if (ns == null) { continue; }

      Optional<PsiClass> clazz = JavaUtils.findClazz(project, ns);
      if (!clazz.isPresent()) { continue; }

      PsiClass psiClass = clazz.get();
      if (psiClass.isInheritor(parentClazz, true)) {
        action.process(psiClass);
      }
    }
  }

  private void processMapperMethod(PsiClass psiClass, String methodName, Processor<IdDomElement> processor) {
    String id = psiClass.getQualifiedName() + "." + methodName;
    for (Mapper mapper : MapperUtils.findMappers(project)) {
      for (IdDomElement idDomElement : mapper.getDaoElements()) {
        if (MapperUtils.getIdSignature(idDomElement).equals(id)) {
          processor.process(idDomElement);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void processMapperInterfaces(@NotNull PsiClass clazz, @NotNull final Processor<Mapper> processor) {
    processMapper(clazz, processor);
    processSubMapperClazz(clazz, new Processor<PsiClass>() {
      @Override
      public boolean process(PsiClass psiClass) {
        processMapper(psiClass, processor);
        return true;
      }
    });
  }

  private void processMapper(PsiClass clazz, Processor<Mapper> processor) {
    String ns = clazz.getQualifiedName();
    for (Mapper mapper : MapperUtils.findMappers(project)) {
      if (MapperUtils.getNamespace(mapper).equals(ns)) {
        processor.process(mapper);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void processMapperInterfaceElements(@NotNull PsiElement target, @NotNull Processor processor) {
    if (target instanceof PsiMethod) {
      processMapperMethods((PsiMethod) target, processor);
    } else if (target instanceof PsiClass){
      processMapperInterfaces((PsiClass) target, processor);
    }
  }

  public <T> Optional<T> findWithFindFirstProcessor(@NotNull PsiElement target) {
    CommonProcessors.FindFirstProcessor<T> processor = new CommonProcessors.FindFirstProcessor<T>();
    processMapperInterfaceElements(target, processor);
    return Optional.fromNullable(processor.getFoundValue());
  }

  public void importClazz(PsiJavaFile file, String clazzName) {
    if (!JavaUtils.hasImportClazz(file, clazzName)) {
      Optional<PsiClass> clazz = JavaUtils.findClazz(project, clazzName);
      if (!clazz.isPresent()) {
        return;
      }
      PsiImportList importList = file.getImportList();
      if (null != importList) {
        PsiElementFactory elementFactory = javaPsiFacade.getElementFactory();
        PsiImportStatement statement = elementFactory.createImportStatement(clazz.get());
        importList.add(statement);
        editorService.format(file, statement);
      }
    }
  }
}

