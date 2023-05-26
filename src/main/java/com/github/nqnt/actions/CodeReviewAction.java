package com.github.nqnt.actions;

import com.github.nqnt.Prompts;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CodeReviewAction extends AlmightyAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		Project project = event.getProject();
		if (project == null)
			return;

		Optional<PsiNamedElement> selectedElement = getSelectedElement(event);
		debug(project, "Clicked on " + selectedElement);

		selectedElement.ifPresent(element -> {
			List<ChatMessage> chatMessages = List.of(
				new ChatMessage("system", Prompts.YOUR_DESTINY),
				new ChatMessage("user", Prompts.format(Prompts.CODE_REVIEW, element.getText()))
			);

			/*debug(project, "Prompt is " + chatMessages);

			List<PsiElement> context = extractContextualElements(project, element);
			debug(project, "### CONTEXT ###");
			debug(project, context.stream().map(PsiElement::getText).collect(Collectors.joining("\n")));
			debug(project, "### CONDENSED ###");
			debug(project, condenseContext(project, context));*/

			backgroundMagic(project, chatMessages, "Reviewing code");
		});
	}

	private String condenseContext(Project project, List<PsiElement> context) {
		StringBuilder builder = new StringBuilder();

		context.forEach(elem -> {
			if (elem instanceof PsiClass cls) {
				builder.append(cls.getText() + "\n");
			}
			else if (elem instanceof PsiTypeElement type) {
				if (type.getType() instanceof PsiClass cls)
					builder.append(cls.getText());
				else
					debug(project, "Ignoring " + type);
			}
			else if (elem instanceof PsiMethod method) {
				builder.append(method.getText() + "\n");
			}
			else if (elem instanceof PsiField field) {
				builder.append(field.getText() + "\n");
			} else
				debug(project, "Ignoring " + elem);
		});

		return builder.toString();
	}

	private List<PsiElement> extractContextualElements(Project project, PsiNamedElement element) {
		List<PsiElement> context = new ArrayList<>();

		if (element instanceof PsiClass type) {
			type.accept(new JavaRecursiveElementVisitor() {
				@Override
				public void visitField(PsiField field) {
					super.visitField(field);

				}

				@Override
				public void visitMethod(PsiMethod method) {
					super.visitMethod(method);
				}
			});
		}

		if (element instanceof PsiMethod method) {
			context.add(method.getReturnTypeElement());
			context.addAll(Arrays.stream(method.getParameterList().getParameters()).map(PsiParameter::getTypeElement).toList());

			method.accept(new JavaRecursiveElementVisitor() {
				@Override
				public void visitMethodCallExpression(PsiMethodCallExpression expr) {
					super.visitMethodCallExpression(expr);
					context.add(expr.resolveMethod());
				}

				@Override
				public void visitTypeElement(PsiTypeElement type) {
					super.visitTypeElement(type);

					if (type instanceof PsiClass cls) {
						context.add(cls);
					}
				}
			});
		}

		return context;
	}
}
