
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import services.CustomisationService;
import domain.Customisation;

@ControllerAdvice
public class GlobalController {

	@Autowired
	private CustomisationService	customisationService;


	@ModelAttribute("banner")
	public String bannerHeader() {
		Customisation customisation;
		String result;

		customisation = this.customisationService.find();
		result = customisation.getBanner();

		return result;
	}

}
