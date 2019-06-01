package Jersey;

import Jersey.persistence.CountryPostgresDaoImpl;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/countries")
public class WorldResource {
    CountryPostgresDaoImpl cpgdao = new CountryPostgresDaoImpl();
    WorldService ws = ServiceProvider.getWorldService();
    @GET
    @Produces("application/json")
    public Response getCountries(){
        List<Country> allCountries = ws.getAllCountries();
        return Response.status(200).entity(allCountries).build();
    }

    @RolesAllowed("user")
    @POST
    @Produces("application/json")
    public Response createCountry(@FormParam("code") String code,
                                  @FormParam("iso3") String iso3,
                                  @FormParam("name") String nm,
                                  @FormParam("continent") String cont,
                                  @FormParam("governmentform") String governmentform,
                                  @FormParam("capital") String cpt,
                                  @FormParam("region") String rgn,
                                  @FormParam("surface") Double sfc,
                                  @FormParam("population") int ppl
                                  ){
        Country newCountry = new Country();
        newCountry.setCode(code);
        newCountry.setIso3(iso3);
        newCountry.setContinent(cont);
        newCountry.setGovernment(governmentform);
        newCountry.setCapital(cpt);
        newCountry.setRegion(rgn);
        newCountry.setSurface(sfc);
        newCountry.setPopulation(ppl);
        newCountry.setName(nm);

        Map<String, String> messages = new HashMap<String, String>();
        if(cpgdao.save(newCountry)){
            messages.put("successful", "Country added");

            return Response.status(200).entity(messages).build();
        }
        messages.put("error", "Country not added");
        return Response.status(409).entity(messages).build();
    }

    @Path("largestsurfaces")
    @GET
    @Produces("application/json")
    public Response getLargestSurface(){
        List<Country> returnSurfaces = ws.get10LargestSurfaces();
        return Response.status(200).entity(returnSurfaces).build();
    }

    @Path("largestpopulations")
    @GET
    @Produces("application/json")
    public Response getLargestPopulations(){
        List<Country> returnPopulations = ws.get10LargestPopulations();
        return Response.status(200).entity(returnPopulations).build();
    }

    @Path("{cc}")
    @GET
    @Produces("application/json")
    public Response getCountryByCode(@PathParam("cc") String countryCode){
        Country country = ws.getCountryByCode(countryCode.toUpperCase());
        if(country.getName() == null){
            Map<String, String> messages = new HashMap<String, String>();
            messages.put("error", "Country does not exist!");

            return Response.status(409).entity(messages).build();
        }
        return Response.status(200).entity(country).build();

    }
    @RolesAllowed("user")
    @Path("{cc}")
    @DELETE
    @Produces("application/json")
    public Response deleteCountry(@PathParam("cc") String countryCode){
        Map<String, String> messages = new HashMap<String, String>();
        if(cpgdao.delete(cpgdao.findByCode(countryCode.toUpperCase()))){
            messages.put("successful", "Country deleted");

            return Response.status(200).entity(messages).build();
        }
        messages.put("error", "Country not found");
        return Response.status(409).entity(messages).build();
    }
    @RolesAllowed("user")
    @Path("{cc}")
    @PUT
    @Produces("application/json")
    public Response updateCountry(@PathParam("cc") String countryCode,
                                  @FormParam("name") String nm,
                                  @FormParam("capital") String cpt,
                                  @FormParam("region") String rgn,
                                  @FormParam("surface") Double sfc,
                                  @FormParam("population") int ppl){

        Country countryToUpdate = cpgdao.findByCode(countryCode);
        countryToUpdate.setName(nm);
        countryToUpdate.setCapital(cpt);
        countryToUpdate.setRegion(rgn);
        countryToUpdate.setSurface(sfc);
        countryToUpdate.setPopulation(ppl);

        Map<String, String> messages = new HashMap<String, String>();
        if(cpgdao.update(countryToUpdate)){
            messages.put("successful", "Country updated");

            return Response.status(200).entity(messages).build();
        }
        messages.put("error", "Country not found");
        return Response.status(409).entity(messages).build();
    }
}