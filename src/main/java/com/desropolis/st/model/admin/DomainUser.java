package com.desropolis.st.model.admin;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DomainUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	Long id;
	private String domain;
	private String email;
	private String openSocialViewerId;
	private List<String> roles;

	public DomainUser() {

	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpenSocialViewerId() {
		return openSocialViewerId;
	}

	public void setOpenSocialViewerId(String openSocialViewerId) {
		this.openSocialViewerId = openSocialViewerId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime
				* result
				+ ((openSocialViewerId == null) ? 0 : openSocialViewerId
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DomainUser other = (DomainUser) obj;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (openSocialViewerId == null) {
			if (other.openSocialViewerId != null)
				return false;
		} else if (!openSocialViewerId.equals(other.openSocialViewerId))
			return false;
		return true;
	}

}
