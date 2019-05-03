package pt.oofaround.resources;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;

import pt.oofaround.util.AuthToken;
import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.RegisterData;
@SuppressWarnings("unused")
@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterResource {

	private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());

	private final Gson g = new Gson();

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	public RegisterResource() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked"})
	@POST
	@Path("/user")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegister(RegisterData data) throws InterruptedException, ExecutionException {

		CollectionReference users = db.collection("users");
		Query query = users.whereEqualTo("email", data.email);

		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			return Response.status(Status.FOUND).entity("Email already in use.").build();
		}

		query = users.whereEqualTo("username", data.username);

		querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			return Response.status(Status.FOUND).entity("Username already in use.").build();
		}

		String passEnc = Hashing.sha512().hashString(data.password, StandardCharsets.UTF_8).toString();

		Map<String, Object> docData = new HashMap();
		docData.put("username", data.username);
		docData.put("password", passEnc);
		docData.put("email", data.email);
		docData.put("role", "user");
		docData.put("country", data.country);
		docData.put("cellphone", data.cellphone);
		docData.put("privacy", data.privacy);

		ApiFuture<WriteResult> newUser = users.document(data.email).set(docData);
		return Response.ok().entity("User created successfully.").build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Path("/bo")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegisterBackOffice(RegisterData data) throws InterruptedException, ExecutionException {

		if(AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "doRegisterBackOffice", data.expirationDate)) {
		CollectionReference users = db.collection("users");
		Query query = users.whereEqualTo("email", data.email);

		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			return Response.status(Status.FOUND).entity("Email already in use.").build();
		}

		query = users.whereEqualTo("username", data.username);

		querySnapshot = query.get();

		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			return Response.status(Status.FOUND).entity("Username already in use.").build();
		}

		String passEnc = Hashing.sha512().hashString(data.password, StandardCharsets.UTF_8).toString();

		Map<String, Object> docData = new HashMap();
		docData.put("username", data.username);
		docData.put("password", passEnc);
		docData.put("email", data.email);
		docData.put("role", "bo");
		docData.put("country", data.country);
		docData.put("cellphone", data.cellphone);
		docData.put("privacy", data.privacy);

		ApiFuture<WriteResult> newUser = users.document(data.email).set(docData);
		return Response.ok().entity("User created successfully.").build();
	}else
		return Response.status(Status.FORBIDDEN).build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Path("/auser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegisterAuser(RegisterData data) throws InterruptedException, ExecutionException {

		if(AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "doRegisterAuser", data.expirationDate)) {
			CollectionReference users = db.collection("users");
			Query query = users.whereEqualTo("email", data.email);

			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				return Response.status(Status.FOUND).entity("Email already in use.").build();
			}

			query = users.whereEqualTo("username", data.username);

			querySnapshot = query.get();

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				return Response.status(Status.FOUND).entity("Username already in use.").build();
			}

			String passEnc = Hashing.sha512().hashString(data.password, StandardCharsets.UTF_8).toString();

			Map<String, Object> docData = new HashMap();
			docData.put("username", data.username);
			docData.put("password", passEnc);
			docData.put("email", data.email);
			docData.put("role", "bo");
			docData.put("country", data.country);
			docData.put("cellphone", data.cellphone);
			docData.put("privacy", data.privacy);

			ApiFuture<WriteResult> newUser = users.document(data.email).set(docData);
			return Response.ok().entity("User created successfully.").build();
		}else
			return Response.status(Status.FORBIDDEN).build();
	}

}
