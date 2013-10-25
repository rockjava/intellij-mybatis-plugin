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

  Icon MAPPER_LINE_MARKER_ICON = IconLoader.getIcon("/images/s.png");

  Icon STATEMENT_LINE_MARKER_ICON = IconLoader.getIcon("/images/7.png");

  Icon SPRING_INJECTION_ICON= IconLoader.getIcon("/images/injection.png");
}