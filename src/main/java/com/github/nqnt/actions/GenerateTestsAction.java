package com.github.nqnt.actions;

import com.github.nqnt.Prompts;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNamedElement;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class GenerateTestsAction extends AlmightyAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		Project project = event.getProject();
		if (project == null)
			return;

		Optional<PsiNamedElement> selectedElement = getSelectedElement(event);

		selectedElement.ifPresent(element -> {
			List<ChatMessage> chatMessages = List.of(
				new ChatMessage("system", Prompts.YOUR_DESTINY),
				new ChatMessage("user", Prompts.format(Prompts.GENERATE_TESTS, element.getText()))
			);

			backgroundMagic(project, chatMessages, "Finding bugs");
		});
	}
}
