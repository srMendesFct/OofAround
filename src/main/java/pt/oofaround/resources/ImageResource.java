package pt.oofaround.resources;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.appengine.repackaged.com.google.common.hash.Hashing;
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

	private static final String SECRETUSER = "99999999527";
	private static final String SECRETPIC = "99999997841";

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

		BlobId blobId = BlobId.of(BUCKET,
				Hashing.hmacSha256(SECRETUSER.getBytes()).hashString(data.username, StandardCharsets.UTF_8).toString());
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build();

		Blob blob = db.create(blobInfo);

		/*blobId = BlobId.of(BUCKET,
				Hashing.hmacSha256(SECRETUSER.getBytes()).hashString(data.username, StandardCharsets.UTF_8).toString()
						+ "/" + Hashing.hmacSha256(SECRETPIC.getBytes())
								.hashString(data.photoName, StandardCharsets.UTF_8).toString());
		blobInfo = BlobInfo.newBuilder(blobId)
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build();

		blob = db.create(blobInfo, data.image);*/

		return Response.ok().build();
	}
}