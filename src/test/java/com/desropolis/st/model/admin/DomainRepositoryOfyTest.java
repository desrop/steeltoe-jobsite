package com.desropolis.st.model.admin;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-context.xml"})
public class DomainRepositoryOfyTest {

	@Autowired
	private LocalServiceTestHelper helper;

	@Autowired
	DomainRepository domainRepository;

	@Autowired
	ObjectifyFactory objectifyFactory;
	
	Domain d1 = new Domain("test1.com");
	Domain d2 = new Domain("test2.com");
	
	@Before
	public void setUp() {

		helper.setUp();
		
		Objectify ofy = objectifyFactory.begin();
		List<Domain> ds = new ArrayList<Domain>();
		ds.add(d1);
		ds.add(d2);
		ofy.put(ds);

	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testFind() {
		
		Domain domain = domainRepository.find(d1.getId());
		assertNotNull(domain);
		assertEquals(d1, domain);
		
	}

	@Test
	public void testListAll() {
		
		List<Domain> domains = domainRepository.listAll();
		assertNotNull(domains);
		assertEquals(2, domains.size());
		String dn = domains.get(0).getDomainName();
		assertTrue("test1.com".equals(dn) || "test2.com".equals(dn));
		if("test1.com".equals(dn)) {
			assertEquals("test2.com", domains.get(1).getDomainName());
		} else {
			assertEquals("test1.com", domains.get(1).getDomainName());
		}
		
	}

	@Test
	public void testUpdate() {
		
		assertNull(d1.getConsumerKey());
		d1.setConsumerKey("abcdef");
		domainRepository.save(d1);
		
		Domain d = domainRepository.find(d1.getId());
		assertNotNull(d);
		assertNotNull(d.getConsumerKey());
		assertEquals("abcdef", d.getConsumerKey());
		
	}

	@Test
	public void testAdd() {

		Domain d = new Domain("test3.com");
		
		assertNull(d.getId());
		domainRepository.save(d);
		assertNotNull(d.getId());
		
		Domain d3 = domainRepository.find(d.getId());
		assertNotNull(d3);
		assertEquals(d, d3);
		
	}
	
}
