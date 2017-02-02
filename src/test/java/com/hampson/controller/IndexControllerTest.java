package com.hampson.controller;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

public class IndexControllerTest {

	private IndexController controller;
	private Model model;
	
	@Before
	public void setup() {
		controller = new IndexController();
		model = Mockito.mock(Model.class);
	}
	
	@After
	public void tearDown() {
		controller = null;
	}
	
	@Test
	public void testIndex() {
		String expected = "index";
		String actual = controller.index(model);
		
		assertEquals(expected, actual);
	}

}
