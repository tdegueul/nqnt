package com.github.nqnt.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.ui.content.Content;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public abstract class AlmightyAction extends AnAction {
	protected Optional<String> getSelectedText(AnActionEvent event) {
		Editor editor = event.getData(CommonDataKeys.EDITOR);

		if (editor != null) {
			// If there is some text selected, retrieve it
			SelectionModel selectionModel = editor.getSelectionModel();
			String selected = selectionModel.getSelectedText();
			if (selected != null && !selected.isEmpty())
				return Optional.of(selected);

			// Otherwise, if we clicked on some element, retrieve its text
			Optional<PsiJavaDocumentedElement> clickedMember = getSelectedElement(event);
			if (clickedMember.isPresent())
				return Optional.of(clickedMember.get().getText());
		}

		return Optional.empty();
	}

	protected Optional<PsiJavaDocumentedElement> getSelectedElement(AnActionEvent event) {
		Editor editor = event.getData(CommonDataKeys.EDITOR);
		if (editor == null)
			return Optional.empty();
		
		PsiElement clickedElement = PsiUtilBase.getElementAtCaret(editor);
		PsiJavaDocumentedElement clickedMember = PsiTreeUtil.getParentOfType(clickedElement, PsiJavaDocumentedElement.class, false);
		return Optional.ofNullable(clickedMember);
	}

	protected String doSomeMagic(List<ChatMessage> messages) {
		try {
			OpenAiService service = new OpenAiService("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");
			ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
				.messages(messages)
				.model("gpt-3.5-turbo")
				.build();
			List<ChatCompletionChoice> choices = service.createChatCompletion(completionRequest).getChoices();

			return choices.isEmpty()
				? ""
				: choices.get(0).getMessage().getContent();
		} catch (Exception e) {
			return "An error occured: " + e.getMessage();
		}
	}

	protected void renderYourMagic(Project project, String answer) {
		if (!answer.isEmpty()) {
			ToolWindow almightyWindow = ToolWindowManager.getInstance(project).getToolWindow("AlmightyViewer");
			if (almightyWindow != null) {
				almightyWindow.show(() -> {
					Content content = almightyWindow.getContentManager().getContent(0);
					if (content.getComponent() instanceof JEditorPane pane) {
						Parser parser = Parser.builder().build();
						Node document = parser.parse(answer);
						HtmlRenderer renderer = HtmlRenderer.builder().build();
						String htmlAnswer = renderer.render(document);
						pane.setText(htmlAnswer);
					}
				});
			}
		}
	}
}
