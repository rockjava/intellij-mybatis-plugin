package com.seventh7.mybatis.util;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.PlatformIcons;

import javax.swing.*;

/**
 * @author yanglin
 */
public interface Icons {

  Icon MYBATIS_LOGO = IconLoader.getIcon("/javaee/persistenceId.png");

  Icon PARAM_COMPLECTION_ICON = PlatformIcons.PARAMETER_ICON;

  Icon MAPPER_LINE_MARKER_ICON = IconLoader.getIcon("/gutter/implementedMethod.png");

  Icon STATEMENT_LINE_MARKER_ICON = IconLoader.getIcon("/gutter/overridingMethod.png");

}