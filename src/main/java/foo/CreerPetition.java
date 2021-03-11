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
		for (int i = 0; i < 10; i++) {
			//Entity p = new Entity("Petition", Long.MAX_VALUES-(formatter.format(date)+":"+User.getEmail()));
			Entity p = new Entity("Petition");
			int owner = r.nextInt(1000);
			p.setProperty("titre", "Kirikou" + i);
			p.setProperty("probleme", "gémalofess"+i);
			Random d = new Random();
			int date1 = 1 + d.nextInt(28);
			String date2 = date1 + "/02/2021";
			p.setProperty("dateC", date2);
			//p.setProperty("dateC", formatter.format(date));
			p.setProperty("etat", "Ouverte");
			p.setProperty("idAuteur", "U"+owner);
			HashSet<String> pset = new HashSet<String>();
			for(int j = 0;j<200;j++)  {
				pset.add("U" + r.nextInt(1000));
			}
			p.setProperty("idSignataire", pset);
			p.setProperty("nbSignature", pset.size());
			HashSet<String> ftags = new HashSet<String>();
			for(int k = 0;k<10;k++)  {
				ftags.add("T" + r.nextInt(20));
			}
			p.setProperty("tags", ftags);
			datastore.put(p);
			response.getWriter().print("<li> created post:" + p.getKey() + "<br>");
		}
		
	}
}
