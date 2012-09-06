package com.desropolis.st.model.admin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Domain implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String domainName;
	private String consumerKey;
	private String consumerSecret;

	public Domain() {
	}

	public Domain(String domainName) {
		this.domainName = domainName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((consumerKey == null) ? 0 : consumerKey.hashCode());
		result = prime * result
				+ ((consumerSecret == null) ? 0 : consumerSecret.hashCode());
		result = prime * result
				+ ((domainName == null) ? 0 : domainName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Domain other = (Domain) obj;
		if (consumerKey == null) {
			if (other.consumerKey != null)
				return false;
		} else if (!consumerKey.equals(other.consumerKey))
			return false;
		if (consumerSecret == null) {
			if (other.consumerSecret != null)
				return false;
		} else if (!consumerSecret.equals(other.consumerSecret))
			return false;
		if (domainName == null) {
			if (other.domainName != null)
				return false;
		} else if (!domainName.equals(other.domainName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
