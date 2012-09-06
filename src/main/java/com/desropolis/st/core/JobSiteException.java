package com.desropolis.st.core;

public class JobSiteException extends RuntimeException {

	private static final long serialVersionUID = -638803125640618571L;

	private Throwable t;
	private String message;

	public JobSiteException(String message, Throwable t) {
		this.message = message;
		this.t = t;
	}

	public Throwable getCause() {
		return t;
	}

	public String getMessage() {
		return message;
	}

}
