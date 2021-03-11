package foo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;


@WebServlet(name = "Petsignature", urlPatterns = { "/signerpet" })
public class SignerPetition extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print("<h2> Signature d'une pétition </h2>");

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key k = KeyFactory.createKey("Petition", 5631671361601536L);
        Query q = new Query("Petition").setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, k));

        PreparedQuery pq = datastore.prepare(q);
        List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());

        Entity p = new Entity("Petition");

        Transaction txn = datastore.beginTransaction();
        
        try {
            p = datastore.get(k);
            for (Entity entity : result) {
                ArrayList<String> fset = (ArrayList<String>) entity.getProperty("idSignataire");
                fset.add("PEPELOL");
                p.setProperty("idSignataire", fset);
            }
            
            long cpt = (long) p.getProperty("nbSignature");
            p.setProperty("nbSignature", cpt + 1);
            datastore.put(p);
            txn.commit();

        } catch (EntityNotFoundException e) {
            response.getWriter().print("<li> ERROR <li>"); 
        } finally {
        	if (txn.isActive()) {
        		txn.rollback();
        	}
        }

        /*
        Entity p = new Entity("Petition", 5634161670881280L);

        for (Entity entity : result) {
            p.setProperty("titre", entity.getProperty("titre"));
            p.setProperty("probleme", entity.getProperty("probleme"));
            p.setProperty("dateC", entity.getProperty("dateC"));
            p.setProperty("etat", entity.getProperty("etat"));
            p.setProperty("idAuteur", entity.getProperty("idAuteur"));

            //HashSet<Integer> fset = (HashSet<Integer>) entity.getProperty("idSignataire");
            ArrayList<Integer> fset = (ArrayList<Integer>) entity.getProperty("idSignataire");
            fset.add(25);

            p.setProperty("idSignataire", fset);
        }
        datastore.put(p);*/
	}
}
