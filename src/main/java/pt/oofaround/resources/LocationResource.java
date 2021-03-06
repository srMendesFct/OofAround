package pt.oofaround.resources;

import java.util.HashMap;
import java.util.LinkedList;
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

import org.json.JSONArray;
import org.json.JSONObject;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.oofaround.support.JsonArraySupport;
import pt.oofaround.support.MediaSupport;
import pt.oofaround.util.AuthToken;
import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.GuestData;
import pt.oofaround.util.LocationData;
import pt.oofaround.util.TokenData;

@Path("/location")
@Produces(MediaType.APPLICATION_JSON)
public class LocationResource {

	private static final Logger LOG = Logger.getLogger(LocationResource.class.getName());

	private final Gson g = new Gson();

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	public LocationResource() {
	}

	@SuppressWarnings({ "unused" })
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

			Map<String, Object> docData = new HashMap<String, Object>();
			docData.put("name", data.name);
			docData.put("description", data.description);
			docData.put("address", data.address);
			docData.put("latitude", data.latitude);
			docData.put("longitude", data.longitude);
			docData.put("category", data.category);
			docData.put("region", data.region);
			docData.put("score", data.score); // calculate score
			docData.put("nbrVisits", 0);
			docData.put("placeID", data.placeID);

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
				res.addProperty("placeID", document.getString("placeID"));
			}

			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.addProperty("tokenID", at.tokenID);

			return Response.ok(g.toJson(res)).build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}

	@POST
	@Path("/guest/get")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getGuestLocation(GuestData data) throws InterruptedException, ExecutionException {

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
			res.addProperty("placeID", document.getString("placeID"));
		}

		return Response.ok(g.toJson(res)).build();
	}

	@POST
	@Path("/getfromcoord")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getFromCoordinates(LocationData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getFromCoordinates")) {
			CollectionReference locations = db.collection("locations");
			Query query = locations.whereEqualTo("latitude", data.latitude).whereEqualTo("longitude", data.longitude);

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
				// res.addProperty("nbrVisits", document.getLong("nbrVisits"));
				res.addProperty("placeID", document.getString("placeID"));
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
	public Response getLocationsByCatAndRegion(LocationData data) throws InterruptedException, ExecutionException {

		LOG.fine("Getting category" + data.name);

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getLocationsByCatAndRegion")) {
			CollectionReference locations = db.collection("locations");
			List<QueryDocumentSnapshot> docs;
			JSONObject res = new JSONObject();

			try {

				if (data.lastName.equalsIgnoreCase("")) {

					if (data.categoriesGet[0].equalsIgnoreCase("") && data.region.equalsIgnoreCase("")) {

						// .order by ranking quando ranking for implementado
						docs = locations.get().get().getDocuments();

					} else if (data.categoriesGet[0].equalsIgnoreCase("")) {

						docs = locations.whereEqualTo("region", data.region).get().get().getDocuments();

					} else if (data.region.equalsIgnoreCase("")) {
						docs = new LinkedList<QueryDocumentSnapshot>();
						docs.addAll(
								locations.whereEqualTo("category", data.categoriesGet[0]).get().get().getDocuments());

						for (int i = 1; i < data.categoriesGet.length; i++) {

							docs.addAll(locations.whereEqualTo("category", data.categoriesGet[i]).get().get()
									.getDocuments());

						}

					} else {
						docs = new LinkedList<QueryDocumentSnapshot>();
						docs.addAll(locations.whereEqualTo("region", data.region)
								.whereEqualTo("category", data.categoriesGet[0]).get().get().getDocuments());

						for (int i = 1; i < data.categoriesGet.length; i++) {
							docs.addAll(locations.whereEqualTo("region", data.region)
									.whereEqualTo("category", data.categoriesGet[i]).get().get().getDocuments());
						}

					}
					res.put("locations",
							JsonArraySupport.createLocationPropArray(docs, "name", "description", "address", "latitude",
									"longitude", "category", "region", "nbrVisits", "score", "image", "placeID",
									"region"));

				} else {

					docs = locations.whereEqualTo("name", data.lastName).get().get().getDocuments();
					QueryDocumentSnapshot lastDoc = docs.get(0);

					if (data.categoriesGet[0].equalsIgnoreCase("") && data.region.equalsIgnoreCase("")) {

						docs = locations.orderBy("nbrVisits").get().get().getDocuments();

					} else if (data.categoriesGet[0].equalsIgnoreCase("")) {

						docs = locations.orderBy("nbrVisits").whereEqualTo("region", data.region).startAfter(lastDoc)
								.limit(data.limit).get().get().getDocuments();

					} else if (data.region.equalsIgnoreCase("")) {
						docs = new LinkedList<QueryDocumentSnapshot>();
						docs.addAll(locations.whereEqualTo("category", data.categoriesGet[0]).startAfter(lastDoc)
								.limit(data.limit).get().get().getDocuments());
						for (int i = 1; i < data.categoriesGet.length; i++) {
							docs.addAll(locations.whereEqualTo("category", data.categoriesGet[i]).startAfter(lastDoc)
									.limit(data.limit).get().get().getDocuments());
						}

					} else {
						docs = new LinkedList<QueryDocumentSnapshot>();

						docs.addAll(locations.orderBy("nbrVisits").whereEqualTo("region", data.region)
								.whereEqualTo("category", data.categoriesGet[0]).startAfter(lastDoc).limit(data.limit)
								.get().get().getDocuments());
						for (int i = 1; i < data.categoriesGet.length; i++) {

							docs.addAll(locations.whereEqualTo("region", data.region).orderBy("nbrVisits")
									.whereEqualTo("region", data.region).whereEqualTo("category", data.categoriesGet[i])
									.startAfter(lastDoc).limit(data.limit).get().get().getDocuments());
						}

					}

					res.put("locations",
							JsonArraySupport.createLocationPropArray(docs, "name", "description", "address", "latitude",
									"longitude", "category", "region", "nbrVisits", "score", "image", "placeID",
									"region"));

				}
				AuthToken at = new AuthToken(data.usernameR, data.role);
				res.put("tokenID", at.tokenID);

				return Response.ok(res.toString()).build();
			} catch (Exception e) {
				String s = "";
				for (StackTraceElement ss : e.getStackTrace()) {
					s += "\n" + ss.toString();
				}
				return Response.status(Status.FORBIDDEN).entity(s).build();
			}
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

	@POST
	@Path("/guest/getcategoryregion")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getGuestLocationsByCatAndRegion(GuestData data) throws InterruptedException, ExecutionException {

		CollectionReference locations = db.collection("locations");
		List<QueryDocumentSnapshot> docs;
		JSONObject res = new JSONObject();

		try {

			if (data.lastName.equalsIgnoreCase("")) {

				if (data.categories[0].equalsIgnoreCase("") && data.region.equalsIgnoreCase("")) {

					// .order by ranking quando ranking for implementado
					docs = locations.get().get().getDocuments();

				} else if (data.categories[0].equalsIgnoreCase("")) {

					docs = locations.whereEqualTo("region", data.region).get().get().getDocuments();

				} else if (data.region.equalsIgnoreCase("")) {
					docs = new LinkedList<QueryDocumentSnapshot>();
					docs.addAll(locations.whereEqualTo("category", data.categories[0]).get().get().getDocuments());

					for (int i = 1; i < data.categories.length; i++) {

						docs.addAll(
								locations.whereEqualTo("category", data.categories[i]).get().get().getDocuments());

					}

				} else {
					docs = new LinkedList<QueryDocumentSnapshot>();
					docs.addAll(locations.whereEqualTo("region", data.region)
							.whereEqualTo("category", data.categories[0]).get().get().getDocuments());

					for (int i = 1; i < data.categories.length; i++) {
						docs.addAll(locations.whereEqualTo("region", data.region)
								.whereEqualTo("category", data.categories[i]).get().get().getDocuments());
					}

				}
				res.put("locations",
						JsonArraySupport.createLocationPropArray(docs, "name", "description", "address", "latitude",
								"longitude", "category", "region", "nbrVisits", "score", "image", "placeID", "region"));

			} else {

				docs = locations.whereEqualTo("name", data.lastName).get().get().getDocuments();
				QueryDocumentSnapshot lastDoc = docs.get(0);

				if (data.categories[0].equalsIgnoreCase("") && data.region.equalsIgnoreCase("")) {

					docs = locations.orderBy("nbrVisits").get().get().getDocuments();

				} else if (data.categories[0].equalsIgnoreCase("")) {

					docs = locations.orderBy("nbrVisits").whereEqualTo("region", data.region).startAfter(lastDoc)
							.limit(data.limit).get().get().getDocuments();

				} else if (data.region.equalsIgnoreCase("")) {
					docs = new LinkedList<QueryDocumentSnapshot>();
					docs.addAll(locations.whereEqualTo("category", data.categories[0]).startAfter(lastDoc)
							.limit(data.limit).get().get().getDocuments());
					for (int i = 1; i < data.categories.length; i++) {
						docs.addAll(locations.whereEqualTo("category", data.categories[i]).startAfter(lastDoc)
								.limit(data.limit).get().get().getDocuments());
					}

				} else {
					docs = new LinkedList<QueryDocumentSnapshot>();

					docs.addAll(locations.orderBy("nbrVisits").whereEqualTo("region", data.region)
							.whereEqualTo("category", data.categories[0]).startAfter(lastDoc).limit(data.limit).get()
							.get().getDocuments());
					for (int i = 1; i < data.categories.length; i++) {

						docs.addAll(locations.whereEqualTo("region", data.region).orderBy("nbrVisits")
								.whereEqualTo("region", data.region).whereEqualTo("category", data.categories[i])
								.startAfter(lastDoc).limit(data.limit).get().get().getDocuments());
					}

				}

				res.put("locations",
						JsonArraySupport.createLocationPropArray(docs, "name", "description", "address", "latitude",
								"longitude", "category", "region", "nbrVisits", "score", "image", "placeID", "region"));

			}

			return Response.ok(res.toString()).build();
		} catch (Exception e) {
			return Response.status(Status.FORBIDDEN).build();
		}
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

	@POST
	@Path("/getall")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllLocations(TokenData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getAllLocations")) {
			CollectionReference locations = db.collection("locations");

			ApiFuture<QuerySnapshot> querySnapshot = locations.get();
			JSONObject res = new JSONObject();
			List<QueryDocumentSnapshot> docs = querySnapshot.get().getDocuments();

			// res.add("locations", JsonArraySupport.createThreePropArray(docs, "latitude",
			// "longitude", "category"));

			JSONArray array = new JSONArray();
			JSONObject jsObj;

			for (QueryDocumentSnapshot document1 : docs) {
				jsObj = new JSONObject();
				jsObj.put("latitude", document1.get("latitude").toString());
				jsObj.put("longitude", document1.get("longitude").toString());
				jsObj.put("category", document1.get("category").toString());
				jsObj.put("placeID", document1.getString("placeID"));
				jsObj.put("region", document1.getString("region"));
				jsObj.put("score", document1.get("score"));
				array.put(jsObj);
			}

			res.put("locations", array);

			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.put("tokenID", at.tokenID);

			return Response.ok(res.toString()).build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}
	
	@POST
	@Path("/guest/getall")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getGuestAllLocations() throws InterruptedException, ExecutionException {

			CollectionReference locations = db.collection("locations");

			ApiFuture<QuerySnapshot> querySnapshot = locations.get();
			JSONObject res = new JSONObject();
			List<QueryDocumentSnapshot> docs = querySnapshot.get().getDocuments();

			// res.add("locations", JsonArraySupport.createThreePropArray(docs, "latitude",
			// "longitude", "category"));

			JSONArray array = new JSONArray();
			JSONObject jsObj;

			for (QueryDocumentSnapshot document1 : docs) {
				jsObj = new JSONObject();
				jsObj.put("latitude", document1.get("latitude").toString());
				jsObj.put("longitude", document1.get("longitude").toString());
				jsObj.put("category", document1.get("category").toString());
				jsObj.put("placeID", document1.getString("placeID"));
				jsObj.put("region", document1.getString("region"));
				jsObj.put("score", document1.get("score"));
				array.put(jsObj);
			}

			res.put("locations", array);

			return Response.ok(res.toString()).build();
	}

	@POST
	@Path("/getsimple")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLocationSimple(LocationData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getLocationSimple")) {
			CollectionReference locations = db.collection("locations");

			ApiFuture<QuerySnapshot> querySnapshot = locations.whereEqualTo("latitude", data.latitude)
					.whereEqualTo("longitude", data.longitude).get();
			JsonObject res = new JsonObject();
			List<QueryDocumentSnapshot> docs = querySnapshot.get().getDocuments();

			JsonArray array = new JsonArray();
			JsonObject jsObj;

			for (QueryDocumentSnapshot document1 : docs) {
				jsObj = new JsonObject();
				jsObj.addProperty("latitude", document1.get("latitude").toString());
				jsObj.addProperty("longitude", document1.get("longitude").toString());
				jsObj.addProperty("category", document1.get("category").toString());
				jsObj.addProperty("placeID", document1.getString("placeID"));
				array.add(jsObj);
			}

			res.add("locations", array);

			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.addProperty("tokenID", at.tokenID);

			return Response.ok(g.toJson(res)).build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}

	@POST
	@Path("/updateid")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePlaceID(LocationData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "updatePlaceID")) {
			CollectionReference locations = db.collection("locations");

			ApiFuture<QuerySnapshot> querySnapshot = locations.whereEqualTo("latitude", data.latitude)
					.whereEqualTo("longitude", data.longitude).get();
			JsonObject res = new JsonObject();
			List<QueryDocumentSnapshot> docs = querySnapshot.get().getDocuments();

			DocumentReference docRef = null;

			for (QueryDocumentSnapshot document1 : docs) {
				docRef = document1.getReference();
			}

			ApiFuture<WriteResult> update = docRef.update("placeID", data.placeID);
			update.get();

			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.addProperty("tokenID", at.tokenID);

			return Response.ok(g.toJson(res)).build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}

}
