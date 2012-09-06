package com.desropolis.st.model.admin;

import org.springframework.beans.factory.annotation.Autowired;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

public abstract class BaseRepositoryOfy {

	@Autowired
	private ObjectifyFactory objectifyFactory;

	Objectify testOfy;

	protected Objectify begin() {
		
		Objectify ofy = null;
		if (testOfy == null) {
			ofy = objectifyFactory.begin();
		} else {
			ofy = testOfy;
		}

		return ofy;
		
	}

}
