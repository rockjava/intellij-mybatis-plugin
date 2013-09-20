package com.seventh7.mybatis;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

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


  private final String label;

  private final String qualifiedName;

  private final Map<String, AnnotationValue> attributePairs;

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

  public Annotation(String label, String qualifiedName) {
    this.label = label;
    this.qualifiedName = qualifiedName;
    attributePairs = Maps.newHashMap();
  }

  private Annotation addAttribute(String key, AnnotationValue value) {
    this.attributePairs.put(key, value);
    return this;
  }

  public Annotation withAttribute(@NotNull String key, @NotNull AnnotationValue value) {
    return this.clone().addAttribute(key, value);
  }

  public Annotation withValue(@NotNull AnnotationValue value) {
    return withAttribute("value", value);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(label);
    if (!Iterables.isEmpty(attributePairs.entrySet())) {
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
    builder.deleteCharAt(builder.length() - 1);
    builder.deleteCharAt(builder.length() - 1);
    builder.append(")");
    return builder.toString();
  }

  private Optional<String> getSingleValue() {
    try {
      String value = Iterables.getOnlyElement(attributePairs.keySet());
      StringBuilder builder = new StringBuilder("(");
      builder.append(attributePairs.get(value).toString());
      builder.append(")");
      return Optional.of(builder.toString());
    } catch (IllegalArgumentException e) {
      return Optional.absent();
    }
  }

  public String getLabel() {
    return label;
  }

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
