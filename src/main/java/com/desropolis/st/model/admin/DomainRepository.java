package com.desropolis.st.model.admin;

import java.util.List;

public interface DomainRepository {

	void save(Domain domain);
	Domain find(long domainId);
	List<Domain> listAll();
	
}
