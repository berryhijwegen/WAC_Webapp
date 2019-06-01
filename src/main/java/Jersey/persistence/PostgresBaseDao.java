package Jersey.persistence;


import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class PostgresBaseDao {
    /**
     * Om dit werkend te krijgen moet je in bestand
     *
     * 		src/main/webapp/META-INF/context.xml
     *
     * De juiste instellingen voor de database opgeven en
     * zorgen dat in deze database de queries uit het
     *
     * CompanyExampleDump.sql zijn uitgevoerd. Je kunt dit
     * SQL-bestand vinden in de root van dit project.
     *
     * @return Connection
     */
    protected final Connection getConnection() {
        Connection result = null;

        try {
            String url = "jdbc:postgresql://rtzxixexiyefib:9fb8dcaf0a2c979fbd8022a97d8cd9c6c7a97eb6f76588edbe9255b8b6c34936@ec2-54-246-92-116.eu-west-1.compute.amazonaws.com:5432/dff1rjrnshphre";
            String user = "rtzxixexiyefib";
            String password = "9fb8dcaf0a2c979fbd8022a97d8cd9c6c7a97eb6f76588edbe9255b8b6c34936";
            result = DriverManager.getConnection(url, user, password);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }
}


