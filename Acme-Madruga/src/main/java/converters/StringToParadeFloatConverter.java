/*
 * StringToParadeFloatConverter.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ParadeFloatRepository;
import domain.ParadeFloat;

@Component
@Transactional
public class StringToParadeFloatConverter implements Converter<String, ParadeFloat> {

	@Autowired
	ParadeFloatRepository	paradeParadeFloatRepository;


	@Override
	public ParadeFloat convert(final String text) {
		ParadeFloat result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.paradeParadeFloatRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
