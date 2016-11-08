package com.seventh7.mybatis.annotation;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Simple implementation
 * @author yanglin
 */
public class Annotation implements Cloneable{

  public static final Annotation PARAM = new Annotation("@Param", "org.apache.ibatis.annotations.Param");

  public static final Annotation SELECT = new Annotation("@Select", "org.apache.ibatis.annotations.Select");

  public static final Annotation UPDATE = new Annotation("@Update", "org.apache.ibatis.annotations.Update");

  public static final Annotation INSERT = new Annotation("@Insert", "org.apache.ibatis.annotations.Insert");

  public static final Annotation DELETE = new Annotation("@Delete", "org.apache.ibatis.annotations.Delete");

  public static final Annotation ALIAS = new Annotation("@Alias", "org.apache.ibatis.type.Alias");

  public static final Annotation AUTOWIRED = new Annotation("@Autowired", "org.springframework.beans.factory.annotation.Autowired");

  public static final Annotation RESOURCE = new Annotation("@Resource", "javax.annotation.Resource");

  public static final Set<Annotation> STATEMENT_SYMMETRIES = ImmutableSet.of(SELECT, UPDATE, INSERT, DELETE);

  private final String label;

  private final String qualifiedName;

  private ImmutableMap<String, AnnotationValue> attributePairs;

  public interface AnnotationValue {
  }

  public static class StringValue implements AnnotationValue{

    private String value;

    public StringValue(@NotNull String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return "\"" + value + "\"";
    }

  }

  public Annotation(@NotNull String label, @NotNull String qualifiedName) {
    this.label = label;
    this.qualifiedName = qualifiedName;
    this.attributePairs = ImmutableMap.of();
  }

  public Annotation withAttribute(@NotNull String key, @NotNull AnnotationValue value) {
    Annotation copy = this.clone();
    copy.attributePairs = ImmutableMap.<String, AnnotationValue>builder()
                          .putAll(this.attributePairs)
                          .put(key, value)
                          .build();
    return copy;
  }

  public Annotation withValue(@NotNull AnnotationValue value) {
    return withAttribute("value", value);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(label);
    if (!attributePairs.isEmpty()) {
      builder.append(setupAttributeText());
    }
    return builder.toString();
  }

  private String setupAttributeText() {
    Optional<String> singleValue = getSingleValue();
    return singleValue.isPresent() ? singleValue.get() : getComplexValue();
  }

  private String getComplexValue() {
    StringBuilder builder = new StringBuilder("(");
    for (String key : attributePairs.keySet()) {
      builder.append(key);
      builder.append(" = ");
      builder.append(attributePairs.get(key).toString());
      builder.append(", ");
    }
    int length = builder.length();
    builder.delete(length - 2, length);
    builder.append(")");
    return builder.toString();
  }

  @NotNull
  public Optional<PsiClass> toPsiClass(@NotNull Project project) {
    return JavaUtils.findClazz(project, getQualifiedName());
  }

  private Optional<String> getSingleValue() {
    try {
      String value = Iterables.getOnlyElement(attributePairs.keySet());
      return Optional.of("(" + attributePairs.get(value).toString() + ")");
    } catch (Exception e) {
      return Optional.absent();
    }
  }

  @NotNull
  public String getLabel() {
    return label;
  }

  @NotNull
  public String getQualifiedName() {
    return qualifiedName;
  }

  @Override
  protected Annotation clone() {
    try {
      return (Annotation)super.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException();
    }
  }

}
