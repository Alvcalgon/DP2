
package forms;

import domain.Area;

public class ChapterRegistrationForm extends RegistrationForm {

	private String	title;
	private Area	area;


	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public Area getArea() {
		return this.area;
	}

	public void setArea(final Area area) {
		this.area = area;
	}

}
