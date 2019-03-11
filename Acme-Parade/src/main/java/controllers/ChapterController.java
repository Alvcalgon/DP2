
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import domain.Chapter;

@Controller
@RequestMapping(value = "/chapter")
public class ChapterController extends AbstractController {

	@Autowired
	private ChapterService	chapterService;


	public ChapterController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Chapter> chapters;

		chapters = this.chapterService.findAll();

		result = new ModelAndView("actor/list");
		result.addObject("actors", chapters);
		result.addObject("requestURI", "chapter/list.do");

		return result;
	}

}
