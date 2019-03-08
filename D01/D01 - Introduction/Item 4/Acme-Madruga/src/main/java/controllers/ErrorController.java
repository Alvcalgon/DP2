/*
 * WelcomeController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController extends AbstractController {

	// Services --------------------------------------------------------------

	// Constructors -----------------------------------------------------------

	public ErrorController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/error")
	public ModelAndView index(final Locale locale) {
		ModelAndView result;

		result = new ModelAndView("misc/error");

		return result;
	}
}
