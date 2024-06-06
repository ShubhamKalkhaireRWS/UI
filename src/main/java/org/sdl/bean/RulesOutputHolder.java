package org.sdl.bean;

public class RulesOutputHolder {
	private String ruleName;
	private String ruleStatus;
	private String URL;
	private String ruleDescription;
	private String fieldType;
	private String fieldIdentifier;
	private String fieldDescription;

	public RulesOutputHolder(String ruleName, String ruleStatus, String uRL, String ruleDescription, String fieldType,
			String fieldIdentifier, String fieldDescription) {
		super();
		this.ruleName = ruleName;
		this.ruleStatus = ruleStatus;
		URL = uRL;
		this.ruleDescription = ruleDescription;
		this.fieldType = fieldType;
		this.fieldIdentifier = fieldIdentifier;
		this.fieldDescription = fieldDescription;
	}

	public RulesOutputHolder() {
		super();
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldIdentifier() {
		return fieldIdentifier;
	}

	public void setFieldIdentifier(String fieldIdentifier) {
		this.fieldIdentifier = fieldIdentifier;
	}

	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	@Override
	public String toString() {
		return "RulesOutputHolder [ruleName=" + ruleName + ", ruleStatus=" + ruleStatus + ", URL=" + URL
				+ ", ruleDescription=" + ruleDescription + ", fieldType=" + fieldType + ", fieldIdentifier="
				+ fieldIdentifier + ", fieldDescription=" + fieldDescription + "]";
	}

}
