package pt.oofaround.cronjobs;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

@SuppressWarnings("serial")
public class RecoveryCron extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(CronJobs.class.getName());

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	@SuppressWarnings("unused")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {

			CollectionReference flags = db.collection("flag");

			Query query = flags.whereEqualTo(com.google.cloud.firestore.FieldPath.documentId(), "ranking");

			ApiFuture<QuerySnapshot> querySnapshot = db.collection("flag")
					.whereEqualTo(com.google.cloud.firestore.FieldPath.documentId(), "recovery").get();

			boolean flag = false;
			DocumentReference docRef = null;

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				flag = document.getBoolean("flag");
				docRef = document.getReference();
			}

			if (flag) {

				docRef.update("flag", false);

				List<QueryDocumentSnapshot> recoveryDocs = db.collection("recovery").get().get().getDocuments();

				if (!recoveryDocs.isEmpty()) {
					for (QueryDocumentSnapshot document : recoveryDocs) {
						if (document.getLong("expires") < System.currentTimeMillis())
							document.getReference().delete();
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
