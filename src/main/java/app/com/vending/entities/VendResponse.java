package app.com.vending.entities;

/**
 * VendResponse for both a Vend item or refund
 * 
 * March 2022
 * 
 * Response to show what has been selected, included product, change value and coins returned or a refund
 *  
 */

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@EntityScan
@JsonPropertyOrder({ "product", "change", "vendType" ,"changeReturnResponse" })
public class VendResponse {
	
	private Product product;	
	private int change;
	private VENDTYPE vendType;
	private List<ChangeReturnResponse> changeReturn;
	
	public VendResponse(Product p, int c, VENDTYPE v,List<ChangeReturnResponse> crList) {
		this.product = p;
		this.change = c;
		this.vendType = v;
		this.changeReturn = crList;
	}

	public enum VENDTYPE {
		PRODUCT,
		REFUND
	}

	
	@JsonInclude(Include.NON_NULL)
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getChange() {
		return change;
	}
	public void setChange(int change) {
		this.change = change;
	}
	
	public void setVendType(VENDTYPE v) {
		this.vendType = v;
	}
	public VENDTYPE getVendType() {
		return this.vendType;
	}
	
	public List<ChangeReturnResponse> getChangeReturn() {
		return changeReturn;
	}
	public void setChangeReturn(List<ChangeReturnResponse> cr) {
		this.changeReturn = cr;
	}

	@Override
	public String toString() {
		return String.format("VendResponse [product=%s, change=%s, vendtype=%s, changeReturnResponse=%s]", product,
				change, vendType, changeReturn);
	}

}
