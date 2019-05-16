package pt.oofaround.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;

import pt.oofaround.resources.ImageResource;

public class MediaSupport {

	

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ImageResource.class.getName());

	private static final String BUCKET = "oofaround.appspot.com";

	public MediaSupport() {
	}

	@SuppressWarnings("unused")
	public static void uploadImage(String name, byte[] image) {
		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();
		
		BlobId blobId = BlobId.of(BUCKET, name);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
				.setContentType("image/jpeg").build();

		Blob blob = db.create(blobInfo, image);
	}
}
