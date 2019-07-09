package pt.oofaround.resources;

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

import org.json.JSONException;
import org.json.JSONObject;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

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
	
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postComment(CommentData data) throws JSONException, InterruptedException, ExecutionException {
		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "postComment")) {

			Map<String, Object> docData = new HashMap<String, Object>();
			
			docData.put("poster", data.usernameR);
			docData.put("comment", data.comment);
			docData.put("routeName",data.routeName);
			docData.put("routeCreatorUsername", data.routeCreatorUsername);
			docData.put("date", Timestamp.of(new Date()));
			
			db.collection("comments").document().set(docData);
			
			JSONObject res = new JSONObject();
			AuthToken at = new AuthToken(data.usernameR, data.role);
			res.put("tokenID", at.tokenID);

			return Response.ok().entity(res.toString()).build();
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}
	
}
