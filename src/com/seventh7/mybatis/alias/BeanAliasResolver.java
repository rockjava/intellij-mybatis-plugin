package com.seventh7.mybatis.alias;

import com.google.common.collect.Sets;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xml.DomService;
import com.seventh7.mybatis.dom.model.Bean;
import com.seventh7.mybatis.dom.model.BeanProperty;
import com.seventh7.mybatis.dom.model.Beans;
import com.seventh7.mybatis.util.CollectionUtils;
import com.seventh7.mybatis.util.DomUtils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Set;

/**
 * @author yanglin
 */
public class BeanAliasResolver extends PackageAliasResolver{

  private SAXBuilder builder = new SAXBuilder(false) {{
    setEntityResolver(new EntityResolver() {
      @Override public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return new InputSource(new StringReader(""));
      }
    });
  }};

  public BeanAliasResolver(Project project) {
    super(project);
  }

  @NotNull @Override
  public Collection<String> getPackages() {
    Set<String> result = Sets.newHashSet();
    Collection<Beans> domElements = DomUtils.findDomElements(project, Beans.class);
    if (CollectionUtils.isEmpty(domElements)) {
      return standby();
    } else {
      for (Beans bs : domElements) {
        for (Bean bean : bs.getBeans()) {
          for (BeanProperty pop : bean.getBeanProperties()) {
            if (pop.getName().getStringValue().equals("typeAliasesPackage")) {
              result.add(pop.getValue().getStringValue());
            }
          }
        }
      }
    }
    return result;
  }

  private Collection<String> standby() {
    Collection<VirtualFile> candidates = DomService.getInstance().getDomFileCandidates(Beans.class, project);
    Set<String> result = Sets.newHashSet();
    try {
      for (VirtualFile file : candidates) {
        file.refresh(true, true);
        Document document = builder.build(new ByteArrayInputStream(file.contentsToByteArray()));
        XPath path = setupXPath("org.mybatis.spring.SqlSessionFactoryBean", "typeAliasesPackage");
        Element property = (Element)path.selectSingleNode(document);
        if (null != property) {
          result.add(property.getAttributeValue("value"));
        }
      }
    } catch (Exception e) {
      return result;
    }
    return result;
  }

  private XPath setupXPath(String beanClassName, String propertyName) throws JDOMException {
    String text = "/ns:beans/ns:bean[@class='" + beanClassName +"']/ns:property[@name='" + propertyName + "']";
    XPath path = XPath.newInstance(text);
    path.addNamespace("ns", "http://www.springframework.org/schema/beans");
    return path;
  }

}
