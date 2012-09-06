package com.desropolis.st.model.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

@Repository
@Qualifier("ofy")
public class DomainUserRepositoryOfy extends BaseRepositoryOfy implements
		DomainUserRepository {

	@Override
	public void save(DomainUser domain) {

		Objectify ofy = begin();
		ofy.put(domain);

	}

	@Override
	public DomainUser findByOpenSocialViewerId(String domainName,
			String openSocialViewerId) {

		Objectify ofy = begin();
		DomainUser domainUser = ofy.query(DomainUser.class)
				.filter("openSocialViewerId", openSocialViewerId)
				.filter("domainName", domainName).get();
		return domainUser;

	}

	@Override
	public DomainUser findByEmail(String domainName, String email) {

		Objectify ofy = begin();
		DomainUser domainUser = ofy.query(DomainUser.class)
				.filter("email", email)
				.filter("domainName", domainName).get();
		return domainUser;

	}

	@Override
	public List<DomainUser> listAll(String domainName) {
	
		Objectify ofy = begin();
		Iterable<Key<DomainUser>> allKeys = ofy.query(DomainUser.class).fetchKeys();
		Map<Key<DomainUser>, DomainUser> domainMap = ofy.get(allKeys);
		List<DomainUser> domainUsers = new ArrayList<DomainUser>(domainMap.values());
		return domainUsers;

	}

}
