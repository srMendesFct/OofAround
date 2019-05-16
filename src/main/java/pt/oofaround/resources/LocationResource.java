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
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
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
			docData.put("link", "https://storage.googleapis.com/oofaround.appspot.com/" + data.name);

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
				res.addProperty("link", document.getString("link"));
			}
			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.addProperty("tokenID", at.tokenID);

			return Response.ok(g.toJson(res)).build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}

	@POST
	@Path("/getCategory")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLocationsByCat(LocationData data) throws InterruptedException, ExecutionException {

		LOG.fine("Getting category" + data.name);
		
		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getPublicRankings")) {
			CollectionReference users = db.collection("users");
			Query query = users.whereEqualTo("category", data.category);
			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			JsonObject res = new JsonObject();
			for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
				res.addProperty("ownScore", document.get("score").toString());
			}

			try {
				Query sortedUsers;
				ApiFuture<QuerySnapshot> queryRes;
				List<QueryDocumentSnapshot> docs;

				if (data.lastRequest == 0) {
					sortedUsers = users.orderBy("score", Direction.DESCENDING).limit(data.limit);
					queryRes = sortedUsers.get();
					docs = queryRes.get().getDocuments();
					res.add("locations", JsonArraySupport.createLocationPropArray(docs, "name", "description", "link", "address", "latitude", "longitude"));
				} else {
					sortedUsers = users.whereEqualTo("username", data.lastName);
					queryRes = sortedUsers.get();
					docs = queryRes.get().getDocuments();
					QueryDocumentSnapshot lastDoc = docs.get(0);
					sortedUsers = users.orderBy("score", Direction.DESCENDING).startAfter(lastDoc).limit(data.limit);
					queryRes = sortedUsers.get();
					docs = queryRes.get().getDocuments();
					res.add("locations", JsonArraySupport.createLocationPropArray(docs, "name", "description", "link", "address", "latitude", "longitude"));
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

		/*if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getLocationsByCat")) {
			CollectionReference locations = db.collection("locations");
			Query query = locations.whereEqualTo("category", data.name);

			ApiFuture<QuerySnapshot> querySnapshot = query.get();
			List<QueryDocumentSnapshot> docs = querySnapshot.get().getDocuments();
			JsonObject res = new JsonObject();
			res.add("names", JsonArraySupport.createOnePropArray(docs, "name"));
			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.addProperty("tokenID", at.tokenID);

			return Response.ok(g.toJson(res)).build();
		} else
			return Response.status(Status.FORBIDDEN).build();*/
	}

}
