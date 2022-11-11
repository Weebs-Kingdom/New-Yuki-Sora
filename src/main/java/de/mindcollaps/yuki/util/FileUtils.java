package de.mindcollaps.yuki.util;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

@YukiLogModule(name = "File Utils")
public class FileUtils {

    public static final String home = System.getProperty("user.dir") + "/yuki";
    private static final JSONParser parser = new JSONParser();

    @SuppressWarnings("SameReturnValue")
    public static String getHome() {
        return home;
    }

    public static boolean deleteFile(String file) {
        File f = new File(file);
        return f.delete();
    }

    public static Object loadObject(String path) throws Exception {
        String filePath = new File(path).getAbsolutePath();
        File file = new File(filePath);
        FileInputStream stream = null;
        ObjectInputStream objStream = null;
        Object obj = null;
        if (!file.exists()) {
            throw new Exception("File was never created!");
        }
        stream = new FileInputStream(file);
        objStream = new ObjectInputStream(stream);
        obj = objStream.readObject();
        objStream.close();
        stream.close();
        return obj;
    }

    public static void saveObject(String path, Object obj) throws Exception {
        String filePath = new File(path).getAbsolutePath();
        File file = new File(filePath);
        FileOutputStream stream = null;
        ObjectOutputStream objStream = null;
        createFileRootAndFile(file);
        stream = new FileOutputStream(file);
        objStream = new ObjectOutputStream(stream);
        objStream.writeObject(obj);
        objStream.flush();
        objStream.close();
        stream.close();
    }

    public static JSONObject loadJsonFile(String path) throws Exception {
        File file = new File(path);
        JSONObject object;
        try {
            Reader reader = new FileReader(file.getAbsolutePath());
            object = (JSONObject) parser.parse(reader);
            reader.close();
        } catch (Exception e) {
            throw new Exception("File load error");
        }
        return object;
    }

    public static JSONObject convertStringToJson(String json) {
        JSONObject object = null;
        try {
            object = (JSONObject) parser.parse(json);
        } catch (ParseException ignored) {
        }
        return object;
    }

    public static String convertJsonToString(JSONObject object) {
        if (object == null)
            return null;
        return object.toJSONString();
    }

    public static void saveJsonFile(String path, JSONObject object) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(object.toJSONString());
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    public static void createFileRootAndFile(File file) {
        String pas = file.getAbsolutePath().replace("\\", "/");
        String[] path = pas.split("/");
        String pat = path[0];
        for (int i = 1; i < path.length - 1; i++) {
            pat = pat + "/" + path[i];
        }
        File dir = new File(pat);
        if (!dir.mkdirs()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                new YukiLogInfo("Can't create file dirs").trace(e).log();
            }
        }
        createFiles(file);
    }

    private static void createFiles(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                new YukiLogInfo("Can't create file dirs").trace(e).log();
            }
        }
    }
}