package pt.oofaround.resources;

import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
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
import com.google.cloud.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pt.oofaround.util.AuthToken;
import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.UserData;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserInfoResource {

	private final Gson g = new Gson();

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	public UserInfoResource() {
	}

	// information of the own user, does not require usernameR for token
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserInfo(UserData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.username, data.role, "getUserInfo",
				data.expirationDate)) {
			CollectionReference users = db.collection("users");

			ApiFuture<QuerySnapshot> querySnapshot = users.whereEqualTo("username", data.username).get();

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				AuthToken at = new AuthToken(data.username, data.role);
				JsonObject res = new JsonObject();
				res.addProperty("username", data.username);
				res.addProperty("role", data.role);
				res.addProperty("expirationDate", at.expirationDate);
				res.addProperty("tokenID", at.tokenID);
				res.addProperty("cellphone", document.getBoolean("cellphone"));
				res.addProperty("country", document.getBoolean("country"));
				res.addProperty("email", document.getBoolean("email"));
				return Response.ok(g.toJson(res)).build();
			}
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.FORBIDDEN).build();
	}

	@Path("/other")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getOtherUserInfo(UserData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getOtherUserInfo",
				data.expirationDate)) {
			CollectionReference users = db.collection("users");

			ApiFuture<QuerySnapshot> querySnapshot = users.whereEqualTo("username", data.username).get();

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				AuthToken at = new AuthToken(data.usernameR, data.role);
				JsonObject res = new JsonObject();
				res.addProperty("username", data.username);
				res.addProperty("username", data.usernameR);
				res.addProperty("role", data.role);
				res.addProperty("expirationDate", at.expirationDate);
				res.addProperty("tokenID", at.tokenID);
				res.addProperty("cellphone", document.getBoolean("cellphone"));
				res.addProperty("country", document.getBoolean("country"));
				res.addProperty("email", document.getBoolean("email"));
				return Response.ok(g.toJson(res)).build();
			}
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.FORBIDDEN).build();
	}

}
