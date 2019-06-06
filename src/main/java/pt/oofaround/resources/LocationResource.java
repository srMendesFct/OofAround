package pt.oofaround.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pt.oofaround.support.JsonArraySupport;
import pt.oofaround.support.MediaSupport;
import pt.oofaround.util.AuthToken;
import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.LocationData;

@Path("/location")
@Produces(MediaType.APPLICATION_JSON)
public class LocationResource {

	private static final Logger LOG = Logger.getLogger(LocationResource.class.getName());

	private final Gson g = new Gson();

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	public LocationResource() {
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createLocation(LocationData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "createLocation")) {
			CollectionReference locations = db.collection("locations");
			Query query = locations.whereEqualTo("name", data.name);

			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				return Response.status(420).build(); // name
			}

			Map<String, Object> docData = new HashMap();
			docData.put("name", data.name);
			docData.put("description", data.description);
			docData.put("address", data.address);
			docData.put("latitude", data.latitude);
			docData.put("longitude", data.longitude);
			docData.put("category", data.category);
			docData.put("region", data.region);
			docData.put("score", data.score); // calculate score
			docData.put("nbrVisits", 0);

			ApiFuture<WriteResult> newLocation = locations.document(data.name).set(docData);
			MediaSupport.uploadImage(data.name, data.image);
			AuthToken at = new AuthToken(data.usernameR, data.role);
			JsonObject token = new JsonObject();
			token.addProperty("tokenID", at.tokenID);

			return Response.ok(g.toJson(token)).build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}

	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLocation(LocationData data) throws InterruptedException, ExecutionException {

		LOG.fine("Getting location" + data.name);

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getLocation")) {
			CollectionReference locations = db.collection("locations");
			Query query = locations.whereEqualTo("name", data.name);

			ApiFuture<QuerySnapshot> querySnapshot = query.get();
			JsonObject res = new JsonObject();
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				res.addProperty("name", document.getString("name"));
				res.addProperty("description", document.getString("description"));
				res.addProperty("address", document.getString("address"));
				res.addProperty("latitude", document.getString("latitude"));
				res.addProperty("longitude", document.getString("longitude"));
				res.addProperty("category", document.getString("category"));
				res.addProperty("region", document.getString("region"));
				res.addProperty("score", document.getLong("score"));
				res.addProperty("nbrVisits", document.getLong("nbrVisits"));
				/*
				 * if ((int) document.get("nbrRates") == 0) res.addProperty("rating", 0); else {
				 * double rate = (document.getLong("oneStar") * 1 + document.getLong("twoStar")
				 * * 2 + document.getLong("threeStar") * 3 + document.getLong("fourStar") * 4 +
				 * document.getLong("fiveStar") * 5) / document.getLong("nbrRates");
				 * DecimalFormat df = new DecimalFormat("#.#"); res.addProperty("rating",
				 * df.format(rate)); }
				 */
			}

			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.addProperty("tokenID", at.tokenID);

			return Response.ok(g.toJson(res)).build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}

	@POST
	@Path("/getcategoryregion")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLocationsByFCatAndRegion(LocationData data) throws InterruptedException, ExecutionException {

		LOG.fine("Getting category" + data.name);

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getLocationsByCatAndRegion")) {
			CollectionReference locations = db.collection("locations");
			Query query;
			ApiFuture<QuerySnapshot> querySnapshot;
			List<QueryDocumentSnapshot> docs;
			JsonObject res = new JsonObject();

			try {

				if (data.lastName.equalsIgnoreCase("")) {

					if (data.category.equalsIgnoreCase("") && data.region.equalsIgnoreCase("")) {

						query = locations; // .order by ranking quando ranking for implementado

					} else if (data.category.equalsIgnoreCase("")) {

						query = locations.whereEqualTo("region", data.region);

					} else if (data.region.equalsIgnoreCase("")) {

						query = locations.whereEqualTo("category", data.category);

					} else {

						query = locations.whereEqualTo("region", data.region).whereEqualTo("region", data.region);

					}

					querySnapshot = query.get();
					docs = querySnapshot.get().getDocuments();
					res.add("locations", JsonArraySupport.createLocationPropArray(docs, "name", "description",
							"address", "latitude", "longitude", "category", "region", "nbrVisits", "score"));

				} else {

					// TODO para varios
					query = locations.whereEqualTo("name", data.lastName);
					querySnapshot = query.get();
					docs = querySnapshot.get().getDocuments();
					QueryDocumentSnapshot lastDoc = docs.get(0);

					if (data.category.equalsIgnoreCase("") && data.region.equalsIgnoreCase("")) {

						query = locations.orderBy("nbrVisits"); // .order by ranking quando ranking for implementado

					} else if (data.category.equalsIgnoreCase("")) {

						query = locations.whereEqualTo("region", data.region).startAfter(lastDoc).limit(data.limit);

					} else if (data.region.equalsIgnoreCase("")) {

						query = locations.whereEqualTo("category", data.category).startAfter(lastDoc).limit(data.limit);

					} else {

						query = locations.whereEqualTo("region", data.region).whereEqualTo("region", data.region)
								.startAfter(lastDoc).limit(data.limit);

					}

					querySnapshot = query.get();
					docs = querySnapshot.get().getDocuments();
					res.add("locations", JsonArraySupport.createLocationPropArray(docs, "name", "description",
							"address", "latitude", "longitude", "category", "region", "nbrVisits", "score"));

				}
				AuthToken at = new AuthToken(data.usernameR, data.role);
				res.addProperty("tokenID", at.tokenID);

				return Response.ok(g.toJson(res)).build();
			} catch (Exception e) {
				String s = "";
				for (StackTraceElement ss : e.getStackTrace()) {
					s += "   " + ss.toString();
				}
				return Response.status(Status.FORBIDDEN).entity(s).build();
			}
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

	@SuppressWarnings("unused")
	@POST
	@Path("/rate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response visitLocation(LocationData data) throws InterruptedException, ExecutionException {

		// Get location info and update nbr of times visited

		CollectionReference locations = db.collection("locations");
		Query query = locations.whereEqualTo("name", data.name);

		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		Long scoreGainned = null;

		Long nbrVisits = null;

		DocumentReference docRef = null;

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			scoreGainned = document.getLong("score");
			docRef = document.getReference();
			nbrVisits = document.getLong("nbrVisits") + 1;
		}

		// Get user info and update his score

		ApiFuture<WriteResult> future = docRef.update("nbrVisits", nbrVisits);

		WriteResult result = future.get();

		CollectionReference users = db.collection("users");
		query = users.whereEqualTo("username", data.usernameR);

		querySnapshot = query.get();

		Long oldScore = null;

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			docRef = document.getReference();
			oldScore = document.getLong("score");
		}

		oldScore += scoreGainned;

		future = docRef.update("score", oldScore);

		result = future.get();

		return Response.ok().entity(g.toJson(scoreGainned)).build();

	}

	

}
