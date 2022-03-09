package app.com.vending.entities;

/**
 * Coins
 * 
 * March 2022
 * 
 * Hold the list of valid coins
 *  
 */

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public abstract class Coins {

	private final static Logger log = LoggerFactory.getLogger(Coins.class);
	
	public enum COINVALUE {
		ONE     	(1),
		TWO     	(2),
		FIVE    	(5),
		TEN 		(10),
		TWENTY  	(20),
		FIFTY		(50),
		ONEPOUND	(100),
		TWOPOUND	(200);
		
		private int value = 0;
		private static final Map<String, Integer> lookup = new HashMap<String, Integer>();
		
		static {
			for (COINVALUE c : COINVALUE.values()) {
				lookup.put(c.name(), c.getValue());
				log.debug("COINS {} {}", c.name(), c.getValue());
			}
		}
		
		private COINVALUE(int value) {
			this.value = value;	
		}
		
		public int getValue() {
			return value;
		}

		public static int coinValue(String c) {
			return lookup.get(c);
		}

		public static boolean coinValid(String c) {
			return lookup.containsKey(c);
		}

	}
	
	
	public Coins() {
	}
	
}
