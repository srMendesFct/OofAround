package pt.oofaround.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pt.oofaround.support.MediaSupport;
import pt.oofaround.util.UploadImageData;

@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
public class ImageResource {

	public ImageResource() {
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadImage(UploadImageData upload) {

		MediaSupport.uploadImage(upload.name, upload.image);

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
