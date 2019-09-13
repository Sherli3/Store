package store.configuration.model;

public enum Status {
	VERIFIED("Verified"), PENDING("Pending"), INVALID("Invalid");
	final String type;

	private Status(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
