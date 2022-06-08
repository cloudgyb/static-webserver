package com.github.cloudgyb.webserver.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * SSL/TLS配置
 *
 * @author cloudgyb
 */
public class SSLConfig {
    private final File privateKey;
    private final File cert;

    public SSLConfig(File privateKey, File cert) {
        this.privateKey = privateKey;
        this.cert = cert;
    }

    public File getPrivateKey() {
        return this.privateKey;
    }

    public File getCert() {
        return this.cert;
    }

    public InputStream getPrivateKeyInputStream() throws FileNotFoundException {
        return new FileInputStream(this.privateKey);
    }

    public InputStream getCertInputStream() throws FileNotFoundException {
        return new FileInputStream(this.cert);
    }


    public void validate() {

    }
}
