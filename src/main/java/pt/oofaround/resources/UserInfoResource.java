package pt.oofaround.resources;

import java.nio.charset.StandardCharsets;
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

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pt.oofaround.support.EmailSupport;
import pt.oofaround.support.JsonArraySupport;
import pt.oofaround.util.AuthToken;
import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.ChangePasswordData;
import pt.oofaround.util.RecoverPasswordData;
import pt.oofaround.util.UserData;

@Path("/userinfo")
@Produces(MediaType.APPLICATION_JSON)
public class UserInfoResource {

	private static final Logger LOG = Logger.getLogger(UserInfoResource.class.getName());

	private final Gson g = new Gson();

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	public UserInfoResource() {
	}

	// information of the own user, does not require usernameR for token
	@SuppressWarnings("unchecked")
	@POST
	@Path("/self")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserInfo(UserData data) throws InterruptedException, ExecutionException {

		LOG.fine("Listing user" + data.usernameR);

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getUserInfo")) {

			ApiFuture<QuerySnapshot> querySnapshot = db.collection("users").whereEqualTo("username", data.usernameR)
					.get();

			JSONObject res = new JSONObject();
			for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
				res.put("score", document.get("score").toString());
				res.put("username", document.getString("username"));
				res.put("email", document.getString("email"));
				res.put("country", document.getString("country"));
				res.put("cellphone", document.getString("cellphone"));
				List<String> routes = (List<String>) document.get("routes");
				if (routes != null)
					res.put("routes", JsonArraySupport.createOnePropArrayFromFirestore(routes, "routeName"));
				// comment
				if (document.getBoolean("privacy"))
					res.put("privacy", "private");
				else
					res.put("privacy", "public");
			}
			// AuthToken at = new AuthToken(data.usernameR, data.role);
			// res.addProperty("tokenID", at.tokenID);

