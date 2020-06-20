/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CouchdbOperations;

import java.util.HashMap;
import java.util.List;

import org.lightcouch.CouchDbClient;

import au.com.suncoastpc.util.CouchDBUtil;
import au.com.suncoastpc.util.types.CouchDatabase;

import com.google.gson.JsonObject;

/**
 *
 * @author lokesh
 */
public class Get_details {
	public JsonObject read_db(CouchDatabase targetDB, String id) {
		if (targetDB == null) {
			return null;
		}

		JsonObject output = new JsonObject();
		CouchDbClient db = CouchDBUtil.getClient(targetDB); // new CouchDbClient(dbProperties);
		try {
			output = db.find(JsonObject.class, id);
			System.out.println(output);
			output.addProperty("result", "success");
		} catch (Exception ex) {
			System.out.println(ex);
			output.addProperty("result", "failed");
		}
		return output;
	}

	public JsonObject read_db(String database_name, String id) {
		CouchDatabase targetDB = CouchDatabase.forName(database_name);
		return read_db(targetDB, id);
	}

	public HashMap<Integer, String> getTop50() {
		HashMap<Integer, String> highPlayers = new HashMap<Integer, String>();

		CouchDbClient db = CouchDBUtil.getClient(CouchDatabase.TOP_50_PLAYERS); // new CouchDbClient(dbProperties);

		List<JsonObject> list = db.view("_all_docs").query(JsonObject.class);

		for (JsonObject json : list) {
			String token = json.get("id").toString().replace("\"", "");
			JsonObject json1 = db.find(JsonObject.class, token);
			String player_level = json1.get("Levels").toString().replace("\"", "");
			String names = json1.get("Names").toString().replace("\"", "");
			highPlayers.put(Integer.parseInt(player_level), names);

		}

		return highPlayers;
	}
}
