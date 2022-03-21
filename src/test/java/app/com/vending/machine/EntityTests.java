package app.com.vending.machine;

import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import app.com.vending.entities.Coins;
import app.com.vending.entities.CoinsDepositedResponse;
import app.com.vending.entities.MoneyResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMetrics
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
public class EntityTests {

	@Test
	@Order(1)
	public void MoneyResponseTest() throws Exception {
		MoneyResponse mr = new MoneyResponse(100, 200);
		Assert.assertEquals("MoneyResponse [floatValue=100, depositValue=200]", mr.toString());
	}
	
	@Test
	@Order(2)
	public void CoinDepositedResponse() throws Exception {		
		CoinsDepositedResponse cdr = new CoinsDepositedResponse(200, "Coins deposited");
		Assert.assertEquals("CoinsDepositedResponse [deposit=200, message=Coins deposited]]", cdr.toString());		
	}
	
}
