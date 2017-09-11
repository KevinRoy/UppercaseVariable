package com.kevin.plugin.uppercasevariable.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBDimension;
import com.kevin.plugin.uppercasevariable.utils.StringUtils;
import org.apache.http.util.TextUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class UpperCaseAction extends AnAction {

    private String className;
    private boolean isKotlin;

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Caret caret = event.getData(PlatformDataKeys.CARET);
        Editor editor = event.getData(PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE);

        Messages.InputDialog inputDialog = new Messages.InputDialog(project, "Please input the Class Name", "Input Class Name"
                , Messages.getInformationIcon(), "", new InputValidator() {
            @Override
            public boolean checkInput(String s) {
                if (!TextUtils.isEmpty(s))
                    return true;

                return false;
            }

            @Override
            public boolean canClose(String s) {
                return true;
            }
        }) {
            @Override
            protected JPanel createMessagePanel() {
                JPanel messagePanel = new JPanel(new BorderLayout());
                if (myMessage != null) {
                    JComponent textComponent = createTextComponent();
                    messagePanel.add(textComponent, BorderLayout.NORTH);
                }

                myField = createTextFieldComponent();
                messagePanel.add(createScrollableTextComponent(), BorderLayout.CENTER);

                JCheckBox jCheckBox = new JCheckBox("is data?");
                jCheckBox.addActionListener(e -> {
                    if (jCheckBox.isSelected()) {
                        isKotlin = true;
                    } else {
                        isKotlin = false;
                    }
                });
                messagePanel.add(jCheckBox, BorderLayout.SOUTH);

                return messagePanel;
            }

            @Override
            protected JTextComponent createTextFieldComponent() {
                JTextArea jTextArea = new JTextArea(15, 100);
                jTextArea.setMinimumSize(new JBDimension(800, 500));
                jTextArea.setMaximumSize(new JBDimension(1000, 700));
                jTextArea.setLineWrap(true);
                jTextArea.setWrapStyleWord(true);
                jTextArea.setAutoscrolls(true);
                return jTextArea;
            }

            protected JComponent createScrollableTextComponent() {
                return new JBScrollPane(myField);
            }
        };

        inputDialog.show();

        className = inputDialog.getInputString();

        if (TextUtils.isEmpty(className))
            return;

        Document document = editor.getDocument();

        CommandProcessor.getInstance().executeCommand(project, () -> ApplicationManager.getApplication().runWriteAction(() -> {

            int offset = 0;

            if (caret != null) {
                offset = caret.getOffset();
            } else {
                offset = document.getTextLength() - 1;
            }

            if (isKotlin)
                document.insertString(offset, StringUtils.getKotlinString(className));
            else
                document.insertString(offset, StringUtils.getJavaString(className));

        }), "insertKotlin", null);
    }
}
