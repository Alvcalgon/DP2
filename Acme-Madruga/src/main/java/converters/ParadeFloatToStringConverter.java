/*
 * ParadeFloatToStringConverter.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.ParadeFloat;

@Component
@Transactional
public class ParadeFloatToStringConverter implements Converter<ParadeFloat, String> {

	@Override
	public String convert(final ParadeFloat paradeParadeFloatDomain) {
		String result;

		if (paradeParadeFloatDomain == null)
			result = null;
		else
			result = String.valueOf(paradeParadeFloatDomain.getId());

		return result;
	}
}
