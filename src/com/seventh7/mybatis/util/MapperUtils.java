package com.seventh7.mybatis.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.Mapper;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;


/**
 * @author yanglin
 */
public final class MapperUtils {

  private MapperUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean isElementWithinMybatisFile(@NotNull PsiElement element) {
    return element instanceof XmlElement && isMybatisFile(((XmlFile) element.getContainingFile()));
  }

  public static boolean isMybatisFile(@NotNull XmlFile file) {
    XmlTag rootTag = file.getRootTag();
    return null != rootTag && rootTag.getName().equals("mapper");
  }

  @NotNull @NonNls
  public static Collection<Mapper> findMappers(@NotNull Project project) {
    GlobalSearchScope scope = GlobalSearchScope.allScope(project);
    List<DomFileElement<Mapper>> elements = DomService.getInstance().getFileElements(Mapper.class, project, scope);
    return Collections2.transform(elements, new Function<DomFileElement<Mapper>, Mapper>() {
      @Override
      public Mapper apply(DomFileElement<Mapper> element) {
        return element.getRootElement();
      }
    });
  }

  @NotNull @NonNls
  public static Collection<Mapper> findMappers(@NotNull Project project, @NotNull String namespace) {
    List<Mapper> result = Lists.newArrayList();
    for (Mapper mapper : findMappers(project)) {
      if (getNamespace(mapper).equals(namespace)) {
        result.add(mapper);
      }
    }
    return result;
  }

  @NotNull @NonNls
  public static Optional<Mapper> findFirstMapper(@NotNull Project project, @NotNull String namespace) {
    Collection<Mapper> mappers = findMappers(project, namespace);
    return CollectionUtils.isEmpty(mappers) ? Optional.<Mapper>absent() : Optional.of(mappers.iterator().next());
  }

  @NotNull @NonNls
  public static Optional<Mapper> findFirstMapper(@NotNull Project project, @NotNull PsiClass clzz) {
    return findFirstMapper(project, clzz.getQualifiedName());
  }

  @NotNull @NonNls
  public static Optional<Mapper> findFirstMapper(@NotNull Project project, @NotNull PsiMethod method) {
    return findFirstMapper(project, method.getContainingClass());
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
    return mapper.getNamespace().getRawText();
  }

  @NotNull @NonNls
  public static String getNamespace(@NotNull DomElement element) {
    return getNamespace(getMapper(element));
  }

  @NonNls
  public static boolean isMapperWithSameNamespace(@Nullable Mapper mapper, @Nullable Mapper target) {
    return null != mapper && null != target ? getNamespace(mapper).equals(getNamespace(target)) : false;
  }

  @Nullable @NonNls
  public static <T extends IdDomElement> String getId(@NotNull T domElement) {
    return domElement.getId().getRawText();
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

}
