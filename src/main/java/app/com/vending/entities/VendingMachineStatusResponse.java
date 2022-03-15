package app.com.vending.entities;

/**
 * VendingMachineStatusResponse
 * 
 * March 2022
 * 
 * Response to show current status of the vending machine
 * 
 */

import org.springframework.boot.autoconfigure.domain.EntityScan;

import app.com.vending.machine.VendingMachine.STATUS;

@EntityScan
public class VendingMachineStatusResponse {

	private STATUS status;

	public STATUS getStatus() {
		return status;
	}

	public VendingMachineStatusResponse(STATUS s) {
		this.status = s;
	}
	
	public void setStatus(STATUS status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("VendingMachineStatusResponse [status=%s]", status);
	}
	
	
}
