package com.desropolis.st.model.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("mock")
public class DomainUserRepositoryMock extends BaseRepositoryOfy implements
		DomainUserRepository {

	private static List<DomainUser> domainUsers = null;
	private static Map<String, DomainUser> domainUsersByOpenSocial = null;
	private static Map<String, DomainUser> domainUsersByEmail = null;
	static {
		makeUsers();
	}

	private static void makeUsers() {

		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_USER");
		domainUsers = new ArrayList<DomainUser>();
		domainUsersByOpenSocial = new HashMap<String, DomainUser>();
		domainUsersByEmail = new HashMap<String, DomainUser>();

		DomainUser joyce = new DomainUser();
		joyce.setEmail("joyce@desropolis.com");
		joyce.setDomain("desropolis.com");
		joyce.setRoles(roles);
		domainUsers.add(joyce);
		domainUsersByEmail.put(joyce.getEmail(), joyce);

		roles = new ArrayList<String>();
		roles.add("ROLE_USER");
		roles.add("ROLE_DOMAIN_ADMIN");
		DomainUser dean = new DomainUser();
		dean.setOpenSocialViewerId("1234567890");
		dean.setEmail("dean@desropolis.com");
		dean.setDomain("desropolis.com");
		dean.setRoles(roles);
		domainUsers.add(dean);
		domainUsersByEmail.put(dean.getEmail(), dean);
		domainUsersByOpenSocial.put(dean.getOpenSocialViewerId(), dean);

	}

	@Override
	public void save(DomainUser domainUser) {

		DomainUser u = domainUsersByEmail.remove(domainUser.getEmail());
		domainUsersByEmail.put(domainUser.getEmail(), domainUser);

		if (u != null) {
			domainUsers.remove(u);
			domainUsers.add(domainUser);
		}

		if (domainUser.getOpenSocialViewerId() != null) {
			domainUsersByOpenSocial.remove(domainUser.getOpenSocialViewerId());
			domainUsersByOpenSocial.put(domainUser.getOpenSocialViewerId(),
					domainUser);
		}

	}

	@Override
	public DomainUser findByOpenSocialViewerId(String domainName, String openSocialViewerId) {

		return domainUsersByOpenSocial.get(openSocialViewerId);

	}

	@Override
	public DomainUser findByEmail(String domainName, String email) {

		return domainUsersByEmail.get(email);

	}

	@Override
	public List<DomainUser> listAll(String domainName) {

		return domainUsers;

	}

}
