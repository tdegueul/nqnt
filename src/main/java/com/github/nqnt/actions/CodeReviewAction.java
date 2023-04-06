package com.github.nqnt.actions;

import com.github.nqnt.Prompts;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class CodeReviewAction extends AlmightyAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		Project project = event.getProject();
		if (project == null)
			return;

		Optional<String> selectedText = getSelectedText(event);

		selectedText.ifPresent(text -> {
			List<ChatMessage> chatMessages = List.of(
				new ChatMessage("system", Prompts.YOUR_DESTINY),
				new ChatMessage("user", Prompts.format(Prompts.CODE_REVIEW, text))
			);

			Task.Backgroundable task = new Task.Backgroundable(project, "Processing Code Review", true) {
				public void run(@NotNull ProgressIndicator indicator) {
					indicator.setText("Contacting OpenAI...");
					String answer = doSomeMagic(chatMessages);
					indicator.setText("Rendering results...");
					ApplicationManager.getApplication().invokeLater(() -> renderYourMagic(project, answer));
				}
			};

			ProgressManager.getInstance().run(task);
		});
	}
}
