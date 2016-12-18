/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.vsnt.smsgateway;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

/**
 *
 * @author Stig
 */
public class SubscribeResource extends ServerResource {

    JSONObject result = new JSONObject();

    @Put("json")
    public Representation put(JsonRepresentation json) throws JSONException, IOException {
        JSONObject entity = json.getJsonObject();

        String keyword = ((String) getRequestAttributes().get("keyword")).toLowerCase();
        String url = entity.getString("url");

        if (!new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS).isValid(url)) {
            error(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid URL.");
            return result();
        }
        if (BindingsReader.getInstance().hasBinding(keyword)) {
            error(Status.SERVER_ERROR_INTERNAL, "Binding already exists.");
            return result();
        }

        BindingsReader.getInstance().addBinding(keyword, url);
        success("Keyword binding successfully added.");

        return result();
    }

    @Delete("json")
    public Representation remove() throws JSONException, IOException {
        String keyword = ((String) getRequestAttributes().get("keyword")).toLowerCase();

        if (BindingsReader.getInstance().deleteBinding(keyword) != null) {
            success("Keyword binding successfully deleted.");
        } else {
            error(Status.SERVER_ERROR_INTERNAL, "Keyword binding not found.");
        }

        return result();
    }

    private Representation result() {
        return new StringRepresentation(this.result.toString(), MediaType.APPLICATION_JSON);
    }

    private void success(String message) throws JSONException {
        success(Status.SUCCESS_OK, message);
    }

    private void error(Status status, String message) throws JSONException {
        result.put("Result", message);
        setStatus(status);
    }

    private void success(Status status, String message) throws JSONException {
        result.put("Result", message);
        setStatus(status);
    }
}
