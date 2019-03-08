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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.CustomisationService;
import domain.Customisation;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	// Services --------------------------------------------------------------
	@Autowired
	private CustomisationService	customisationService;


	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(final Locale locale) {
		ModelAndView result;
		Customisation customisation;
		SimpleDateFormat formatter;
		String moment, system_name, welcome_message;

		customisation = this.customisationService.find();

		if (locale.getLanguage().equals(new Locale("es").getLanguage()))
			welcome_message = customisation.getSpanishWelcomeMessage();
		else
			welcome_message = customisation.getEnglishWelcomeMessage();

		system_name = customisation.getName();

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		result.addObject("system_name", system_name);
		result.addObject("welcome_message", welcome_message);
		result.addObject("moment", moment);

		return result;
	}

	@RequestMapping(value = "/terms")
	public ModelAndView terms() {
		ModelAndView result;

		result = new ModelAndView("welcome/terms");

		return result;
	}
}
