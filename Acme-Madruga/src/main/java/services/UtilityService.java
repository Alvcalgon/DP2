
package services;

import java.util.Date;
import java.util.Random;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
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

	@Autowired
	private ProcessionService		processionService;


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
			Assert.isTrue(actor.getEmail().matches("[A-Za-z_.]+[\\w]+@[a-zA-Z0-9.-]+|[\\w\\s]+[\\<][A-Za-z_.]+[\\w]+@[a-zA-Z0-9.-]+[\\>]|[A-Za-z_.]+[\\w]+@|[\\w\\s]+[\\<][A-Za-z_.]+[\\w]+@+[\\>]"));
		else
			Assert.isTrue(actor.getEmail().matches("[A-Za-z_.]+[\\w]+[\\S]+@[a-zA-Z0-9.-]+|[\\w\\s]+[\\<][A-Za-z_.]+[\\w]+@[a-zA-Z0-9.-]+[\\>]"));
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

	public String generateValidTicker() {
		String numbers, result;
		Integer day, month, year;
		LocalDate currentDate;
		Integer counter;

		currentDate = LocalDate.now();
		year = currentDate.getYear() % 100;
		month = currentDate.getMonthOfYear();
		day = currentDate.getDayOfMonth();

		numbers = String.format("%02d", year) + "" + String.format("%02d", month) + "" + String.format("%02d", day) + "-";
		counter = 0;

		do {
			result = numbers + this.createRandomLetters();
			counter++;
		} while (!(this.processionService.existTicker(result) == null) && counter < 650000);

		return result;
	}

	// Private methods ---------------------------------------------------------

	private String createRandomLetters() {
		String result, characters;
		Random randomNumber;

		result = "";
		randomNumber = new Random();
		characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		for (int i = 0; i <= 4; i++)
			result += characters.charAt(randomNumber.nextInt(characters.length()));

		return result;
	}

}
