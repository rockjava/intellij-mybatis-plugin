package com.seventh7.mybatis.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.intellij.util.xml.ElementPresentationManager;
import com.seventh7.mybatis.dom.model.Configuration;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.dom.model.MyBatisElement;
import com.seventh7.mybatis.dom.model.TypeAlias;
import com.seventh7.mybatis.dom.model.TypeAliases;
import com.seventh7.mybatis.service.JavaService;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.generate.tostring.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


/**
 * @author yanglin
 */
public final class MapperUtils {

  private MapperUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean isTargetPresentInMapperXml(@NotNull PsiElement element) {
    return JavaService.getInstance(element.getProject()).findWithFindFirstProcessor(element).isPresent();
  }

  @NotNull
  public static Optional<IdDomElement> findParentIdDomElement(@Nullable PsiElement element) {
    DomElement domElement = DomUtil.getDomElement(element);
    if (null == domElement) {
      return Optional.absent();
    }
    if (domElement instanceof IdDomElement) {
      return Optional.of((IdDomElement) domElement);
    }
    return Optional.fromNullable(DomUtil.getParentOfType(domElement, IdDomElement.class, true));
  }

  public static PsiElement createMapperFromFileTemplate(@NotNull String fileTemplateName,
                                                        @NotNull String fileName,
                                                        @NotNull PsiDirectory directory,
                                                        @Nullable Properties pops) throws Exception {
    FileTemplate fileTemplate = FileTemplateManager.getInstance().getJ2eeTemplate(fileTemplateName);
    return FileTemplateUtil.createFromTemplate(fileTemplate, fileName, pops, directory);
  }

  @NotNull
  public static Collection<PsiDirectory> findMapperDirectories(@NotNull Project project) {
    return Collections2.transform(findMappers(project), new Function<Mapper, PsiDirectory>() {
      @Override
      public PsiDirectory apply(Mapper input) {
        /** The containing file of a mapper should never return null */
        return input.getXmlElement().getContainingFile().getContainingDirectory();
      }
    });
  }

  public static boolean isElementWithinMybatisFile(@NotNull PsiElement element) {
    PsiFile psiFile = element.getContainingFile();
    return element instanceof XmlElement && DomUtils.isMybatisFile(psiFile);
  }

  @NotNull @NonNls
  public static Collection<Mapper> findMappers(@NotNull Project project) {
    return DomUtils.findDomElements(project, Mapper.class);
  }

  @NotNull @NonNls
  public static Collection<Mapper> findMappers(@NotNull Project project, @NotNull GlobalSearchScope scope) {
    return DomUtils.findDomElements(project, scope, Mapper.class);
  }

  @NotNull @NonNls
  public static Collection<Mapper> findMappers(@NotNull Project project, @Nullable String namespace) {
    if (namespace == null) {
      return Collections.emptyList();
    }
    List<Mapper> result = Lists.newArrayList();
    for (Mapper mapper : findMappers(project)) {
      if (getNamespace(mapper).equals(namespace)) {
        result.add(mapper);
      }
    }
    return result;
  }

  @NotNull
  public static Collection<Mapper> findMappers(@NotNull Project project, @NotNull PsiClass clazz) {
    return JavaUtils.isElementWithinInterface(clazz) ? findMappers(project, clazz.getQualifiedName()) : Collections.<Mapper>emptyList();
  }

  @NotNull
  public static Collection<Mapper> findMappers(@NotNull Project project, @NotNull PsiMethod method) {
    PsiClass clazz = method.getContainingClass();
    return null == clazz ? Collections.<Mapper>emptyList() : findMappers(project, clazz);
  }

  @NotNull @NonNls
  public static Optional<Mapper> findFirstMapper(@NotNull Project project, @NotNull String namespace) {
    Collection<Mapper> mappers = findMappers(project, namespace);
    return CollectionUtils.isEmpty(mappers) ? Optional.<Mapper>absent() : Optional.of(mappers.iterator().next());
  }

  @NotNull @NonNls
  public static Optional<Mapper> findFirstMapper(@NotNull Project project, @NotNull PsiClass clazz) {
    String qualifiedName = clazz.getQualifiedName();
    return null != qualifiedName ? findFirstMapper(project, qualifiedName) : Optional.<Mapper>absent();
  }

