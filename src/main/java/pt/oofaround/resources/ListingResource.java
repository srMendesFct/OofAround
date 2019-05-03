package pt.oofaround.resources;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.ws.rs.GET;
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
import com.google.cloud.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pt.oofaround.util.AuthToken;
import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.TokenData;

@Path("/list")
@Produces(MediaType.APPLICATION_JSON)
public class ListingResource {
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

	private final Gson g = new Gson();

	private FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround")
			.build();
	private final Firestore db = firestore.getService();

	public ListingResource() {
	}

	@GET
	@Path("/users")
	public Response getAllUsers(TokenData data) throws InterruptedException, ExecutionException {
		LOG.fine("Listing users");

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getAllUsers",
				data.expirationDate)) {
			CollectionReference users = db.collection("users");
			try {
				Query query = users;
				ApiFuture<QuerySnapshot> querySnapshot = query.get();
				JsonObject res = new JsonObject();
				int i = 0;
				for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
					res.addProperty("user" + i++, document.getString("username"));
				}

				AuthToken at = new AuthToken(data.usernameR, data.role);
				JsonObject token = new JsonObject();
				token.addProperty("username", at.username);
				token.addProperty("role", at.role);
				token.addProperty("expirationDate", at.expirationDate);
				token.addProperty("tokenID", at.tokenID);
				return Response.ok(g.toJson(token)).build();
			} catch (Exception e) {
				return Response.status(Status.FORBIDDEN).entity(e.toString()).build();
			}
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

	@GET
	@Path("/publicusers")
	public Response getPublicUsers(TokenData data) throws InterruptedException, ExecutionException {
		LOG.fine("Listing users");

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getPublicUsers",
				data.expirationDate)) {
			CollectionReference users = db.collection("users");
			try {
				Query query = users;
				ApiFuture<QuerySnapshot> querySnapshot = query.get();
				JsonObject res = new JsonObject();
				int i = 0;
				for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
					res.addProperty("user" + i++, document.getString("username"));
				}

				AuthToken at = new AuthToken(data.usernameR, data.role);
				JsonObject token = new JsonObject();
				token.addProperty("username", at.username);
				token.addProperty("role", at.role);
				token.addProperty("expirationDate", at.expirationDate);
				token.addProperty("tokenID", at.tokenID);
				return Response.ok(g.toJson(token)).build();
			} catch (Exception e) {
				return Response.status(Status.FORBIDDEN).entity("Failed get").build();
			}
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}
}
