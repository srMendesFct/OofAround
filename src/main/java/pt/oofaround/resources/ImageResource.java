package pt.oofaround.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;

import pt.oofaround.support.MediaSupport;
import pt.oofaround.util.UploadImageData;

@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
public class ImageResource {

	private static final Logger LOG = Logger.getLogger(ImageResource.class.getName());

	private static final String BUCKET = "oofaround.appspot.com";

	public ImageResource() {
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadImage(UploadImageData upload) {

		MediaSupport.uploadImage(upload.name, upload.image);

		return Response.ok().build();
	}

	@POST
	@Path("/folder")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createFolder() {
		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();

		BlobId blobId = BlobId.of(BUCKET, "BBB/");
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build();

		return Response.ok().build();
	}

	/*
	 * @GET
	 * 
	 * @Path("{name}")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response
	 * downloadImage(@PathParam("name") String name) { Blob blob =
	 * storage.get(BlobId.of(BUCKET, name)); return
	 * Response.ok().entity(blob.getSize()).build(); }
	 *
	 * @GET
	 * 
	 * @Path("/logo") public Response downloadImage() { // Blob blob =
	 * storage.get(BlobId.of(BUCKET, "logo_equipa.jpg")); Blob blob = db.get(BUCKET,
	 * "logo_equipa.jpg", BlobGetOption.fields(Storage.BlobField.values())); String
	 * s = "https://storage.googleapis.com/oofaround.appspot.com/" +
	 * blob.getMediaLink(); return Response.ok(s).build(); }
	 */
}
