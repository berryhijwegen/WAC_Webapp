package Jersey.persistence;

import Jersey.Country;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryPostgresDaoImpl extends PostgresBaseDao implements CountryDao {

    @Override
    public boolean save(Country country) {
        boolean result = false;
        PostgresBaseDao pgdao = new PostgresBaseDao();
        try(Connection conn = pgdao.getConnection()){
            String query = "INSERT INTO Country (code, iso3, name, capital, continent, region, surfacearea, population, governmentform, latitude, longitude)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, country.getCode());
            pst.setString(2, country.getIso3());
            pst.setString(3, country.getName());
            pst.setString(4, country.getCapital());
            pst.setString(5, country.getContinent());
            pst.setString(6, country.getRegion());
            pst.setDouble(7, country.getSurface());
            pst.setInt(8, country.getPopulation());
            pst.setString(9, country.getGovernment());
            pst.setDouble(10, country.getLatitude());
            pst.setDouble(11, country.getLongitude());
            pst.executeUpdate();
            result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Country> findall() {
        ArrayList<Country> tempCountries = new ArrayList<Country>();
        PostgresBaseDao pgdao = new PostgresBaseDao();
        try(Connection conn = pgdao.getConnection()){
            System.out.println("123");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT code, iso3, name, capital, continent, region, surfacearea, population, governmentform, latitude, longitude FROM country ORDER by name ASC");
            while(rs.next()){
                Country tempCountry = new Country();
                tempCountry.setCode(rs.getString(1));
                tempCountry.setIso3(rs.getString(2));
                tempCountry.setName(rs.getString(3));
                tempCountry.setCapital(rs.getString(4));
                tempCountry.setContinent(rs.getString(5));
                tempCountry.setRegion(rs.getString(6));
                tempCountry.setSurface(rs.getFloat(7));
                tempCountry.setPopulation(rs.getInt(8));
                tempCountry.setGovernment(rs.getString(9));
                tempCountry.setLatitude(rs.getFloat(10));
                tempCountry.setLongitude(rs.getFloat(11));

                tempCountries.add(tempCountry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempCountries;
    }

    @Override
    public Country findByCode(String code) {
        Country tempCountry = new Country();
        PostgresBaseDao pgdao = new PostgresBaseDao();
        try(Connection conn = pgdao.getConnection()){
            Statement stmt = conn.createStatement();
            System.out.println(code);
            ResultSet rs = stmt.executeQuery("SELECT code, iso3, name, capital, continent, region, surfacearea, population, governmentform, latitude, longitude " +
                                            "FROM COUNTRY " +
                                            "WHERE code = '" + code + "'");
            rs.next();
            tempCountry.setCode(rs.getString(1));
            tempCountry.setIso3(rs.getString(2));
            tempCountry.setName(rs.getString(3));
            tempCountry.setCapital(rs.getString(4));
            tempCountry.setContinent(rs.getString(5));
            tempCountry.setRegion(rs.getString(6));
            tempCountry.setSurface(rs.getFloat(7));
            tempCountry.setPopulation(rs.getInt(8));
            tempCountry.setGovernment(rs.getString(9));
            tempCountry.setLatitude(rs.getFloat(10));
            tempCountry.setLongitude(rs.getFloat(11));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempCountry;
    }

    @Override
    public List<Country> findBy10LargestPopulations() {
        ArrayList<Country> tempCountries = new ArrayList<Country>();
        PostgresBaseDao pgdao = new PostgresBaseDao();
        try(Connection conn = pgdao.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT code, iso3, name, capital, continent, region, surfacearea, population, governmentform, latitude, longitude " +
                                             "FROM COUNTRY " +
                                             "ORDER BY population " +
                                             "LIMIT 10");
            while(rs.next()){
                Country tempCountry = new Country();
                tempCountry.setCode(rs.getString(1));
                tempCountry.setIso3(rs.getString(2));
                tempCountry.setName(rs.getString(3));
                tempCountry.setCapital(rs.getString(4));
                tempCountry.setContinent(rs.getString(5));
                tempCountry.setRegion(rs.getString(6));
                tempCountry.setSurface(rs.getFloat(7));
                tempCountry.setPopulation(rs.getInt(8));
                tempCountry.setGovernment(rs.getString(9));
                tempCountry.setLatitude(rs.getFloat(10));
                tempCountry.setLongitude(rs.getFloat(11));

                tempCountries.add(tempCountry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempCountries;
    }

    @Override
    public List<Country> find10largestSurfaces() {
        ArrayList<Country> tempCountries = new ArrayList<Country>();
        PostgresBaseDao pgdao = new PostgresBaseDao();
        try(Connection conn = pgdao.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT code, iso3, name, capital, continent, region, surfacearea, population, governmentform, latitude, longitude " +
                    "FROM COUNTRY " +
                    "ORDER BY surfacearea " +
                    "LIMIT 10");
            while(rs.next()){
                Country tempCountry = new Country();
                tempCountry.setCode(rs.getString(1));
                tempCountry.setIso3(rs.getString(2));
                tempCountry.setName(rs.getString(3));
                tempCountry.setCapital(rs.getString(4));
                tempCountry.setContinent(rs.getString(5));
                tempCountry.setRegion(rs.getString(6));
                tempCountry.setSurface(rs.getFloat(7));
                tempCountry.setPopulation(rs.getInt(8));
                tempCountry.setGovernment(rs.getString(9));
                tempCountry.setLatitude(rs.getFloat(10));
                tempCountry.setLongitude(rs.getFloat(11));

                tempCountries.add(tempCountry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempCountries;
    }

    @Override
    public boolean update(Country country) {
        boolean result = false;
        PostgresBaseDao pgdao = new PostgresBaseDao();
        try(Connection conn = pgdao.getConnection()){
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Country " +
                    "SET iso3 = '" + country.getIso3() + "'" +
                    ", name = '" + country.getName() + "'" +
                    ", capital = '" + country.getCapital() + "'" +
                    ", continent = '" + country.getContinent() + "'" +
                    ", region = '" + country.getRegion() + "'" +
                    ", surfacearea = " + country.getSurface() +
                    ", population = " + country.getPopulation() +
                    ", governmentform = '" + country.getGovernment() + "'" +
                    ", latitude = " + country.getLatitude() +
                    ", longitude = " + country.getLongitude() +
                    " WHERE code = '" + country.getCode() + "'" );
            result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(Country country) {
        boolean result = false;
        PostgresBaseDao pgdao = new PostgresBaseDao();
        try(Connection conn = pgdao.getConnection()){
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM Country WHERE code = '" + country.getCode() + "'");
            result = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
