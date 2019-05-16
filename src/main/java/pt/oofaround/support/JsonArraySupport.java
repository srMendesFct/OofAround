package pt.oofaround.support;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import com.google.cloud.firestore.QueryDocumentSnapshot;
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
	
	if (docs.isEmpty())
		throw new NotFoundException();
	for (String document1 : docs) {
		jsObj = new JsonObject();
		jsObj.addProperty(property, document1);
		array.add(jsObj);
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
	
}
