/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CouchdbOperations;

import org.lightcouch.CouchDbClient;

import au.com.suncoastpc.util.CouchDBUtil;
import au.com.suncoastpc.util.types.CouchDatabase;

import com.google.gson.JsonObject;

/**
 *
 * @author lokesh
 */
public class Update_details {

	public void update(String database_name, String id, String levelValue, String nameValue) {
		CouchDatabase targetDB = CouchDatabase.forName(database_name);
		if (targetDB == null) {
			return;
		}

		CouchDbClient db = CouchDBUtil.getClient(targetDB); // new CouchDbClient(dbProperties);
		try {
			JsonObject json1 = db.find(JsonObject.class, id);
			json1.addProperty("Levels", levelValue);
			json1.addProperty("Names", nameValue);
			db.update(json1);
		} catch (Exception e) {
			JsonObject json1 = new JsonObject();
			json1.addProperty("Levels", levelValue);
			json1.addProperty("Names", nameValue);
			json1.addProperty("_id", id);
			db.save(json1);
		}

		// aroth: this step should be unnecessary (and is probably even wrong)
		// CouchDBUtil.shutdownClient(targetDB);
		// db.shutdown();

	}
}
