
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.TranslationPosition;

@Component
@Transactional
public class TranslationPositionToStringConverter implements Converter<TranslationPosition, String> {

	@Override
	public String convert(final TranslationPosition translationPosition) {
		String result;

		if (translationPosition == null)
			result = null;
		else
			result = String.valueOf(translationPosition.getId());

		return result;
	}

}
