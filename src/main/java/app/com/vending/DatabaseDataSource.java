package app.com.vending;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseDataSource {

	@Value("${vendingmachine.datasource.url}")
	private String url;
	@Value("${vendingmachine.datasource.username}")
	private String username;
	@Value("${vendingmachine.datasource.password}")
	private String password;
	@Value("${vendingmachine.datasource.driver-class-name}")
	private String driverclassname;
	
	@Bean
    public DataSource dataSource() {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(this.driverclassname);
        dataSourceBuilder.url(this.url);
        dataSourceBuilder.username(this.username);
        dataSourceBuilder.password(this.password);
        return dataSourceBuilder.build();
    }	
	
}
