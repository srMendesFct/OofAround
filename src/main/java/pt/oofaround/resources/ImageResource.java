package pt.oofaround.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobGetOption;
import com.google.cloud.storage.Storage.BlobListOption;
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
	public Response uploadToProfileNewFolder(FolderData data) {
		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();

		BlobId blobId = BlobId.of(BUCKET, data.username + "/");
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build();

		Blob blob = db.create(blobInfo);

		MediaSupport.uploadImage(data.username + "/" + data.photoName, data.image);

		/*
		 * blobId = BlobId.of(BUCKET, data.username + "/" + data.photoName); blobInfo =
		 * BlobInfo.newBuilder(blobId) .setAcl(new
		 * ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(),
		 * Role.READER)))).content.build();
		 * 
		 * blob = db.create(blobInfo, data.image);
		 */

		return Response.ok().build();
	}

	@POST
	@Path("/picfolder")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadToProfileFolder(FolderData data) {
		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();

		BlobId blobId = BlobId.of(BUCKET, data.username + "/" + data.photoName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build();

		Blob blob = db.create(blobInfo, data.image);

		Page<Blob> list = db.list(BUCKET, BlobListOption.prefix(data.username + "/"));

		return Response.ok().build();
	}

	@POST
	@Path("/getList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getList(FolderData data) {
		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();

		Page<Blob> list = db.list(BUCKET, BlobListOption.prefix(data.username + "/"));
		Iterator<Blob> it = list.iterateAll().iterator();
		List<String> blobs = new LinkedList<String>();
		it.next();
		while (it.hasNext())
			blobs.add(it.next().getName());

		// Blob blob = db.get(BlobId.of(BUCKET, data.username + "/" + "0"));

		return Response.ok().entity(blobs).build();
	}

	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response get(FolderData data) {
		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();

		BlobId blobId = BlobId.of(BUCKET, data.username + "/" + data.photoName);

		Blob blob = db.get(blobId, BlobGetOption.fields(Storage.BlobField.MEDIA_LINK));

		// Blob blob = db.get(BlobId.of(BUCKET, data.username + "/" + "0"));

		return Response.ok().entity(blob.getMediaLink()).build();
	}

}