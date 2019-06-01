package Jersey.persistence;
import Jersey.Country;

import java.util.List;

public interface CountryDao {
    boolean save(Country country);
    List<Country> findall();
    Country findByCode(String code);
    List<Country> findBy10LargestPopulations();
    List<Country> find10largestSurfaces();
    boolean update(Country country);
    boolean delete(Country country);
}
