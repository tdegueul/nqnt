package com.github.nqnt;

public class Prompts {
	public static final String YOUR_DESTINY = """
    Act as a worldwide expert in Java programming with perfect knowledge in software engineering, programming, and latest Java features.
    Answer questions as concisely as possible.
    Don't output code unless explicitly asked.""";

	public static final String CODE_REVIEW = """
		Conduct a thorough code review of the following code. List all points of improvement, without pre-text or post-text.
		Focus on code quality, modularity, readability, correctness, language features, and style.
		Answer in Markdown.
		```
		the_code
		```
		""";

	public static final String DOCUMENTATION = """
		Generate a concise, readable, and insightful documentation of the top-level element in the following code.
		Answer with a Javadoc comment in the form `/** documentation */` without any Markdown.
		```
		the_code
		```
		""";

	public static final String FIND_BUG = """
		Can you find any bug, unexpected behavior, or unexpected exception in the code below?
		What are the inputs that could be provided to this code and make it fail?
		Let's think step by step.
		Answer in Markdown.
		```
		the_code
		```
		""";

	public static final String GENERATE_TESTS = """
		Propose a list of unit test cases for the following code without pre-text or post-text.
		Apply Java testing best practices when appropriate, including mocking, stubbing, and fluent assertions.
		Answer in Markdown.
		```
		the_code
		```
		
		Each test case should be in the following format:
		```
		- Test name: <name>
		- Test description: <short_description>
		- Tested method: <method_name>
		- Test implementation:
		<code>
		```
		""";

	public static String format(String prompt, String content) {
		return prompt.replace("the_code", content);
	}

	private Prompts() {

	}
}
