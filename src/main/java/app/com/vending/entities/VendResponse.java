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
@JsonPropertyOrder({ "product", "change", "vendtype" ,"changeReturnResponse" })
public class VendResponse {
	
	private Product product;	
	private int change;
	private VENDTYPE vendtype;
	private List<ChangeReturnResponse> changeReturnResponse;
	
	public VendResponse(Product p, int c, VENDTYPE v,List<ChangeReturnResponse> crList) {
		this.product = p;
		this.change = c;
		this.vendtype = v;
		this.changeReturnResponse = crList;
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
		this.vendtype = v;
	}
	public VENDTYPE getVendType() {
		return this.vendtype;
	}
	
	public List<ChangeReturnResponse> getChangeReturn() {
		return changeReturnResponse;
	}
	public void setChangeReturn(List<ChangeReturnResponse> cr) {
		this.changeReturnResponse = cr;
	}

	@Override
	public String toString() {
		return String.format("VendResponse [product=%s, change=%s, vendtype=%s, changeReturnResponse=%s]", product,
				change, vendtype, changeReturnResponse);
	}

}
