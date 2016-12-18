/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.vsnt.smsgateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Stig
 */
public class GammuSmsDispatcher implements ISmsDispatcher {
    
    @Override
    public boolean send(String recipient, String message) throws IllegalArgumentException, IOException, InterruptedException {
        if (recipient == null) {
            throw new IllegalArgumentException("No recipient specified.");
        }
        if (recipient.length() == 0) {
            throw new IllegalArgumentException("No recipient specified.");
        }
        if (!recipient.matches("^(\\+45|)[0-9]{8}$")) {
            throw new IllegalArgumentException("Invalid recipient(s) specified.");
        }
        if (message == null) {
            throw new IllegalArgumentException("No message specified.");
        }
        
        String gammu = Settings.getInstance().getGammu();

        String[] command = new String[] {
            gammu,
            "TEXT",
            recipient,
            "-text",
            message
        };
        
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();
        process.waitFor();

        int exit = process.exitValue();
        if (exit != 0) {
            throw new IOException(String.format("Gammu error code: %s. (%s)", exit, getOutput(process)));
        }

        return true;
    }

    private String getOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }
}
