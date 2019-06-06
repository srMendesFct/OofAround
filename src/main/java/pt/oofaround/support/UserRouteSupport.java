package pt.oofaround.support;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class UserRouteSupport {

	public static int addRouteToProfile(String email, String routeName, String[] existingRoutes) {
		
		for (int i = 0; i < existingRoutes.length; i++) {
			if(existingRoutes[i].equalsIgnoreCase(routeName))
				return 2;
		}
		
		FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
		final Firestore db = firestore.getService();
		
		DocumentReference docRef = db.collection("users").document(email);
		
		
		
		return 1;
	}
	
}
