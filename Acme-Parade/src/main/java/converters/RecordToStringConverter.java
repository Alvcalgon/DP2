
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Record;

@Component
@Transactional
public class RecordToStringConverter implements Converter<Record, String> {

	@Override
	public String convert(final Record record) {
		String result;

		if (record == null)
			result = null;
		else
			result = String.valueOf(record.getId());

		return result;
	}

}
