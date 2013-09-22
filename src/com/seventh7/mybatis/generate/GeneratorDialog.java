package com.seventh7.mybatis.generate;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

import com.intellij.ide.util.ChooseElementsDialog;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.swing.*;

/**
 * @author yanglin
 */
public class GeneratorDialog extends ChooseElementsDialog<StatementGenerator> {

  public GeneratorDialog(Project project, List<? extends StatementGenerator> items) {
    super(project, items, "Choose target", "Choose the statement to generate");
    this.myChooser.setSingleSelectionMode();
  }

  @Override
  protected String getItemText(StatementGenerator item) {
    return item.getDisplayText();
  }

  @Nullable @Override
  protected Icon getItemIcon(StatementGenerator item) {
    return null;
  }

  @NotNull
  public Optional<StatementGenerator> getSelected() {
    return Optional.fromNullable(Iterables.getOnlyElement(this.getChosenElements(), null));
  }

}

