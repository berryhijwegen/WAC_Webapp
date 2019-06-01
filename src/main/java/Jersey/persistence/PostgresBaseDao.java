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
//            InitialContext ic = new InitialContext();
//            DataSource datasource = (DataSource) ic.lookup("java:comp/env/jdbc/PostgresDS");
//
//            result = datasource.getConnection();
            String url = "jdbc:postgresql://localhost:5432/worlddb";
            String user = "postgres";
            String password = "123";
            result = DriverManager.getConnection(url, user, password);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }
}


