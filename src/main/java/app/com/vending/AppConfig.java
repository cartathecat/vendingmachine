package app.com.vending;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import app.com.vending.entities.CoinConfig;
import app.com.vending.machine.service.VendingMachineChange;
import app.com.vending.machine.service.VendingMachineCoinBucket;
import app.com.vending.machine.service.VendingMachineDeposit;
import app.com.vending.machine.service.VendingMachineFloat;
import app.com.vending.machine.service.VendingMachineServiceImpl;

@Configuration
public class AppConfig {

	final static String DB_VERSION = "1.0.0.0";
	
	@Bean
	public VendingMachineFloat vendingMachineFloat() {
		return new VendingMachineFloat();
	}

	@Bean
	public VendingMachineDeposit vendingMachineDeposit() {
		return new VendingMachineDeposit();
	}

	@Bean
	public VendingMachineCoinBucket vendingMachineCoinBucket() {
		return new VendingMachineCoinBucket();
	}
	
	@Bean
	public VendingMachineChange vendingMachineChange() {
		return new VendingMachineChange();
	}

	@Bean
	public VendingMachineServiceImpl vendingMachineServiceImpl() {
		return new VendingMachineServiceImpl();
	}
	
	@Bean
	public CoinConfig coinConfig() {
		return new CoinConfig();
	}
	
}
