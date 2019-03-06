
package services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Administrator;

@Service
@Transactional
public class UtilityService {

	// Managed repository ------------------------------------------------------

	// Supporting services -----------------------------------------------------

	@Autowired
	private CustomisationService	customisationService;


	// Constructors ------------------------
	public UtilityService() {
		super();
	}

	// Methods ------------------------------
	public Date current_moment() {
		Date result;

		result = new Date(System.currentTimeMillis() - 1);

		return result;
	}

	public void checkEmailActors(final Actor actor) {
		if (actor instanceof Administrator)
			Assert.isTrue(actor.getEmail().matches("[A-Za-z0-9]+@[a-zA-Z0-9.-]+|[\\w\\s]+[\\<][A-Za-z0-9]+@[a-zA-Z0-9.-]+[\\>]|[A-Za-z0-9]+@|[\\w\\s]+[\\<][A-Za-z0-9]+@+[\\>]"));
		else
			Assert.isTrue(actor.getEmail().matches("[A-Za-z0-9]+@[a-zA-Z0-9.-]+|[\\w\\s]+[\\<][A-Za-z0-9]+@[a-zA-Z0-9.-]+[\\>]"));
	}

	public String getValidPhone(String phone) {
		String countryCode, result;

		if (phone != null && !phone.equals("")) {
			phone = phone.trim();

			if (phone.matches("(([0-9]{1,3}\\ )?([0-9]+))")) {
				countryCode = this.customisationService.find().getCountryCode();
				result = countryCode + " " + phone;
			} else
				result = phone;
		} else
			result = null;

		return result;
	}

	public void checkPicture(final String pictures) {
		final List<String> pictureList;

		Assert.notNull(pictures);
		pictureList = this.getSplittedString(pictures);

		for (final String at : pictureList)
			try {
				new URL(at);
			} catch (final MalformedURLException e) {
				throw new IllegalArgumentException("Invalid URL");
			}
	}

	public List<String> getSplittedString(final String string) {
		List<String> result;
		String[] stringsArray;

		result = new ArrayList<>();
		stringsArray = string.split("\r");

		for (String at : stringsArray) {
			at = at.trim();
			if (!at.isEmpty())
				result.add(at);
		}

		return result;
	}

	public List<String> ListByString(final String cadena) {
		List<String> result;
		String[] campos;
		String word;

		campos = cadena.split(",");

		result = new ArrayList<String>();
		for (final String s : campos) {
			word = s.trim();
			result.add(word);
		}

		return result;
	}
}
