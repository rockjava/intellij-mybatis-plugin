package com.seventh7.mybatis.inspection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.dom.model.IdDomElement;
import com.seventh7.mybatis.ui.ListSelectionListener;
import com.seventh7.mybatis.ui.UiComponentFacade;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author yanglin
 */
public class ConflictingStatementQuickFix extends GenericQuickFix {

  private final List<IdDomElement> elements;

  public ConflictingStatementQuickFix(@NotNull List<IdDomElement> elements) {
    this.elements = elements;
  }

  @NotNull @Override
  public String getName() {
    return "Remove conflicting statement";
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    UiComponentFacade.getInstance(project).showListPopup("Choose statement to remove", new ListSelectionListener() {
      @Override
      public void selected(int index) {
        try {
          elements.get(index).getXmlElement().delete();
        } catch (IncorrectOperationException e) {
        }
      }

      @Override
      public boolean isWriteAction() {
        return true;
      }
    }, getDisplayText());
  }

  private String[] getDisplayText() {
    Collection<String> res = Collections2.transform(elements, new Function<IdDomElement, String>() {
      @Override
      public String apply(IdDomElement input) {
        XmlElement xmlElement = MapperUtils.getMapper(input).getXmlElement();
        String relativePath = FileUtil.getRelativePath(xmlElement.getProject().getBasePath(),
                                                       xmlElement.getContainingFile().getVirtualFile().getCanonicalPath().toString(), File.separatorChar);
        return "\"" + MapperUtils.getId(input) + "\" in \"" + relativePath + "\"";
      }
    });
    return res.toArray(new String[res.size()]);
  }
}
