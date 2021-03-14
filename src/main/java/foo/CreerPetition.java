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
		
		//Date date = new Date();
		//SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		
		Random r = new Random();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// Create petition
		for (int i = 0; i < 250; i++) {
			//Entity p = new Entity("Petition", Long.MAX_VALUES-(formatter.format(date)+":"+User.getEmail()));
			Random d = new Random();
			int date1 = 1 + d.nextInt(28);
			int date2 = 1 + d.nextInt(12);
			int date3 = 2017 + d.nextInt(3);
			int date4 = d.nextInt(23);
			int date5 = d.nextInt(59);
			int date6 = d.nextInt(59);
			String owner = "U"+r.nextInt(1000);
			Entity p = new Entity("Petition", date1+""+date2+""+date3+""+date4+""+date5+""+date6+""+owner);
			p.setProperty("titre", "Contre la COVID" + i);
			p.setProperty("probleme", "La miage c'est difficile"+i);		
			String datelocale = date1+"/"+date2+"/"+date3+" "+date4+":"+date5+":"+date6;
			p.setProperty("dateC", datelocale);
			//p.setProperty("dateC", formatter.format(date));
			p.setProperty("etat", "Ouverte");
			p.setProperty("idAuteur", owner);
			HashSet<String> pset = new HashSet<String>();
			for(int j = 0;j<200;j++)  {
				pset.add("U" + r.nextInt(1000));
			}
			p.setProperty("idSignataire", pset);
			p.setProperty("nbSignature", pset.size());
			HashSet<String> ftags = new HashSet<String>();
			for(int k = 0;k<3;k++)  {
				ftags.add("T" + r.nextInt(20));
			}
			p.setProperty("tags", ftags);
			datastore.put(p);
			response.getWriter().print("<li> created post:" + p.getKey() + "<br>");
		}
		
	}
}
