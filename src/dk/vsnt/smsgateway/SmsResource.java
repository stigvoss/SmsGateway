/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.vsnt.smsgateway;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 *
 * @author Stig
 */
public class SmsResource extends ServerResource {

    String[] recipients = null;
    String message = null;

    JSONObject result = new JSONObject();

    @Post("json")
    public Representation put(JsonRepresentation json) throws JSONException {
        initialize(json);

        int sentMessages = 0;
        ISmsDispatcher dispatcher = new GammuSmsDispatcher();

        for (String recipient : this.recipients) {

            try {

                if (dispatcher.send(recipient, message)) {
                    sentMessages++;
                }

            } catch (IllegalArgumentException ex) {
                error(Status.CLIENT_ERROR_PRECONDITION_FAILED, ex);
            } catch (IOException | InterruptedException ex) {
                error(Status.SERVER_ERROR_INTERNAL, ex);
            }
        }

        if (sentMessages == this.recipients.length) {
            success("The message(s) was successfully enqueued.");
        } else if (sentMessages > 0) {
            int failedMessages = this.recipients.length - sentMessages;
            error(Status.SUCCESS_PARTIAL_CONTENT, String.format("%s of %s messages failed to enqueue, remaining messages was enqueued.", failedMessages, this.recipients.length));
        }

        return result();
    }

    private void initialize(JsonRepresentation representation) throws JSONException {
        JSONObject entity = representation.getJsonObject();
        JSONArray recipients = entity.getJSONArray("recipients");

        this.recipients = new String[recipients.length()];

        for (int i = 0; i < recipients.length(); i++) {
            this.recipients[i] = recipients.getString(i);
        }

        this.message = entity.getString("message");
    }

    private Representation result() {
        return new StringRepresentation(this.result.toString(), MediaType.APPLICATION_JSON);
    }

    private void error(Status status, String message) throws JSONException {
        result.put("Description", message);
        setStatus(status);
    }

    private void error(Status status, Exception ex) throws JSONException {
        result.put("Exception", ex.getClass().getName());
        result.put("Description", ex.getMessage());
        setStatus(status);
    }

    private void success(String message) throws JSONException {
        success(Status.SUCCESS_OK, message);
    }

    private void success(Status status, String message) throws JSONException {
        result.put("Result", message);
        setStatus(status);
    }
}
