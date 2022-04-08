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

public interface IVendingMachineService {

	public VendingMachineFloat VendingMachineFloat();
	public VendingMachineDeposit VendingMachineDeposit();
	public VendingMachineChange VendingMachineChange();
	public VendingMachineCoinBucket VendingMachineCoinBucket();
	
	public void Initialise(int floatValue);	
	public List<?> GetAllProducts();
	public VendResponse FindItem(int id);
	public void RemoveCoins(List<ChangeReturnResponse> crList);	
}
