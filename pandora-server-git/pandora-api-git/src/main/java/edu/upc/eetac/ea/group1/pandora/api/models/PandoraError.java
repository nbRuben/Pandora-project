package edu.upc.eetac.ea.group1.pandora.api.models;

public class PandoraError {
	private int status;
	private String message;

	public PandoraError() {
		super();
	}

	public PandoraError(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}