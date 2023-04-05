package com.github.nqnt.views;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AlmightyWindowFactory implements ToolWindowFactory {
	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		ContentFactory contentFactory = ContentFactory.getInstance();

		JEditorPane editor = new JEditorPane();
		editor.setContentType("text/html");
		editor.setText("<html><body>Oh, almighty.</body></html>");

		Content content = contentFactory.createContent(editor, "", false);
		toolWindow.getContentManager().addContent(content);
	}
}
