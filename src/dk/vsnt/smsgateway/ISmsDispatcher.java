/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.vsnt.smsgateway;

import java.io.IOException;

/**
 *
 * @author Stig
 */
public interface ISmsDispatcher {
    boolean send(String recipient, String message) throws IllegalArgumentException, IOException, InterruptedException;
}
