/*
 * EnrolmentToStringConverter.java
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

import domain.Enrolment;

@Component
@Transactional
public class EnrolmentToStringConverter implements Converter<Enrolment, String> {

	@Override
	public String convert(final Enrolment enrolment) {
		String result;

		if (enrolment == null)
			result = null;
		else
			result = String.valueOf(enrolment.getId());

		return result;
	}

}
