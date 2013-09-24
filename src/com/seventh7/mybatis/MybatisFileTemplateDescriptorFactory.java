package com.seventh7.mybatis;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import com.seventh7.mybatis.util.Icons;

/**
 * @author yanglin
 */
public class MybatisFileTemplateDescriptorFactory implements FileTemplateGroupDescriptorFactory {

  public static final String MYBATIS_MAPPER_XML_TEMPLATE = "Mybatis Mapper.xml";

  @Override
  public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
    FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor("Mybaits", Icons.MYBATIS_LOGON);
    group.addTemplate(new FileTemplateDescriptor(MYBATIS_MAPPER_XML_TEMPLATE, Icons.MYBATIS_LOGON));
    return group;
  }

}
