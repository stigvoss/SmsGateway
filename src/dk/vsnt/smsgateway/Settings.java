/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.vsnt.smsgateway;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Stig
 */
public class Settings {
    

    private static Settings instance = null;

    private Properties properties = null;
    private InputStream inputStream = null;

    private final String settingsFile = "SmsGateway.properties";
    
    private int port;
    private final String TAG_PORT = "Port";
    private final int PORT_DEFAULT = 8080;
    
    private String gammu;
    private final String TAG_GAMMU = "Gammu";
    private final String GAMMU_DEFAULT = "/usr/bin/gammu";
    
    private String apiRoot;
    private final String TAG_API_ROOT = "ApiRoot";
    private final String API_ROOT_DEFAULT = "/sms";

    private Settings() throws IOException {
        try {
            initialize();
            load();
        } catch (IOException ex) {
            System.out.printf("Unable to access %s, using defaults.\n", this.settingsFile);
            defaults();
        } finally {
            dispose();
        }
    }

    public static Settings getInstance() throws IOException {
        if (Settings.instance == null) {
            Settings.instance = new Settings();
        }

        return Settings.instance;
    }

    private void initialize() throws IOException {
        this.properties = new Properties();
        this.inputStream = new FileInputStream(this.settingsFile);
        this.properties.load(this.inputStream);
    }

    private void dispose() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
        }
    }

    private void load() {
        this.port = Integer.parseInt(this.properties.getProperty(this.TAG_PORT, "" + this.PORT_DEFAULT));
        this.gammu = this.properties.getProperty(this.TAG_GAMMU, this.GAMMU_DEFAULT);
        this.apiRoot = this.properties.getProperty(this.TAG_API_ROOT, this.API_ROOT_DEFAULT);
    }
    
    private void defaults() {
        this.port = this.PORT_DEFAULT;
        this.gammu = this.GAMMU_DEFAULT;
        this.apiRoot = this.API_ROOT_DEFAULT;
    }

    public int getPort() {
        return this.port;
    }
    
    public String getGammu() {
        return this.gammu;
    }
    
    public String getApiRoot() {
        return this.apiRoot;
    }
}
