
package controllers.administrator;

import java.util.Collection;
import java.util.Locale;
import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PositionService;
import services.TranslationPositionService;
import controllers.AbstractController;
import domain.Position;
import domain.TranslationPosition;
import forms.PositionForm;

@Controller
@RequestMapping("/position/administrator")
public class PositionAdministratorController extends AbstractController {

	@Autowired
	private PositionService				positionService;

	@Autowired
	private TranslationPositionService	translationPositionService;


	public PositionAdministratorController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionId, final Locale locale) {
		ModelAndView result;
		TranslationPosition translationPosition;
		final String name;

		try {
			translationPosition = this.translationPositionService.findByLanguagePosition(positionId, locale.getLanguage());
			name = translationPosition.getName();

			result = new ModelAndView("position/display");
			result.addObject("name", name);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final Locale locale) {
		ModelAndView result;
		Collection<Position> positions;
		SortedMap<Integer, String> mapa;
		positions = this.positionService.findAll();

		mapa = this.positionService.positionsByLanguages(positions, locale.getLanguage());

		result = new ModelAndView("position/list");
		result.addObject("mapa", mapa);
		result.addObject("requestURI", "position/administrator/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final Locale locale) {
		ModelAndView result;
		PositionForm positionForm;

		positionForm = new PositionForm();

		result = this.createEditModelAndView(positionForm, locale);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionId, final Locale locale) {
		final ModelAndView result;
		PositionForm positionForm;
		String en_name, es_name;

		en_name = this.translationPositionService.findByLanguagePosition(positionId, "en").getName();
		es_name = this.translationPositionService.findByLanguagePosition(positionId, "es").getName();

		positionForm = new PositionForm();

		positionForm.setId(positionId);
		positionForm.setEn_name(en_name);
		positionForm.setEs_name(es_name);

		result = this.createEditModelAndView(positionForm, locale);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final PositionForm positionForm, final BindingResult binding, final Locale locale) {
		ModelAndView result;
		Position position;

		this.positionService.validateName(locale.getLanguage(), "en_name", positionForm.getEn_name(), binding);
		this.positionService.validateName(locale.getLanguage(), "es_name", positionForm.getEs_name(), binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(positionForm, locale);
		else
			try {
				position = this.positionService.reconstruct(positionForm);
				this.positionService.save(position);

				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(positionForm, locale, "position.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final PositionForm positionForm, final BindingResult binding, final Locale locale) {
		ModelAndView result;
		Position position;

		try {
			position = this.positionService.findOne(positionForm.getId());
			this.positionService.delete(position);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(positionForm, locale, "position.commit.error");
		}

		return result;
	}

	// Arcillary methods ------------------------------------
	protected ModelAndView createEditModelAndView(final PositionForm positionForm, final Locale locale) {
		ModelAndView result;

		result = this.createEditModelAndView(positionForm, locale, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final PositionForm positionForm, final Locale locale, final String messageCode) {
		ModelAndView result;
		Boolean canBeDeleted;

		if (positionForm.getId() == 0)
			canBeDeleted = false;
		else
			canBeDeleted = this.positionService.isUsedPosition(positionForm.getId()) == 0;

		result = new ModelAndView("position/edit");
		result.addObject("positionForm", positionForm);
		result.addObject("canBeDeleted", canBeDeleted);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
