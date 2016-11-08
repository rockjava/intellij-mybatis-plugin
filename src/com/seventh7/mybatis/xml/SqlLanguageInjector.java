package com.seventh7.mybatis.xml;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.sql.dialects.generic.GenericDialect;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.seventh7.mybatis.dom.model.Delete;
import com.seventh7.mybatis.dom.model.Insert;
import com.seventh7.mybatis.dom.model.Select;
import com.seventh7.mybatis.dom.model.Update;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author yanglin
 */
public class SqlLanguageInjector implements MultiHostInjector {

    private static final Class[] SUPPORTED_TYPES = new Class[]{Select.class, Insert.class, Update.class, Delete.class};

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
        if (!context.isValid() || !(context instanceof XmlText)) {
            return;
        }

        XmlTag xmlTag = PsiTreeUtil.getParentOfType(context, XmlTag.class, true);
        DomElement domElement = DomUtil.getDomElement(xmlTag);
        if (!isSupportedType(domElement)) {
            return;
        }
        registrar.startInjecting(GenericDialect.INSTANCE)
                 .addPlace(null, null, (PsiLanguageInjectionHost) context, TextRange.create(0, context.getTextLength()));
    }

    private static boolean isSupportedType(DomElement element) {
        if (element == null) {
            return false;
        }

        for (Class clazz : SUPPORTED_TYPES) {
            if (clazz.isInstance(element)) {
                return true;
            }
        }

        return false;
    }

    @NotNull
    @Override
    public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Collections.singletonList(XmlText.class);
    }
}
