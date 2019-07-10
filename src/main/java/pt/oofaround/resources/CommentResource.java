package pt.oofaround.resources;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage.BlobGetOption;

import pt.oofaround.util.AuthToken;
import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.CommentData;

@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	public CommentResource() {
	}

	@SuppressWarnings("unused")
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postComment(CommentData data) throws JSONException, InterruptedException, ExecutionException {
		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "postComment")) {

			Map<String, Object> docData = new HashMap<String, Object>();
			JSONArray jArr = new JSONArray();
			JSONObject jObj = new JSONObject();

			docData.put("poster", data.usernameR);
			docData.put("comment", data.comment);
			docData.put("routeName", data.routeName);
			docData.put("routeCreatorUsername", data.routeCreatorUsername);
			Date date = new Date();
			docData.put("date", Timestamp.of(date));
			
			jObj.put("poster",  data.usernameR);
			jObj.put("comment",  data.comment);
			jObj.put("routeName",  data.routeName);
			jObj.put("routeCreatorUsername", data.routeCreatorUsername);
			jObj.put("date", date);
			jArr.put(jObj);

			db.collection("comments").document().set(docData);

			ApiFuture<QuerySnapshot> querySnapshot = db.collection("comments").whereEqualTo("routeName", data.routeName)
					.whereEqualTo("routeCreatorUsername", data.routeCreatorUsername).get();

			for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
				jObj = new JSONObject();
				jObj.put("poster", document.get("poster"));
				jObj.put("comment", document.get("comment"));
				jObj.put("routeName", document.get("routeName"));
				jObj.put("routeCreatorUsername", document.get("routeCreatorUsername"));
				jObj.put("date", ((Timestamp) document.get("date")).toDate());
				jArr.put(jObj);
			}

			StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

			Storage storageDB = storage.getService();

			BlobId blobId = BlobId.of("oofaround.appspot.com", data.routeName + data.routeCreatorUsername + ".json");
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

			Blob blob = storageDB.create(blobInfo, jArr.toString().getBytes(StandardCharsets.UTF_8));

			JSONObject res = new JSONObject();
			res.put("comments", jArr);
			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.put("tokenID", at.tokenID);

			return Response.ok().entity(res.toString()).build();
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@POST
	@Path("/listcomments")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listComments(CommentData data) throws JSONException, InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "listComments")) {
			try {

				StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround")
						.build();

				Storage storageDB = storage.getService();

				BlobId blobId = BlobId.of("oofaround.appspot.com",
						data.routeName + data.routeCreatorUsername + ".json");

				Blob blob = storageDB.get(blobId, BlobGetOption.fields(Storage.BlobField.MEDIA_LINK));

				JSONArray jArr = new JSONArray(new String(blob.getContent()));

				JSONObject res = new JSONObject();

				res.put("comments", jArr);

				AuthToken at = new AuthToken(data.usernameR, data.role);
				res.put("tokenID", at.tokenID);

				return Response.ok().entity(res.toString()).build();
			} catch (Exception e) {
				return Response.status(Status.NOT_FOUND).entity("User doesn't exist.").build();
			}
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

	@POST
	@Path("/deletecomment")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteComment(CommentData data) throws JSONException, InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "deleteComment")) {
			try {
				ApiFuture<QuerySnapshot> querySnapshot = db.collection("comments")
						.whereEqualTo("poster", data.usernameR).whereEqualTo("date", Timestamp.of(data.timestamp))
						.get();

				for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
					document.getReference().delete();
				}

				JSONObject res = new JSONObject();
				AuthToken at = new AuthToken(data.usernameR, data.role);
				res.put("tokenID", at.tokenID);

				return Response.ok().entity(res.toString()).build();
			} catch (Exception e) {
				return Response.status(Status.NOT_FOUND).entity("User doesn't exist.").build();
			}
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

}
