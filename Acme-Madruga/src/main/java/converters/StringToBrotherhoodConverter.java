/*
 * StringToBrotherhoodConverter.java
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

import repositories.BrotherhoodRepository;
import domain.Brotherhood;

@Component
@Transactional
public class StringToBrotherhoodConverter implements Converter<String, Brotherhood> {

	@Autowired
	BrotherhoodRepository	brotherhoodRepository;


	@Override
	public Brotherhood convert(final String text) {
		Brotherhood result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.brotherhoodRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
