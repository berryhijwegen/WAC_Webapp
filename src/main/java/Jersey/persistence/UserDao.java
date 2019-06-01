package Jersey.persistence;
import Jersey.User;

public interface UserDao {
    String findRoleForUser(String name, String pass);
}
