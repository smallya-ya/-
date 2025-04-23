package com.ruoyi.common.utils.battle;

import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @author hongjiasen
 */
public class AliyunEmqxSSLFactory {

    public static SSLSocketFactory getSSLSocktet(String caPath, String crtPath, String keyPath, String password) throws Exception {
        CertificateFactory cAf = CertificateFactory.getInstance("X.509");
        ClassPathResource caResource = new ClassPathResource(caPath);
        X509Certificate ca = (X509Certificate) cAf.generateCertificate(caResource.getInputStream());
        KeyStore caKs = KeyStore.getInstance("JKS");
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", ca);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(caKs);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ClassPathResource crtResource = new ClassPathResource(crtPath);
        X509Certificate caCert = (X509Certificate) cf.generateCertificate(crtResource.getInputStream());

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", caCert);
        ks.setKeyEntry("private-key", getPrivateKey(keyPath), password.toCharArray(),
                new java.security.cert.Certificate[]{caCert}  );
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
        kmf.init(ks, password.toCharArray());

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        return context.getSocketFactory();
    }

    public static PrivateKey getPrivateKey(String path) throws Exception {
        byte[] buffer = Base64.getMimeDecoder().decode(getPem(path));

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);

    }

    private static String getPem(String path) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }
        br.close();
        return sb.toString();
    }

}
