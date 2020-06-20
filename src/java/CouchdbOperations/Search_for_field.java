/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CouchdbOperations;

import java.util.List;

import org.lightcouch.CouchDbClient;

import au.com.suncoastpc.util.CouchDBUtil;
import au.com.suncoastpc.util.types.CouchDatabase;

import com.google.gson.JsonObject;

/**
 *
 * @author lokesh
 */
public class Search_for_field {
	public JsonObject get_details(String database_name, int objectIndex) {
		CouchDatabase targetDB = CouchDatabase.forName(database_name);
		if (targetDB == null) {
			return null;
		}

		CouchDbClient db = CouchDBUtil.getClient(targetDB); // new CouchDbClient(dbProperties);

		//FIXME:  this method of indexing into the documents list gets quite inefficient performance-wise for high n
		List<JsonObject> list = db.view("_all_docs").skip(objectIndex).limit(1).query(JsonObject.class);
		for (JsonObject json : list) {
			String token = json.get("id").toString().replace("\"", "");
			return db.find(JsonObject.class, token);
		}

		return null;
	}
}
