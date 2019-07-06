package pt.oofaround.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobGetOption;
import com.google.cloud.storage.StorageOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import pt.oofaround.support.MediaSupport;
import pt.oofaround.util.AuthToken;
import pt.oofaround.util.AuthenticationTool;
import pt.oofaround.util.CuponData;

@Path("/cupon")
@Produces(MediaType.APPLICATION_JSON)
public class CuponResource {

	FirestoreOptions firestore = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();
	private final Firestore db = firestore.getService();

	public CuponResource() {
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCupon(CuponData data)
			throws InterruptedException, ExecutionException, WriterException, IOException {
		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "createCupon")) {
			CollectionReference cupons = db.collection("cupons");

			for (@SuppressWarnings("unused")
			QueryDocumentSnapshot document : cupons.whereEqualTo("locationName", data.locationName)
					.whereEqualTo("value", data.value).get().get().getDocuments()) {
				return Response.status(Status.FOUND).build();
			}

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(data.locationName + " " + data.value + " " + data.description,
					BarcodeFormat.QR_CODE, 250, 250);

			ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
			MediaSupport.uploadImage(data.locationName.trim() + String.valueOf(data.value) + "_qrcode",
					pngOutputStream.toByteArray());

			Map<String, Object> docData = new HashMap<String, Object>();
			docData.put("locationName", data.locationName);
			docData.put("value", data.value);
			docData.put("latitude", data.latitude);
			docData.put("longitude", data.longitude);
			docData.put("description", data.description);
			docData.put("region", data.region);

			cupons.document().set(docData);

			AuthToken at = new AuthToken(data.usernameR, data.role);

			JSONObject res = new JSONObject();
			res.put("tokenID", at.tokenID);

			return Response.ok().entity(res.toString()).build();
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@POST
	@Path("/get")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCupon(CuponData data) throws InterruptedException, ExecutionException {
		// alterar createCupon para getCupon
		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "createCupon")) {
			List<QueryDocumentSnapshot> cuponList = db.collection("cupons")
					.whereEqualTo("locationName", data.locationName).whereEqualTo("value", data.value).get().get()
					.getDocuments();
			if (cuponList.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			} else {
				JSONObject res = new JSONObject();

				for (QueryDocumentSnapshot document : cuponList) {
					res.put("locationName", document.getString("locationName"));
					res.put("latitude", document.getString("latitude"));
					res.put("longitude", document.getString("longitude"));
					res.put("region", document.getString("region"));
					res.put("description", document.getString("description"));
					res.put("value", document.getDouble("value"));
					res.put("qrlink", data.locationName.trim() + String.valueOf(data.value) + "_qrcode");
				}

				AuthToken at = new AuthToken(data.usernameR, data.role);
				res.put("tokenID", at.tokenID);

				return Response.ok().entity(res.toString()).build();
			}
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listByRegionCat(CuponData data) throws InterruptedException, ExecutionException {
		if (AuthenticationTool.authenticate(data.tokenID, data.usernameR, data.role, "listByRegionCat")) {
			ApiFuture<QuerySnapshot> querySnapshot;

			if (data.region.equalsIgnoreCase("")) {
				querySnapshot = db.collection("cupons").get();
			} else {
				querySnapshot = db.collection("cupons").whereEqualTo("region", data.region).get();
			}

			JSONArray jArr = new JSONArray();
			JSONObject jObj;

			StorageOptions storage = StorageOptions.getDefaultInstance().toBuilder().setProjectId("oofaround").build();

			Storage storageDB = storage.getService();

			BlobId blobId;
			Blob blob;

			for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
				jObj = new JSONObject();
				jObj.put("locationName", document.get("locationName"));
				jObj.put("value", document.get("value"));
				jObj.put("latitude", document.get("latitude"));
				jObj.put("longitude", document.get("longitude"));
				jObj.put("description", document.get("description"));
				jObj.put("region", document.get("region"));

				blobId = BlobId.of("oofaround.appspot.com",
						document.getString("locationName").trim() + String.valueOf(document.get("value")) + "_qrcode");
				blob = storageDB.get(blobId, BlobGetOption.fields(Storage.BlobField.MEDIA_LINK));

				jObj.put("qrCode", blob.getContent().toString());

				jArr.put(jObj);
			}

			jObj = new JSONObject();
			jObj.put("cupons", jArr);

			AuthToken at = new AuthToken(data.usernameR, data.role);
			jObj.put("tokenID", at.tokenID);

			return Response.ok().build();
		} else
			return Response.status(Status.FORBIDDEN).build();
	}

}