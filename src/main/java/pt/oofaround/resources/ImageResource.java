package pt.oofaround.resources;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.api.core.ApiFuture;
import com.google.api.gax.paging.Page;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobGetOption;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;

import pt.oofaround.support.MediaSupport;
import pt.oofaround.util.UploadImageData;

@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
public class ImageResource {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ImageResource.class.getName());

	private static final String BUCKET = "oofaround.appspot.com";

	public ImageResource() {
	}

	@POST
	@Path("/uploadprofile")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadImage(UploadImageData upload) {

		MediaSupport.uploadImage(upload.name + "_profile", upload.image);

		return Response.ok().build();
	}

	@POST
	@Path("/uploadfoto")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadToProfileFolder(UploadImageData data) throws InterruptedException, ExecutionException {
		FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround")
				.build();
		Firestore db = firestore.getService();

		CollectionReference users = db.collection("users");
		Query query = users.whereEqualTo("username", data.name);

		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		long nbrPics = 0;
		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			nbrPics = (long) document.get("numberPhotos");
			ApiFuture<WriteResult> future = document.getReference().update("numberPhotos", nbrPics+1);
			future.get();
		}
		MediaSupport.uploadImage(data.name + "/" + nbrPics, data.image);

		return Response.ok().build();
	}

	@POST
	@Path("/getList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getList(UploadImageData data) {
		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();

		Page<Blob> list = db.list(BUCKET, BlobListOption.prefix(data.name + "/"));
		Iterator<Blob> it = list.iterateAll().iterator();
		List<String> blobs = new LinkedList<String>();
		it.next();
		while (it.hasNext())
			blobs.add(it.next().getMediaLink());

		// Blob blob = db.get(BlobId.of(BUCKET, data.username + "/" + "0"));

		return Response.ok().entity(blobs).build();
	}

	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response get(UploadImageData data) {
		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();

		BlobId blobId = BlobId.of(BUCKET, data.name);

		Blob blob = db.get(blobId, BlobGetOption.fields(Storage.BlobField.MEDIA_LINK));


		// Blob blob = db.get(BlobId.of(BUCKET, data.username + "/" + "0"));

		return Response.ok().entity(blob).build();
	}

}