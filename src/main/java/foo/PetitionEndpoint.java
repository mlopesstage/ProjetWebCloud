package foo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;


@Api(name = "myApi",
     version = "v1",
     audiences = "46330245209-9jejvn58229dabq46josvavn3crj9ogl.apps.googleusercontent.com",
  	 clientIds = "46330245209-9jejvn58229dabq46josvavn3crj9ogl.apps.googleusercontent.com",
  	 scopes = "https://www.googleapis.com/auth/userinfo.profile",
  			
     namespace =
     @ApiNamespace(
		   ownerDomain = "helloworld.example.com",
		   ownerName = "helloworld.example.com",
		   packagePath = "")
     )

public class PetitionEndpoint {

	@ApiMethod(name = "signedpet", httpMethod = HttpMethod.GET)
	public CollectionResponse<Entity> signedpet(@Named("mail") String mail, @Nullable @Named("next") String cursorString) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Petition").setFilter(new FilterPredicate("idSignataire", FilterOperator.EQUAL, mail));
		PreparedQuery pq = datastore.prepare(q);
		
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10);

		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}

		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		cursorString = results.getCursor().toWebSafeString();

		return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
	}
	
	@ApiMethod(name = "mycreatedpet", httpMethod = HttpMethod.GET)
	public CollectionResponse<Entity> mycreatedpet(@Named("mail") String mail, @Nullable @Named("next") String cursorString) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Petition").setFilter(new FilterPredicate("idAuteur", FilterOperator.EQUAL, mail));
		PreparedQuery pq = datastore.prepare(q);
		
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10);

		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}

		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		cursorString = results.getCursor().toWebSafeString();

		return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
	}
	
	@ApiMethod(name = "toppetition", httpMethod = HttpMethod.GET)
	public CollectionResponse<Entity> toppetition(@Nullable @Named("next") String cursorString) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Petition").addSort("nbSignature", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10);

		if (cursorString != null) {
			fetchOptions.startCursor(Cursor.fromWebSafeString(cursorString));
		}

		QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
		cursorString = results.getCursor().toWebSafeString();

		return CollectionResponse.<Entity>builder().setItems(results).setNextPageToken(cursorString).build();
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
	public Entity signerpet(@Named("mail") String mail, @Named("id") String id) {
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
                fset.add(mail);
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
	public Entity creerpetition(CreerPetition cp) {

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Entity p = new Entity("Petition", Long.MAX_VALUE-(new Date()).getTime()+":"+cp.owner);
		p.setProperty("dateC", formatter.format(date));
		p.setProperty("etat", "Ouverte");
		HashSet<String> pset = new HashSet<String>();		
		pset.add("");
		p.setProperty("idSignataire", pset);
		p.setProperty("idAuteur", cp.owner);
		p.setProperty("nbSignature", 0);
		p.setProperty("probleme", cp.petProbleme);
		HashSet<String> ftags = new HashSet<String>(); //a supp
		ftags.add(""); //a supp
		p.setProperty("tags", ftags); //a supp
		p.setProperty("titre", cp.petName);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		datastore.put(p);
		txn.commit();
		return p;
	}
}