			return Response.ok(res.toString()).build();
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/routes")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserRoutes(UserData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getUserRoutes")) {
			CollectionReference users = db.collection("users");
			Query query = users.whereEqualTo("username", data.usernameR);
			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			JSONObject res = new JSONObject();
			for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
				List<String> routes = (List<String>) document.get("routes");
				if (routes != null)
					res.put("routes", JsonArraySupport.createOnePropArrayFromFirestore(routes, "routeName"));
				else {
					return Response.status(Status.NO_CONTENT).build();
				}
			}
			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.put("tokenID", at.tokenID);

			return Response.ok(res.toString()).build();
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/doneroutes")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDoneRoutes(UserData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getDoneRoutes")) {
			CollectionReference users = db.collection("users");
			Query query = users.whereEqualTo("username", data.usernameR);
			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			JSONObject res = new JSONObject();
			for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
				List<String> routes = (List<String>) document.get("doneRoutes");
				if (routes != null) {

					String[] split;
					JSONArray jArr = new JSONArray();
					JSONObject jObj;

					for (String s : routes) {
						jObj = new JSONObject();
						split = s.split(" ");
						jObj.put("name", split[0]);
						jObj.put("creatorUsername", split[1]);
						jArr.put(jObj);
					}
					res.put("doneRoutes", jArr);
				} else {
					return Response.status(Status.NO_CONTENT).build();
				}
			}
			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.put("tokenID", at.tokenID);

			return Response.ok(res.toString()).build();
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Path("/alterself")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterUserInfo(UserData data) throws InterruptedException, ExecutionException {

		Map<String, Object> docData = new HashMap();

		docData.put("email", data.email);
		docData.put("country", data.country);
		docData.put("cellphone", data.cellphone);
		docData.put("privacy", data.privacy);

		ApiFuture<WriteResult> alterInfo = db.collection("users").document(data.usernameR).set(docData,
				SetOptions.merge());
		alterInfo.get();

		JsonObject res = new JsonObject();

		// AuthToken at = new AuthToken(data.usernameR, data.role);
		// res.addProperty("tokenID", at.tokenID);

		return Response.ok().entity(g.toJson(res)).build();
	}

	@POST
	@Path("/alterpassword")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterPassword(ChangePasswordData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "alterPassword")) {

			String passEnc = Hashing.sha512().hashString(data.oldPassword, StandardCharsets.UTF_8).toString();

			if (db.collection("users").document(data.usernameR).get().get().getString("password").equals(passEnc)) {

				Map<String, Object> docData = new HashMap<String, Object>();
				passEnc = Hashing.sha512().hashString(data.password, StandardCharsets.UTF_8).toString();

				docData.put("password", passEnc);

				ApiFuture<WriteResult> alterInfo = db.collection("users").document(data.usernameR).set(docData,
						SetOptions.merge());
				alterInfo.get();

				JSONObject res = new JSONObject();

				AuthToken at = new AuthToken(data.usernameR, data.role);
				res.put("tokenID", at.tokenID);

				return Response.ok().entity(res.toString()).build();
			} else
				return Response.status(Status.FORBIDDEN).build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}

	@POST
	@Path("/getrecovercode")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getRecoverCode(ChangePasswordData data) throws InterruptedException, ExecutionException {

		String recoverCode = Hashing.sha256()
				.hashBytes((data.usernameR + String.valueOf(System.currentTimeMillis())).getBytes()).toString();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("username", data.usernameR);
		map.put("id", recoverCode);
		map.put("expires", System.currentTimeMillis() + 300000);

		db.collection("recovery").document().set(map);

		db.collection("flag").document("recovery").update("flag", true);

		EmailSupport.sendRecoverCode(db.collection("users").document(data.usernameR).get().get().getString("email"),
				recoverCode);

		return Response.ok().build();
	}

	@POST
	@Path("/recoverpassword")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response recoverPassword(RecoverPasswordData data) throws InterruptedException, ExecutionException {

		ApiFuture<QuerySnapshot> querySnapshot = db.collection("recovery").whereEqualTo("id", data.recoverCode).get();

		String username = "";

		for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
			if (document.getLong("expires") < System.currentTimeMillis()) {
				document.getReference().delete();
				return Response.status(403).build();
			} else {
				username = document.getString("username");
			}
		}

		if (data.password.equals(data.confirmPassword) && !username.equals("")) {
			Map<String, Object> docData = new HashMap<String, Object>();
			String passEnc = Hashing.sha512().hashString(data.password, StandardCharsets.UTF_8).toString();

			docData.put("password", passEnc);

			ApiFuture<WriteResult> alterInfo = db.collection("users").document(username).set(docData,
					SetOptions.merge());
			alterInfo.get();

			return Response.ok().build();
		} else
			return Response.status(Status.EXPECTATION_FAILED).build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Path("/alterotherrole")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterOtherRole(UserData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "alterOtherRole")) {
			Map<String, Object> docData = new HashMap();

			docData.put("role", data.newRole);

			db.collection("users").document(data.username).set(docData, SetOptions.merge());

			JSONObject res = new JSONObject();

			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.put("tokenID", at.tokenID);

			return Response.ok().entity(res.toString()).build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}

	@POST
	@Path("/other")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getOtherUserInfo(UserData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getOtherUserInfo")) {
			CollectionReference users = db.collection("users");
			Query query = users.whereEqualTo("username", data.username);
			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			JsonObject res = new JsonObject();
			for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
				res.addProperty("score", document.get("score").toString());
				res.addProperty("username", document.getString("username"));
				res.addProperty("email", document.getString("email"));
				res.addProperty("country", document.getString("country"));
				res.addProperty("cellphone", document.getString("cellphone"));
				if (document.getBoolean("privacy"))
					res.addProperty("privacy", "private");
				else
					res.addProperty("privacy", "public");
			}
			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.addProperty("tokenID", at.tokenID);

			return Response.ok(g.toJson(res)).build();
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

}
