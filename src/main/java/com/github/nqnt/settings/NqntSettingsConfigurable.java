package com.github.nqnt.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class NqntSettingsConfigurable implements Configurable {
	private JPanel panel;
	private JTextField apiKeyField;
	private JTextField timeoutField;

	@Nls(capitalization = Nls.Capitalization.Title)
	@Override
	public String getDisplayName() {
		return "NQNT Settings";
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		panel = new JPanel(new GridLayout(2, 2));
		panel.setPreferredSize(new Dimension(400, 400));

		JLabel apiKeyLabel = new JLabel("OpenAI key:", SwingConstants.TRAILING);
		apiKeyField = new JTextField(52);
		apiKeyLabel.setLabelFor(apiKeyField);
		JLabel timeoutLabel = new JLabel("OpenAI timeout:", SwingConstants.TRAILING);
		timeoutField = new JTextField(2);
		timeoutLabel.setLabelFor(timeoutField);

		panel.add(apiKeyLabel);
		panel.add(apiKeyField);
		panel.add(timeoutLabel);
		panel.add(timeoutField);

		return panel;
	}

	@Override
	public boolean isModified() {
		NqntSettingsState state = NqntSettingsState.getInstance();
		return !apiKeyField.getText().equals(state.apiKey) ||
			Integer.parseInt(timeoutField.getText()) != state.timeout;
	}

	@Override
	public void apply() {
		NqntSettingsState settings = NqntSettingsState.getInstance();
		settings.apiKey = apiKeyField.getText();
		settings.timeout = Integer.parseInt(timeoutField.getText());
	}

	@Override
	public void reset() {
		NqntSettingsState state = NqntSettingsState.getInstance();
		apiKeyField.setText(state.apiKey);
		timeoutField.setText(String.valueOf(state.timeout));
	}

	@Override
	public void disposeUIResources() {
		apiKeyField = null;
		timeoutField = null;
		panel = null;
	}
}
