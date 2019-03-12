
package controllers.chapter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.ChapterService;
import controllers.AbstractController;
import domain.Area;
import domain.Chapter;
import forms.ChapterForm;

@Controller
@RequestMapping(value = "/chapter/chapter")
public class ChapterChapterController extends AbstractController {

	@Autowired
	private ChapterService	chapterService;

	@Autowired
	private AreaService		areaService;


	public ChapterChapterController() {
		super();
	}

	@RequestMapping(value = "/selectArea", method = RequestMethod.GET)
	public ModelAndView selectArea() {
		ModelAndView result;
		final ChapterForm chapterForm;

		try {
			chapterForm = new ChapterForm();

			result = this.createEditModelAndView(chapterForm);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/selectArea", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ChapterForm chapterForm, final BindingResult binding) {
		ModelAndView result;
		Chapter chapter;

		if (binding.hasErrors())
			result = this.createEditModelAndView(chapterForm);
		else
			try {
				chapter = this.chapterService.reconstruct(chapterForm, binding);
				this.chapterService.save(chapter);

				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chapterForm, "chapter.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------

	protected ModelAndView createEditModelAndView(final ChapterForm chapterForm) {
		ModelAndView result;

		result = this.createEditModelAndView(chapterForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ChapterForm chapterForm, final String messageCode) {
		ModelAndView result;
		Collection<Area> areas;

		areas = this.areaService.findAreasNotAssigned();

		result = new ModelAndView("chapter/selectArea");
		result.addObject("messageCode", messageCode);
		result.addObject("areas", areas);
		result.addObject("chapterForm", chapterForm);

		return result;

	}

}
