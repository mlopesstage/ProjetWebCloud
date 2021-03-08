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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@WebServlet(name = "PSigneesServlet", urlPatterns = { "/petitionssignees" })
public class AfficherPetitionsSignees extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");


		response.getWriter().print("<h2> Afficher les pétitions signées </h2>");

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Petition").setFilter(new FilterPredicate("idSignataire", FilterOperator.EQUAL, 14));
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());

		response.getWriter().print("<li> result:" + result.size() + "<br>");
		for (Entity entity : result) {
			response.getWriter().print("<li>" + entity.getProperty("titre") + entity.getProperty("probleme") 
			+ entity.getProperty("idAuteur") + entity.getProperty("idSignataire"));
		}
	}
}
