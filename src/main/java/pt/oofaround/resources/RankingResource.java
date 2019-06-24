package pt.oofaround.resources;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobGetOption;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.oofaround.util.AuthToken;
import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.RankingData;

@Path("/ranking")
@Produces(MediaType.APPLICATION_JSON)
public class RankingResource {

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	private final Gson g = new Gson();

	public RankingResource() {
	}

	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getRank(RankingData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getRank")) {
			try {
				List<QueryDocumentSnapshot> rankingDocs = db.collection("rankings")
						.whereEqualTo("username", data.usernameR).get().get().getDocuments();

				JsonObject res = new JsonObject();

				for (QueryDocumentSnapshot document : rankingDocs) {
					res.addProperty("username", document.getString("username"));
					res.addProperty("score", document.getLong("score"));
					res.addProperty("rank", document.getId());
				}

				AuthToken at = new AuthToken(data.usernameR, data.role);
				res.addProperty("tokenID", at.tokenID);

				return Response.ok().entity(g.toJson(res)).build();
			} catch (Exception e) {
				return Response.status(Status.NOT_FOUND).entity("User doesn't exist.").build();
			}
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}

	@POST
	@Path("/getAll")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllRanks(RankingData data) throws InterruptedException, ExecutionException {

		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "getAllRanks")) {
			try {
				// List<QueryDocumentSnapshot> rankingDocs =
				// db.collection("rankings").limit(100).get().get().getDocuments();

				JsonObject res = new JsonObject();
				/*
				 * JsonObject jObj; JsonArray ranks = new JsonArray();
				 * 
				 * 
				 * for (QueryDocumentSnapshot document : rankingDocs) { jObj = new JsonObject();
				 * jObj.addProperty("username", document.getString("username"));
				 * jObj.addProperty("score", document.getLong("score"));
				 * jObj.addProperty("rank", document.getId()); ranks.add(jObj); }
				 */

				StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround")
						.build();

				Storage storageDB = storage.getService();

				BlobId blobId = BlobId.of("oofaround.appspot.com", "topRankArray.json");

				Blob blob = storageDB.get(blobId, BlobGetOption.fields(Storage.BlobField.MEDIA_LINK));

				JsonArray jArr = new JsonArray();

				jArr.add(new String(blob.getContent()));

				res.add("ranks", jArr);

				AuthToken at = new AuthToken(data.usernameR, data.role);
				res.addProperty("tokenID", at.tokenID);

				return Response.ok().entity(g.toJson(res)).build();
			} catch (Exception e) {
				return Response.status(Status.NOT_FOUND).entity("User doesn't exist.").build();
			}
		} else
			return Response.status(Status.FORBIDDEN).entity("Invalid permissions.").build();
	}
}
