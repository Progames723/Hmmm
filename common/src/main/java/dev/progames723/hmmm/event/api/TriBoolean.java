package dev.progames723.hmmm.event.api;

public enum TriBoolean {
	TRUE(true),
	DEFAULT(null),
	FALSE(false);
	
	TriBoolean(Boolean value) {this.value = value;}
	
	private final Boolean value;
	
	public Boolean value() {
		return value;
	}
}
