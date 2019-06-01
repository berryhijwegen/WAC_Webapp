package Jersey;

import Jersey.persistence.CountryPostgresDaoImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WorldService {
	CountryPostgresDaoImpl cpgdao = new CountryPostgresDaoImpl();
	public List<Country> getAllCountries() {
		return cpgdao.findall();
	}
	
	public List<Country> get10LargestPopulations() {
		return cpgdao.findBy10LargestPopulations();
	}

	public List<Country> get10LargestSurfaces() {
		return cpgdao.find10largestSurfaces();
	}
	
	public Country getCountryByCode(String code) {
		return cpgdao.findByCode(code);
	}
}
