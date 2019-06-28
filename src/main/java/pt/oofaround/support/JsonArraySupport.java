package pt.oofaround.support;

import java.util.Base64;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage.BlobGetOption;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonArraySupport {

	public JsonArraySupport() {
	}

	public static JsonArray createOnePropArray(List<QueryDocumentSnapshot> docs, String property) {

		JsonArray array = new JsonArray();
		JsonObject jsObj;

		if (docs.isEmpty())
			throw new NotFoundException();
		for (QueryDocumentSnapshot document1 : docs) {
			jsObj = new JsonObject();
			jsObj.addProperty(property, document1.get(property).toString());
			array.add(jsObj);
		}
		return array;
	}

	public static JsonArray createOnePropArrayFromFirestoreArray(List<String> docs, String property) {

		JsonArray array = new JsonArray();
		JsonObject jsObj;

		for (String document1 : docs) {
			jsObj = new JsonObject();
			jsObj.addProperty(property, document1);
			array.add(jsObj);
		}
		return array;
	}

	public static JSONArray createLocationPropArray(List<QueryDocumentSnapshot> docs, String property1,
			String property2, String property3, String property4, String property5, String property6, String property7,
			String property8, String property9, String property10) {

		JSONArray array = new JSONArray();
		JSONObject jsObj;

		StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

		Storage db = storage.getService();

		BlobId blobId;

		Blob blob;

		if (docs.isEmpty())
			throw new NotFoundException();
		for (QueryDocumentSnapshot document1 : docs) {
			jsObj = new JSONObject();
			jsObj.put(property1, document1.get(property1).toString());
			jsObj.put(property2, document1.get(property2).toString());
			jsObj.put(property3, document1.get(property3).toString());
			jsObj.put(property4, document1.get(property4).toString());
			jsObj.put(property5, document1.get(property5).toString());
			jsObj.put(property6, document1.get(property6).toString());
			jsObj.put(property7, document1.get(property7).toString());
			// jsObj.put(property8, document1.get(property8).toString());
			// jsObj.put(property9, document1.get(property9).toString());

			blobId = BlobId.of("oofaround.appspot.com", property1);

			blob = db.get(blobId, BlobGetOption.fields(Storage.BlobField.MEDIA_LINK));

			jsObj.put(property10, Base64.getEncoder().encodeToString(blob.getContent()));

			array.put(jsObj);
		}
		return array;
	}

	public static JsonArray createTwoPropArray(List<QueryDocumentSnapshot> docs, String property1, String property2) {

		JsonArray array = new JsonArray();
		JsonObject jsObj;

		if (docs.isEmpty())
			throw new NotFoundException();
		for (QueryDocumentSnapshot document1 : docs) {
			jsObj = new JsonObject();
			jsObj.addProperty(property1, document1.get(property1).toString());
			jsObj.addProperty(property2, document1.get(property2).toString());
			array.add(jsObj);
		}
		return array;
	}

	public static JsonArray createThreePropArray(List<QueryDocumentSnapshot> docs, String property1, String property2,
			String property3) {

		JsonArray array = new JsonArray();
		JsonObject jsObj;

		if (docs.isEmpty())
			throw new NotFoundException();
		for (QueryDocumentSnapshot document1 : docs) {
			jsObj = new JsonObject();
			jsObj.addProperty(property1, document1.get(property1).toString());
			jsObj.addProperty(property2, document1.get(property2).toString());
			jsObj.addProperty(property3, document1.get(property3).toString());
			array.add(jsObj);
		}
		return array;
	}

}
