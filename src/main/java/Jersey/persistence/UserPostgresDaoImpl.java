package Jersey.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserPostgresDaoImpl extends PostgresBaseDao implements UserDao{
    public String findRoleForUser(String name, String pass){
        PostgresBaseDao pgdao = new PostgresBaseDao();
        try(Connection conn = pgdao.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT role FROM useraccount WHERE username = '%s' AND password = '%s'", name, pass));
            if(rs.next()){
                return rs.getString(1);
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
