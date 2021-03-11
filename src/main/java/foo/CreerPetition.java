package foo;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;


@WebServlet(name = "PCreationServlet", urlPatterns = { "/creerpetition" })
public class CreerPetition extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		
		Random r = new Random();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// Create petition
		for (int i = 0; i < 5; i++) {
			int owner = r.nextInt(100);
			Entity p = new Entity("Petition");
			p.setProperty("titre", "Kirikou" + i);
			p.setProperty("probleme", "gémalofess"+i);
			p.setProperty("dateC", formatter.format(date));
			p.setProperty("etat", true);
			p.setProperty("idAuteur", "U"+owner);
			HashSet<String> pset = new HashSet<String>();
			for (int j = 0; j < 10; j++) {
				pset.add("U" + r.nextInt(100));
			}
			p.setProperty("idSignataire", pset);
			p.setProperty("nbSignature", pset.size());
			datastore.put(p);
			response.getWriter().print("<li> created post:" + p.getKey() + "<br>");
		}
	}
}
