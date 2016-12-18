/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.vsnt.smsgateway;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Stig
 */
public class BindingsReader {

    private static BindingsReader instance = null;

    private JSONArray bindings = null;

    private BindingsReader() throws IOException, FileNotFoundException, JSONException {
        load();
    }

    public static BindingsReader getInstance() throws IOException, FileNotFoundException, JSONException {
        if (BindingsReader.instance == null) {
            BindingsReader.instance = new BindingsReader();
        }
        return BindingsReader.instance;
    }

    private void load() throws FileNotFoundException, IOException, JSONException {
        FileReader reader = new FileReader("bindings.json");
        String bindings = read(reader);
        this.bindings = new JSONArray(bindings);
    }

    private String read(InputStreamReader reader) throws IOException {
        String result = "";
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result = result + line;
        }
        return result.toLowerCase();
    }

    public void addBinding(String keyword, String url) throws JSONException, FileNotFoundException {
        this.bindings.put(new JSONObject().put(keyword, url));

        try (PrintWriter writer = new PrintWriter("bindings.json")) {
            writer.write(this.bindings.toString());
        }
    }

    public boolean hasBinding(String keyword) throws JSONException {
        for (int i = 0; i < this.bindings.length(); i++) {
            JSONObject obj = this.bindings.getJSONObject(i);
            if (obj.has(keyword)) {
                return true;
            }
        }

        return false;
    }

    public String getBinding(String keyword) throws JSONException {

        for (int i = 0; i < this.bindings.length(); i++) {
            JSONObject obj = this.bindings.getJSONObject(i);
            if (obj.has(keyword)) {
                return obj.getString(keyword);
            }
        }

        for (int i = 0; i < this.bindings.length(); i++) {
            JSONObject obj = this.bindings.getJSONObject(i);
            if (obj.has("*")) {
                return obj.getString("*");
            }
        }

        return null;
    }

    public Object deleteBinding(String keyword) throws JSONException, FileNotFoundException {
        for (int i = 0; i < this.bindings.length(); i++) {
            JSONObject obj = this.bindings.getJSONObject(i);
            if (obj.has(keyword)) {
                Object removed = this.bindings.remove(i);
                try (PrintWriter writer = new PrintWriter("bindings.json")) {
                    writer.write(this.bindings.toString());
                }
                return removed;
            }
        }

        return null;
    }
}
