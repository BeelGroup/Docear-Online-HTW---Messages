package org.docear.messages.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserIdentifier implements Serializable {
	private final String username;
	private final String source;

	public UserIdentifier(String source, String username) {
		super();
		this.username = username;
		this.source = source;
	}

	public String getUsername() {
		return username;
	}

	public String getSource() {
		return source;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserIdentifier) {
			final UserIdentifier userIdentifier = (UserIdentifier) obj;
			return userIdentifier.getUsername().equals(username) && userIdentifier.getSource().equals(source);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (username+source).hashCode();
	}
	
	@Override
	public String toString() {
		return "{Source: "+source+"; Username: "+username+"}";
	}
}
