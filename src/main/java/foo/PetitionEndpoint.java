package foo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;


@Api(name = "myApi",
     version = "v1",
     audiences = "232742472331-2f32mq8k3pbenacl44usnrf37362vo4t.apps.googleusercontent.com",
  	 clientIds = "232742472331-2f32mq8k3pbenacl44usnrf37362vo4t.apps.googleusercontent.com",
  	 scopes = "https://www.googleapis.com/auth/userinfo.profile",
  			
     namespace =
     @ApiNamespace(
		   ownerDomain = "helloworld.example.com",
		   ownerName = "helloworld.example.com",
		   packagePath = "")
     )

public class PetitionEndpoint {

	@ApiMethod(name = "signedpet", httpMethod = HttpMethod.GET)
	public List<Entity> signedpet() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Petition").setFilter(new FilterPredicate("idSignataire", FilterOperator.EQUAL, "mica-lopes@hotmail.fr"));
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		return result;
	}
	
	@ApiMethod(name = "mycreatedpet", httpMethod = HttpMethod.GET)
	public List<Entity> mycreatedpet() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Petition").setFilter(new FilterPredicate("idAuteur", FilterOperator.EQUAL, "mica-lopes@hotmail.fr"));
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		return result;
	}
	
	@ApiMethod(name = "toppetition", httpMethod = HttpMethod.GET)
	public List<Entity> toppetition() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Petition").addSort("nbSignature", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withLimit(100));
		return result;
	}
	
	@ApiMethod(name = "detailpet", httpMethod = HttpMethod.GET)
	public List<Entity> detailpet(@Named("id") String id) {
		//long idl = Long.parseLong(id);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key k = KeyFactory.createKey("Petition", id);
		Query q = new Query("Petition").setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, k));
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		return result;
	}
	
	@ApiMethod(name = "signerpet", httpMethod = HttpMethod.GET)
	public Entity signerpet(@Named("id") String id) {
		//long idl = Long.parseLong(id);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key k = KeyFactory.createKey("Petition", id);
        Query q = new Query("Petition").setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, k));

        PreparedQuery pq = datastore.prepare(q);
        List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());

        Entity p = new Entity("Petition");

        Transaction txn = datastore.beginTransaction();

        try {
            p = datastore.get(k);
            for (Entity entity : result) {
                ArrayList<String> fset = (ArrayList<String>) entity.getProperty("idSignataire");
                fset.add("mica-lopes@hotmail.fr");
                p.setProperty("idSignataire", fset);
            }

            long cpt = (long) p.getProperty("nbSignature");
            p.setProperty("nbSignature", cpt + 1);
            datastore.put(p);
            txn.commit();

        } catch (EntityNotFoundException e) {
        	System.out.println("<li> ERROR <li>"); 
        } finally {

            if (txn.isActive()) {
                txn.rollback();
            }
        }
		return p;
	}
	
	@ApiMethod(name = "fermerpet", httpMethod = HttpMethod.GET)
	public Entity fermerpet(@Named("id") String id) {
		//long idl = Long.parseLong(id);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key k = KeyFactory.createKey("Petition", id);
        Query q = new Query("Petition").setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, k));

        PreparedQuery pq = datastore.prepare(q);
        List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());

        Entity p = new Entity("Petition");

        try {
            p = datastore.get(k);                                 
            p.setProperty("etat", "Fermée");                      
            datastore.put(p);
        } catch (EntityNotFoundException e) {
        	System.out.println("<li> ERROR <li>"); 
        }
		return p;
	}
	
	@ApiMethod(name = "creerpetition", httpMethod = HttpMethod.POST)
	public Entity creerpetition(User user, CreerPetition1 cp) {

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		//Entity p = new Entity("Petition", Long.MAX_VALUE-(new Date()).getTime()+":"+user.getEmail());
		Entity p = new Entity("Petition", Long.MAX_VALUE-(new Date()).getTime()+":"+"U1001");
		p.setProperty("dateC", formatter.format(date));
		p.setProperty("etat", "Ouverte");
		HashSet<String> pset = new HashSet<String>();		
		pset.add(""); //A SUP
		p.setProperty("idSignataire", pset);
		p.setProperty("idAuteur","mica-lopes@hotmail.fr");
		//p.setProperty("idAuteur", user.getEmail());
		p.setProperty("nbSignature", 0);
		p.setProperty("probleme", cp.petProbleme);
		HashSet<String> ftags = new HashSet<String>();
		p.setProperty("tags", ftags);
		p.setProperty("titre", cp.petName);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		datastore.put(p);
		txn.commit();
		return p;
	}
}