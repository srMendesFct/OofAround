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
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import pt.oofaround.support.MediaSupport;
import pt.oofaround.util.FolderData;
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
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadImage(UploadImageData upload) {

		MediaSupport.uploadImage(upload.name, upload.image);

		return Response.ok().build();
	}

	@SuppressWarnings("unused")
	@POST
	@Path("/folder")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadToProfileFolder(FolderData data) {
		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();

		BlobId blobId = BlobId.of(BUCKET, data.username + "/" + data.photoName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build();

		Blob blob = db.create(blobInfo, data.image);

		return Response.ok().build();
	}
}
