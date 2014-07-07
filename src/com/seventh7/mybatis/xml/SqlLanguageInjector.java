package com.seventh7.mybatis.xml;

import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.sql.dialects.generic.GenericDialect;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class SqlLanguageInjector implements LanguageInjector {

  @Override
  public void getLanguagesToInject( @NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
    PsiElement context = host.getContext();
    if (context == null || !MapperUtils.isElementWithinMybatisFile(context)) {
      return;
    }
    DomElement domElement = DomUtil.getDomElement(context);
    if (domElement == null) {
      return;
    }
    Mapper mapper = DomUtil.getParentOfType(domElement, Mapper.class, true);
    if (mapper == null) {
      return;
    }

    if (context instanceof XmlAttribute) {
      injectionPlacesRegistrar.addPlace(GenericDialect.INSTANCE, ((XmlAttribute) context).getValueTextRange(), null, null);
    }
  }

}
