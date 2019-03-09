/*
 * CustomerToStringConverter.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import java.net.URLEncoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;

@Component
@Transactional
public class CreditCardToStringConverter implements Converter<CreditCard, String> {

	@Override
	public String convert(final CreditCard creditCard) {
		String result;
		StringBuilder builder;

		if (creditCard == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(creditCard.getHolder(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getMake(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getNumber(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getExpirationMonth(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getExpirationYear(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Integer.toString(creditCard.getCvvCode()), "UTF-8"));
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new IllegalArgumentException(oops);

			}
		return result;
	}
}
