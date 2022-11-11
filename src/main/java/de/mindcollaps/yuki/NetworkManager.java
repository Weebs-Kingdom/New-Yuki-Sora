package de.mindcollaps.yuki;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@YukiLogModule(name = "Network Manager")
public class NetworkManager {

    private final YukiSora yukiSora;

    public NetworkManager(YukiSora engine) {
        this.yukiSora = engine;
    }

    public String post(String path, String json, String apiToken) {
        return req(path, json, apiToken, "POST");
    }

    public String patch(String path, String json, String apiToken) {
        return req(path, json, apiToken, "PATCH");
    }

    public String delete(String path, String json, String apiToken) {
        return req(path, json, apiToken, "DELETE");
    }


    private String req(String path, String json, String apiToken, String methode) {
        if (json == null)
            return null;
        String log = "REQ : " + path + " Methode: " + methode + " req: " + json;
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) makeConnection(path);
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("Failed to establish connection\n" + log).trace(e));
            return null;
        }
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        if (apiToken != null) {
            addApiToken(apiToken, connection);
        }
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        try {
            if (methode.equals("PATCH")) {
                connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                //connection.setRequestProperty("X-HTTP-Method", "PATCH");
                connection.setRequestMethod("POST");
            } else
                connection.setRequestMethod(methode);
        } catch (ProtocolException e) {
            YukiLogger.log(new YukiLogInfo("Failed to establish connection\n" + log).trace(e));
            return null;
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);
        try {
            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
            char[] ar = json.toCharArray();
            os.write(json);
            os.flush();
            os.close();
        } catch (ConnectException e) {
            YukiLogger.log(new YukiLogInfo("Requested server is not available | Timeout\n" + log).trace(e));
            return null;
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("Error in sending data!\n" + log).trace(e));
            return null;
        }

        return readResponse(log, connection);
    }

    public String get(String path, String apiToken) {
        String log = "REQ : " + path + " Methode: " + "GET";
        try {
            HttpURLConnection c = (HttpURLConnection) makeConnection(path);
            if (apiToken != null) {
                addApiToken(apiToken, c);
                c.setRequestProperty("Accept", "application/json");
            }
            return readResponse(log, c);
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("Error get method!\n" + log).trace(e));
            return null;
        }
    }

    private String readResponse(String requestLog, HttpURLConnection connection) {
        String responseString = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        } catch (ConnectException e) {
            YukiLogger.log(new YukiLogInfo("[Network Manager] Requested server is not available | Timeout\n" + requestLog).trace(e));
        } catch (FileNotFoundException e) {
            YukiLogger.log(new YukiLogInfo("A request responded with 404\n" + requestLog).debug());
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("Error in response reading!\n" + requestLog).trace(e));
        }

        try {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            responseString = response.toString();
        } catch (Exception e) {

        }

        if (YukiProperties.getApplicationSettings().fineDebug)
            YukiLogger.log(new YukiLogInfo(requestLog + "\nRES : " + responseString).debug());
        return responseString;
    }

    private Object makeConnection(String path) throws Exception {
        Object c;
        if (path.startsWith("https"))
            c = new URL(path).openConnection();
        else
            c = new URL(path).openConnection();
        return c;
    }

    private void addApiToken(String token, HttpURLConnection c) {
        c.setRequestProperty("api-token", token);
    }
}
