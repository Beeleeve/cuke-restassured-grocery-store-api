package com.grocerystore.api.steps;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

	private Map<String, Object> scenarioContext;

	public ScenarioContext() {
		scenarioContext = new HashMap<String, Object>();
	}

	public void setContext(TestState key, Object value) {
		scenarioContext.put(key.toString(), value);
	}

	public Object getContext(TestState key) {
		return scenarioContext.get(key.toString());
	}

	public Boolean isContains(TestState key) {
		return scenarioContext.containsKey(key.toString());
	}

}
