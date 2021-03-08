package foo;
import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

@WebServlet(name = "PetTop100", urlPatterns = { "/toppet" })
public class AfficherTopPetitions extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print("<h2> Voici le top 100 des meilleures pétitions triées par date </h2>");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		//Key k = KeyFactory.createKey("Petition", 5637476211228672L);
		Query q = new Query("Petition").addSort("idSignataire", SortDirection.DESCENDING);

		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withLimit(3));

		//response.getWriter().print("<li> result:" + result.size() + "<br>"
	
		response.getWriter().print("<li> result:" + result.size() + "<br>");
		for (Entity entity : result) {
			response.getWriter().print("<li>" + entity.getProperty("dateC") + entity.getProperty("titre") + entity.getProperty("probleme") 
			+ entity.getProperty("idAuteur") + entity.getProperty("idSignataire"));
		}
		
	}
}
