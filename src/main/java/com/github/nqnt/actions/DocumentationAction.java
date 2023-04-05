package com.github.nqnt.actions;

import com.github.nqnt.Prompts;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiJavaDocumentedElement;
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

		Optional<String> selectedText = getSelectedText(event);

		selectedText.ifPresent(text -> {
			List<ChatMessage> chatMessages = List.of(
				new ChatMessage("system", Prompts.YOUR_DESTINY),
				new ChatMessage("user", Prompts.format(Prompts.DOCUMENTATION, text))
			);

			String answer = doSomeMagic(chatMessages);
			renderYourMagic(project, answer);

			Optional<PsiJavaDocumentedElement> element = getSelectedElement(event);
			element.ifPresent(elem ->
				ApplicationManager.getApplication().invokeLater(() ->
					WriteCommandAction.runWriteCommandAction(project, () -> {
						PsiElementFactory factory = PsiElementFactory.getInstance(project);
						PsiComment comment = factory.createCommentFromText(answer, null);

						if (elem.getDocComment() != null) {
							elem.getDocComment().replace(comment);
						} else {
							elem.addBefore(comment, elem.getFirstChild());
						}
					})
				)
			);
		});
	}
}
