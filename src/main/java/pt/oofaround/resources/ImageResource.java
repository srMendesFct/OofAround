package pt.oofaround.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import pt.oofaround.util.UploadImageData;

@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
public class ImageResource {

	private final Storage storage = StorageOptions.getDefaultInstance().getService();

	private static final Logger LOG = Logger.getLogger(ImageResource.class.getName());

	private static final String BUCKET = "oofaround.appspot.com";

	public ImageResource() {
	}

	@SuppressWarnings("unused")
	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadImage(UploadImageData upload) {
		BlobId blobId = BlobId.of(BUCKET, upload.name);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(MediaType.APPLICATION_OCTET_STREAM).build();
		Blob blob = storage.create(blobInfo, upload.image);

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
	 */

	@GET
	@Path("/logo")
	public Response downloadImage() {
		Blob blob = storage.get(BlobId.of(BUCKET, "logo_equipa.jpg"));
		return Response.ok().entity(blob.getSize()).build();
	}

}
