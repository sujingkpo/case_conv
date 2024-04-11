package com.czy.case_conv;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.czy.case_conv.settings.Settings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class SelectionReplacerAction extends AnAction {
    @Override
    public void update(final AnActionEvent e) {
        //Get required data keys
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor and if some text in the editor is selected
        e.getPresentation().setVisible(
            project != null
                && editor != null
                && editor.getSelectionModel().hasSelection()
        );
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        Project project = anActionEvent.getData(CommonDataKeys.PROJECT);

        if (editor == null || project == null) {
            return;
        }

        CaretModel caretModel = editor.getCaretModel();
        Document document = editor.getDocument();

        ArrayList<Replacement> replacements = new ArrayList<>();
        for (Caret caret : caretModel.getAllCarets()) {
            if (!caret.hasSelection()) {
                continue;
            }

            String selectedText = caret.getSelectedText();
            assert selectedText != null; // because we checked .hasSelection() above, selectedText will not be null.

            StringBuilder replacement = new StringBuilder();
            switch (Settings.getInstance().newlineMode) {
                case RECORD_SEPARATOR:
                    Pattern p = Pattern.compile("\r?\n");
                    Matcher m = p.matcher(selectedText);

                    String record;
                    int off = 0;
                    while (m.find()) {
                        record = selectedText.substring(off, m.start());
                        if (!record.isEmpty()) {
                            replacement.append(replace(record));
                        }

                        replacement.append(m.group(0)); // append the line separator

                        off = m.end();
                    }

                    record = selectedText.substring(off);
                    if (!record.isEmpty()) {
                        replacement.append(replace(record));
                    }
                    break;
                case WHITESPACE:
                    replacement.append(replace(selectedText));
                    break;
            }

            replacements.add(new Replacement(
                caret.getSelectionStart(),
                caret.getSelectionEnd(),
                replacement.toString()
            ));
        }

        // Sort in reverse order so a replacement won't mess up the indices of the other replacements
        replacements.sort((o1, o2) -> -o1.compareTo(o2));

        WriteCommandAction.runWriteCommandAction(project, () -> replacements.forEach(r -> r.doReplace(document)));
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }

    abstract protected String replace(String s);
}
