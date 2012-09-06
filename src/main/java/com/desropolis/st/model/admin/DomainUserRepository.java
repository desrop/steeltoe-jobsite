package com.desropolis.st.model.admin;

import java.util.List;

public interface DomainUserRepository {

	void save(DomainUser domainUser);
	DomainUser findByOpenSocialViewerId(String domainName, String openSocialUserId);
	DomainUser findByEmail(String domainName, String email);
	List<DomainUser> listAll(String domainName);
	
}
