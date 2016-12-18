/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.vsnt.smsgateway;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 *
 * @author Stig
 */
public class SmsGatewayApplication extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/send", SmsResource.class);
        router.attach("/subscribe/{keyword}", SubscribeResource.class);

        return router;
    }
    
}
