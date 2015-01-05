package uk.bl.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import play.Logger;

public class HttpBasicAuth {

public static void downloadFileWithAuth(String urlStr, String user, String pass, String outFilePath) {
    try {
        URL url = new URL(urlStr);
        String authStr = user + ":" + pass;
        String authEncoded = Base64.encodeBytes(authStr.getBytes());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + authEncoded);

        File file = new File(outFilePath);
        InputStream in = (InputStream) connection.getInputStream();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        for (int b; (b = in.read()) != -1;) {
            out.write(b);
        }
        out.close();
        in.close();
    }
    catch (Exception e) {
		Logger.debug("downloadFileWithAuth() error: " + e);
        e.printStackTrace();
    }
}
}