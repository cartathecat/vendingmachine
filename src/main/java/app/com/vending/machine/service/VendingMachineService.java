package app.com.vending.machine.service;

/**
 * VendingMachineService interface
 * 
 * March 2022
 *  
 */

import java.util.List;
import app.com.vending.entities.ChangeReturnResponse;
import app.com.vending.entities.VendResponse;

public interface VendingMachineService {

	public VendingMachineFloat VendingMachineFloat();
	public VendingMachineDeposit VendingMachineDeposit();
	public VendingMachineChange VendingMachineChange();
	public VendingMachineCoinBucket VendingMachineCoinBucket();
	
	public void Initialise(int floatValue);	
	public List<?> GetProducts();
	public VendResponse VendItem(int id);
	public void RemoveCoins(List<ChangeReturnResponse> crList);	
}
