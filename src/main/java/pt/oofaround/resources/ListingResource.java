package pt.oofaround.resources;

import java.util.ArrayList;
import java.util.List;
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

import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.TokenData;

@SuppressWarnings("unused")
@Path("/list")
@Produces(MediaType.APPLICATION_JSON)
public class ListingResource {
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

	private final Gson g = new Gson();

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("solo-project-apdc")
			.build();
	private final Firestore db = firestore.getService();

	public ListingResource() {
	}

	@GET
	@Path("/users")
	public Response getUsers(TokenData data) {
		LOG.fine("Listing users");

		if(AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "EmptyForNow", data.expirationDate)) {
		CollectionReference users = db.collection("users");
		try {
			Query query = users;
			ApiFuture<QuerySnapshot> querySnapshot = query.get();
			List<String> usersL = new ArrayList<String>();

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				usersL.add(document.getString("username"));
			}

			String json = new Gson().toJson(usersL);

			return Response.status(Status.OK).entity(json).build();
		} catch (Exception e) {
			return Response.status(Status.FORBIDDEN).entity("Failed get").build();
		}
	}else
		return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}
}
