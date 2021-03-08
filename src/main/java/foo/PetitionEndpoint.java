package foo;
import java.util.List;
import java.util.Random;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;


@Api(name = "myApi",
     version = "v1",
     audiences = "46330245209-7273ldijeh5upu6koeml6d3roehfooss.apps.googleusercontent.com",
  	 clientIds = "46330245209-7273ldijeh5upu6koeml6d3roehfooss.apps.googleusercontent.com",
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
		Query q = new Query("Petition").setFilter(new FilterPredicate("idSignataire", FilterOperator.EQUAL, 14));
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		return result;
	}
}