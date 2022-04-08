package app.com.vending;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vending Machine application
 * 
 * March 2022
 * 
 * Create a vending machine and appropriate beans 
 * 
 * 24/03 - Removed VendingMachine bean
 * 
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import app.com.vending.entities.CoinConfig;
import app.com.vending.machine.service.VendingMachineChange;
import app.com.vending.machine.service.VendingMachineCoinBucket;
import app.com.vending.machine.service.VendingMachineDeposit;
import app.com.vending.machine.service.VendingMachineFloat;
import app.com.vending.machine.service.VendingMachineServiceImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.*;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "VendingMachine API", version = "1.0", description = "VendingMachine simulator"))
public class VendingMachineApplication {

	private final static Logger log = LoggerFactory.getLogger(VendingMachineApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(VendingMachineApplication.class, args);
	}
	
	@PostConstruct
	private static void Running() {
		log.info("Servier is running");
	}

}
