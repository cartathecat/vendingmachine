package app.com.vending.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class CoinConverter implements Converter<String,Coin> {

	private final static Logger log = LoggerFactory.getLogger(CoinConverter.class);
			
	@Override
	public Coin convert(String source) {
		log.debug("CoinConverter : " + source);
		String[] c = source.split(":");
		return new Coin(c[0],Integer.parseInt(c[1]));
	}

}
