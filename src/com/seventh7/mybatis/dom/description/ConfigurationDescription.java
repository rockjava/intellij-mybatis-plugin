package com.seventh7.mybatis.dom.description;

import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import com.seventh7.mybatis.dom.model.Configuration;
import com.seventh7.mybatis.util.DomUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class ConfigurationDescription extends DomFileDescription<Configuration>{

  public ConfigurationDescription() {
    super(Configuration.class, "configuration");
  }

  @Override
  public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
    return DomUtils.isMybatisConfigurationFile(file);
  }

}
