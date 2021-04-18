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


@WebServlet(name = "PCreationServlet", urlPatterns = { "/creerpetitionasupp" })
public class CreerPetitionASuppALaFin extends HttpServlet{

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		Random r = new Random();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			String owner = "UserMail"+r.nextInt(250)+"@gmail.com";
			Entity p = new Entity("Petition", Long.MAX_VALUE-(new Date().getTime())+":"+owner);
			p.setProperty("titre", "Pétition " + r.nextInt(1000) + " pour une bonne note");
			p.setProperty("probleme", "Nous voudrions une bonne note dans ce projet pour cette pétition n°"+ r.nextInt(1000));
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			p.setProperty("dateC", formatter.format(date));
			p.setProperty("etat", "Ouverte");
			p.setProperty("idAuteur", owner);
			HashSet<String> pset = new HashSet<String>();
			for(int j = 0;j<200;j++)  {
				pset.add("UserMail" + r.nextInt(250)+"@gmail.com");
			}
			p.setProperty("idSignataire", pset);
			p.setProperty("nbSignature", pset.size());
			datastore.put(p);
			response.getWriter().print("<li> created post:" + p.getKey() + "<br>");
	}
}