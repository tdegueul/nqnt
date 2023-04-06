package com.github.nqnt.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "NqntSettingsState", storages = {@Storage("NqntSettingsPlugin.xml")})
public class NqntSettingsState implements PersistentStateComponent<NqntSettingsState> {
	public String apiKey = "OPENAI_KEY";
	public int timeout = 30;

	public static NqntSettingsState getInstance() {
		return ApplicationManager.getApplication().getService(NqntSettingsState.class);
	}

	@Nullable
	@Override
	public NqntSettingsState getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull NqntSettingsState state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