  @NotNull @NonNls
  public static Optional<Mapper> findFirstMapper(@NotNull Project project, @NotNull PsiMethod method) {
    PsiClass containingClass = method.getContainingClass();
    return null != containingClass ? findFirstMapper(project, containingClass) : Optional.<Mapper>absent();
  }

  @SuppressWarnings("unchecked")
  @NotNull @NonNls
  public static Mapper getMapper(@NotNull DomElement element) {
    Optional<Mapper> optional = Optional.fromNullable(DomUtil.getParentOfType(element, Mapper.class, true));
    if (optional.isPresent()) {
      return optional.get();
    } else {
      throw new IllegalArgumentException("Unknown element");
    }
  }

  @NotNull @NonNls
  public static String getNamespace(@NotNull Mapper mapper) {
    String ns = mapper.getNamespace().getStringValue();
    return null == ns ? "" : ns;
  }

  @NotNull @NonNls
  public static String getNamespace(@NotNull DomElement element) {
    return getNamespace(getMapper(element));
  }

  @NonNls
  public static boolean isMapperWithSameNamespace(@Nullable Mapper mapper, @Nullable Mapper target) {
    return null != mapper && null != target && getNamespace(mapper).equals(getNamespace(target));
  }

  @NotNull @NonNls
  public static <T extends IdDomElement> String getId(@NotNull T domElement) {
    String rawText = domElement.getId().getRawText();
    return rawText == null ? "" : rawText;
  }

  @NotNull @NonNls
  public static <T extends IdDomElement> String getIdSignature(@NotNull T domElement) {
    return getNamespace(domElement) + "." + getId(domElement);
  }

  @NotNull @NonNls
  public static <T extends IdDomElement> String getIdSignature(@NotNull T domElement, @NotNull Mapper mapper) {
    Mapper contextMapper = getMapper(domElement);
    return isMapperWithSameNamespace(contextMapper, mapper) ? getId(domElement) : getIdSignature(domElement);
  }

  public static void processConfiguredTypeAliases(@NotNull Project project, @NotNull Processor<TypeAlias> processor) {
    for (Configuration conf : getMybatisConfigurations(project)) {
      for (TypeAliases tas : conf.getTypeAliases()) {
        for (TypeAlias ta : tas.getTypeAlias()) {
          String stringValue = ta.getAlias().getStringValue();
          if (null != stringValue && !processor.process(ta)) {
            return;
          }
        }
      }
    }
  }

  private static Collection<Configuration> getMybatisConfigurations(Project project) {
    return DomUtils.findDomElements(project, Configuration.class);
  }

  public static void processConfiguredPackage(@NotNull Project project,
                                              @NotNull Processor<com.seventh7.mybatis.dom.model.Package> processor) {
    for (Configuration conf : getMybatisConfigurations(project)) {
      for (TypeAliases tas : conf.getTypeAliases()) {
        for (com.seventh7.mybatis.dom.model.Package pkg : tas.getPackages()) {
          if (!processor.process(pkg)) {
            return;
          }
        }
      }
    }
  }

  @NotNull
  public static List<? extends MyBatisElement> findDuplicateElements(@Nullable DomElement element) {
    if (!(element instanceof MyBatisElement)) {
      return Collections.emptyList();
    }
    final String elementName = ElementPresentationManager.getElementName(element);
    if (StringUtil.isEmpty(elementName)) {
      return Collections.emptyList();
    }
    List<? extends DomElement> identitySiblings = DomUtil.getIdentitySiblings(element);
    ArrayList<MyBatisElement> duplicateElements = Lists.newArrayList();
    for (DomElement ele : identitySiblings) {
      boolean duplicated = (ele instanceof MyBatisElement) &&
                           Comparing.equal(elementName, ElementPresentationManager.getElementName( ele), true) &&
                           hasSameDatabaseId((MyBatisElement)element, (MyBatisElement)ele);
      if (duplicated) {
        duplicateElements.add((MyBatisElement) ele);
      }
    }
    return duplicateElements;
  }

  public static boolean hasSameDatabaseId(@Nullable MyBatisElement ele1, @Nullable MyBatisElement ele2) {
    if (ele1 == null || ele2 == null) {
      return false;
    }

    String databaseId1 = ele1.getDatabaseId().getStringValue();
    String databaseId2 = ele2.getDatabaseId().getStringValue();
    return Comparing.equal(databaseId1, databaseId2, false);
  }

}
