import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by avorona on 01.10.15.
 */
public class MainServer {
    public static void main(String[] args) {
        SSLServerSocket server = null;
        System.setProperty("line.separator", "\r\n");
        try {
            server = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(443);
            SSLContext ctx = SSLContext.getInstance("SSL");
//
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(new FileInputStream("/usr/lib/jvm/java-8-oracle/jre/lib/security/jssecacerts"), null);
//
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            kmf.init(keyStore, null);
//
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            tmf.init(keyStore);
//
            ctx.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, null);

            HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((s1, sslSession) -> true);

            while (true) {
                Socket socket = server.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));

                reader.lines().forEach(s -> {
                    if (s.contains("OPTIONS")) {
                        try {
                            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                            writer.println("HTTP/1.1 OK 200");
                            writer.println("Access-Control-Allow-Origin: *");
                            writer.println("Access-Control-Allow-Origin: POST, GET, PUT, OPTIONS, DELETE");
                            writer.println("Access-Control-Max-Age: 3600");
                            writer.println("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept");
                            writer.println();
                            writer.flush();
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(s);
                    }
                });
                reader.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
