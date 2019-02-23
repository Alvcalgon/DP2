
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.TranslationPositionRepository;
import domain.TranslationPosition;

@Component
@Transactional
public class StringToTranslationPositionConverter implements Converter<String, TranslationPosition> {

	@Autowired
	TranslationPositionRepository	translationPositionRepository;


	@Override
	public TranslationPosition convert(final String text) {
		TranslationPosition result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.translationPositionRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
