package app.com.vending;

/**
 * Vending Machine application
 * 
 * March 2022
 * 
 * Create a vending machine and appropriate beans 
 * 
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import app.com.vending.machine.VendingMachine;
import app.com.vending.machine.repository.Repository;
import app.com.vending.machine.service.VendingMachineChange;
import app.com.vending.machine.service.VendingMachineCoinBucket;
import app.com.vending.machine.service.VendingMachineDeposit;
import app.com.vending.machine.service.VendingMachineFloat;
import app.com.vending.machine.service.VendingMachineServiceImpl;

@SpringBootApplication
public class VendingMachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(VendingMachineApplication.class, args);
	}

	@Bean
	public VendingMachine vendingMachine() {
		return new VendingMachine();
	}

	@Bean
	public Repository repository() {
		return new Repository();
	}

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
	
}
