package pt.oofaround.cronjobs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobGetOption;
import com.google.cloud.storage.StorageOptions;

@SuppressWarnings("serial")
public class CronJobs extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(CronJobs.class.getName());

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	@SuppressWarnings("unused")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {

			CollectionReference flags = db.collection("flag");

			Query query = flags.whereEqualTo(com.google.cloud.firestore.FieldPath.documentId(), "ranking");

			ApiFuture<QuerySnapshot> querySnapshot = query.get();

			boolean flag = false;
			DocumentReference docRef = null;

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				flag = document.getBoolean("flag");
				docRef = document.getReference();
			}

			if (flag) {

				docRef.update("flag", false);

				StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround")
						.build();

				Storage storageDB = storage.getService();

				List<QueryDocumentSnapshot> userDocs = db.collection("users").orderBy("score", Direction.DESCENDING)
						.get().get().getDocuments();

				if (!userDocs.isEmpty()) {

					List<QueryDocumentSnapshot> rankingDocs = db.collection("rankings").orderBy("score").get().get()
							.getDocuments();

					QueryDocumentSnapshot userStorage;
					JSONArray storageArray = new JSONArray();
					JSONObject storageObj;

					for (int i = 0; i < 10 && i < userDocs.size(); i++) {
						userStorage = userDocs.get(i);
						storageObj = new JSONObject();
						storageObj.put("username", userStorage.getString("username"));
						storageObj.put("score", userStorage.getLong("score"));
						storageObj.put("rank", String.valueOf(i+1));

						try {
							BlobId blobId = BlobId.of("oofaround.appspot.com",
									userStorage.getString("username") + "_profile");

							Blob blob = storageDB.get(blobId, BlobGetOption.fields(Storage.BlobField.MEDIA_LINK));

							storageObj.put("image", Base64.getEncoder().encodeToString(blob.getContent()));
						} catch (Exception e) {
							BlobId blobId = BlobId.of("oofaround.appspot.com", "profile_generic.png");

							Blob blob = storageDB.get(blobId, BlobGetOption.fields(Storage.BlobField.MEDIA_LINK));

							String s = Base64.getEncoder().encodeToString(blob.getContent());

							storageObj.put("image",  Base64.getEncoder().encodeToString(blob.getContent()));
						}

						storageArray.put(storageObj);
					}

					BlobId blobId = BlobId.of("oofaround.appspot.com", "topRankArray.json");
					BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

					Blob blob = storageDB.create(blobInfo, storageArray.toString().getBytes(StandardCharsets.UTF_8));

					Map<String, Object> docData;

					int compValue = Integer.compare(userDocs.size(), rankingDocs.size());

					if (compValue == 0) {

						int i = 0;

						DocumentSnapshot user;
						DocumentSnapshot rank;

						for (i = 0; i < rankingDocs.size(); i++) {
							user = userDocs.get(i);
							rank = rankingDocs.get(i);

							docData = new HashMap<>();
							if (user.get("username") != rank.get("username")
									|| user.get("score") != rank.get("score")) {
								docData.put("username", user.get("username"));
								docData.put("score", user.get("score"));

								ApiFuture<WriteResult> future = db.collection("rankings")
										.document(String.valueOf(i + 1)).set(docData, SetOptions.merge());
								future.get();
							}
						}
					} else if (compValue > 0) {

						int i = 0;

						DocumentSnapshot user;
						DocumentSnapshot rank;

						for (i = 0; i < rankingDocs.size(); i++) {
							user = userDocs.get(i);
							rank = rankingDocs.get(i);

							docData = new HashMap<>();
							if (user.get("username") != rank.get("username")
									|| user.get("score") != rank.get("score")) {
								docData.put("username", user.get("username"));
								docData.put("score", user.get("score"));

								ApiFuture<WriteResult> future = db.collection("rankings")
										.document(String.valueOf(i + 1)).set(docData, SetOptions.merge());
								future.get();
							}
						}

						while (i < userDocs.size()) {
							user = userDocs.get(i);
							docData = new HashMap<>();

							docData.put("username", user.get("username"));
							docData.put("score", user.get("score"));

							db.collection("rankings").document(String.valueOf(++i)).set(docData).get();
						}
					} else if (compValue < 0) {

						int i = 0;
						DocumentSnapshot user;
						DocumentSnapshot rank;

						for (i = 0; i < userDocs.size(); i++) {
							user = userDocs.get(i);
							rank = rankingDocs.get(i);

							docData = new HashMap<>();
							if (user.get("username") != rank.get("username")
									|| user.get("score") != rank.get("score")) {
								docData.put("username", user.get("username"));
								docData.put("score", user.get("score"));

								ApiFuture<WriteResult> future = db.collection("rankings")
										.document(String.valueOf(i + 1)).set(docData, SetOptions.merge());
								future.get();
							}
						}

						while (i < rankingDocs.size()) {
							db.collection("rankings").document(String.valueOf(i++)).delete().get();
						}
					}
				}
			}

			_logger.info("Cron Job has been executed");
		} catch (

		Exception ex) {
			_logger.info("Cron Job has failed");

		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
