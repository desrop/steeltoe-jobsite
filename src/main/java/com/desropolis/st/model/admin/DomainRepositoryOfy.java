package com.desropolis.st.model.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

@Repository
public class DomainRepositoryOfy extends BaseRepositoryOfy implements DomainRepository {

	@Override
	public void save(Domain domain) {
		
		Objectify ofy = begin();
		ofy.put(domain);
		
	}

	@Override
	public Domain find(long domainId) {

		Objectify ofy = begin();
		Domain domain = ofy.get(Domain.class, domainId);
		return domain;

	}

	@Override
	public List<Domain> listAll() {
	
		Objectify ofy = begin();
		Iterable<Key<Domain>> allKeys = ofy.query(Domain.class).fetchKeys();
		Map<Key<Domain>, Domain> domainMap = ofy.get(allKeys);
		List<Domain> domains = new ArrayList<Domain>(domainMap.values());
		return domains;

	}

}
