package utez.edu.mx.SistemaCDS.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

//@Service
public class DataSourceConfiguration {

    //@Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3307/sistema_cds");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        return dataSource;

    }
}
