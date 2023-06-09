package com.github.nqnt.actions;

import com.github.nqnt.Prompts;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiDocCommentOwner;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiNamedElement;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class DocumentationAction extends AlmightyAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		Project project = event.getProject();
		if (project == null)
			return;

		Optional<PsiNamedElement> selectedElement = getSelectedElement(event);

		selectedElement.ifPresent(element -> {
			List<ChatMessage> chatMessages = List.of(
				new ChatMessage("system", Prompts.YOUR_DESTINY),
				new ChatMessage("user", Prompts.format(Prompts.DOCUMENTATION, element.getText()))
			);

			Task.Backgroundable task = new Task.Backgroundable(project, "Generating documentation", true) {
				public void run(@NotNull ProgressIndicator indicator) {
					indicator.setText("Contacting OpenAI...");
					String answer = doSomeMagic(chatMessages);

					indicator.setText("Rendering results...");
					renderYourMagic(project, answer, false);

					indicator.setText("Patching code...");
					Optional<PsiNamedElement> element = getSelectedElement(event);
					element.ifPresent(elem -> {
						if (elem instanceof PsiDocCommentOwner docOwner)
							ApplicationManager.getApplication().invokeLater(() ->
								WriteCommandAction.runWriteCommandAction(project, () -> {
									PsiElementFactory factory = PsiElementFactory.getInstance(project);
									PsiComment comment = factory.createCommentFromText(answer, null);

									if (docOwner.getDocComment() != null) {
										docOwner.getDocComment().replace(comment);
									} else {
										docOwner.addBefore(comment, docOwner.getFirstChild());
									}
								})
							);
					});
				}
			};

			ProgressManager.getInstance().run(task);
		});
	}
}
