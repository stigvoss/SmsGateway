/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.vsnt.smsgateway;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 *
 * @author Stig
 */
public class SmsGatewayServer {
    public static void main(String[] args) throws Exception {
        int port = Settings.getInstance().getPort();
        String apiRoot = Settings.getInstance().getApiRoot();
        
        runServer(new SmsGatewayApplication(), port, apiRoot);
    }
    
    public static void runServer(Application serverApplication, int port, String root) throws Exception {
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, port);
        component.getDefaultHost().attach(root, serverApplication);
        component.start();
    }
}
