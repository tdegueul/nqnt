package com.github.nqnt.views;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

public class AlmightyConsoleFactory implements ToolWindowFactory {
	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
		Content content = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "Almighty Output", false);
		toolWindow.getContentManager().addContent(content);
		consoleView.print("Hello from MyPlugin!", ConsoleViewContentType.NORMAL_OUTPUT);
	}
}
