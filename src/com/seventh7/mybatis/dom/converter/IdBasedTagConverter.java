package com.seventh7.mybatis.dom.converter;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.intellij.util.xml.ResolvingConverter;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * @author yanglin
 */
public abstract class IdBasedTagConverter extends ResolvingConverter<XmlTag> {

  protected boolean crossMapperSupported;

  private Function<DomElement, XmlTag> function = new Function<DomElement, XmlTag>() {
    @Override
    public XmlTag apply(DomElement element) {
      return element.getXmlTag();
    }
  };

  public IdBasedTagConverter() {
    this(true);
  }

  protected IdBasedTagConverter(boolean crossMapperSupported) {
    this.crossMapperSupported = crossMapperSupported;
  }

  @Nullable @Override
  public XmlTag fromString(@Nullable @NonNls String value, ConvertContext context) {
    return matchIdDomElement(selectStrategy(context).getValue(), value, context).orNull();
  }

  @NotNull
  private Optional<XmlTag> matchIdDomElement(Collection<? extends IdDomElement> idDomElements, String value, ConvertContext context) {
    Mapper contextMapper = MapperUtils.getMapper(context.getInvocationElement());
    for (IdDomElement idDomElement : idDomElements) {
      if (MapperUtils.getIdSignature(idDomElement).equals(value) ||
          MapperUtils.getIdSignature(idDomElement, contextMapper).equals(value)) {
        return Optional.of(idDomElement.getXmlTag());
      }
    }
    return Optional.absent();
  }

  @Nullable @Override
  public String toString(@Nullable XmlTag tag, ConvertContext context) {
    DomElement domElement = DomUtil.getDomElement(tag);
    if (!(domElement instanceof IdDomElement)) {
      return null;
    }
    Mapper contextMapper = MapperUtils.getMapper(context.getInvocationElement());
    return MapperUtils.getIdSignature((IdDomElement) domElement, contextMapper);
  }

  @NotNull @Override
  public Collection<? extends XmlTag> getVariants(ConvertContext context) {
    return Collections2.transform(selectStrategy(context).getValue(), function);
  }

  private TraverseStrategy selectStrategy(ConvertContext context) {
    return false == crossMapperSupported ? new InsideMapperStrategy(context) : new CrossMapperStrategy(context);
  }

  /**
   * @param mapper mapper in the project, null if {@link #crossMapperSupported} is false
   * @param context the dom convert context
   */
  @NotNull
  public abstract Collection<? extends IdDomElement> getComparables(@Nullable Mapper mapper, ConvertContext context);

  private abstract class TraverseStrategy {
    protected ConvertContext context;
    public TraverseStrategy(@NotNull ConvertContext context) {
      this.context = context;
    }

    public abstract Collection<? extends IdDomElement> getValue();
  }

  private class InsideMapperStrategy extends TraverseStrategy{

    public InsideMapperStrategy(@NotNull ConvertContext context) {
      super(context);
    }

    @Override
    public Collection<? extends IdDomElement> getValue() {
      return getComparables(null,  context);
    }

  }

  private class CrossMapperStrategy extends TraverseStrategy {

    public CrossMapperStrategy(@NotNull ConvertContext context) {
      super(context);
    }

    @Override
    public Collection<? extends IdDomElement> getValue() {
      List<IdDomElement> result = Lists.newArrayList();
      for (Mapper mapper : MapperUtils.findMappers(context.getProject())) {
        result.addAll(getComparables(mapper, context));
      }
      return result;
    }

  }
}
