package pt.oofaround.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
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
					BarcodeFormat.QR_CODE, 300, 300);

			ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
			MediaSupport.uploadImage(data.locationName + String.valueOf(data.value) + "_qrcode",
					pngOutputStream.toByteArray());

			Map<String, Object> docData = new HashMap<String, Object>();
			docData.put("locationName", data.locationName);
			docData.put("value", data.value);
			docData.put("latitude", data.latitude);
			docData.put("longitude", data.longitude);
			docData.put("description", data.description);

			cupons.document().set(docData);

			AuthToken at = new AuthToken(data.usernameR, data.role);

			JSONObject res = new JSONObject();
			res.put("tokenID", at.tokenID);

			return Response.ok().entity(res.toString()).build();
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

}
