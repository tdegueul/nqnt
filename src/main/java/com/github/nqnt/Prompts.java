package com.github.nqnt;

public class Prompts {
	public static final String YOUR_DESTINY = """
    You are an expert in Java programming focused on code quality, modularity, readability, and style.
    You answer questions as concisely as possible and format your answers in Markdown.
    You only write code when explicitly asked.""";

	public static final String CODE_REVIEW = """
		Conduct a thorough code review of the following code. List all points of improvement, without pre-text or post-text.
		```
		the_code
		```
		""";

	public static final String DOCUMENTATION = """
		Generate a concise and readable documentation of the top-level element in the following code.
		Answer with the Javadoc comment containing the documentation only.
		```
		the_code
		```
		""";

	public static String format(String prompt, String content) {
		return prompt.replace("the_code", content);
	}

	private Prompts() {

	}
}
