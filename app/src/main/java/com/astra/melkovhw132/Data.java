package com.astra.melkovhw132;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Data {
    private static final String FILE_NAME = "data.txt";

    /**
     * Check pair "login+password" exists
     * @param context context
     * @param login login
     * @param password password
     * @return true: account exists, false: account not exists
     */
    public static boolean checkExists(Context context, String login, String password, boolean inInternalStorage) {
        Map<String, String> items = getAccounts(context, inInternalStorage);
        for(String item : items.keySet()) {
            if(login.equals(item) && password.equals(items.get(item))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check login exists (pre-registration test)
     * @param context context
     * @param login login
     * @return true: login exists, false: login not exists
     */
    public static boolean checkExists(Context context, String login, boolean inInternalStorage) {
        Map<String, String> items = getAccounts(context,  inInternalStorage);

        for (String item : items.keySet()) {
            if (item.equals(login)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Write content to file in private external storage
     *
     * @param context context (Activity)
     * @param login login
     * @param password password
     */
    public static void addData(Context context, String login, String password, boolean inInternalStorage) {
        File file = getFile(context, inInternalStorage);

        if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }

            FileWriter writer = null;
            try {
                writer = new FileWriter(file, true);
                writer.write(login + ";" + password + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    assert writer != null;
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    private static File getFile(Context context, boolean inInternalStorage) {
        if(inInternalStorage) {
            return new File(context.getFilesDir(), FILE_NAME);
        }

        if (isExternalStorageWritable()) {
            return new File(context.getExternalFilesDir(null), FILE_NAME);
        } else {
            return null;
        }
    }

    private static Map<String, String> getAccounts(Context context, boolean inInternalStorage) {
        Map<String, String> result = new HashMap<>();

        File file = getFile(context, inInternalStorage);

        FileReader reader = null;
        BufferedReader bufferedReader = null;

        try {
            if(!file.exists()) {
                file.createNewFile();
            }

            // read file line by line
            reader = new FileReader(file);
            bufferedReader = new BufferedReader(reader);

            String line = bufferedReader.readLine();
            while (line != null) {
                // split string on login (0) and password (1)denis
                String[] data = line.split(";");

                // add one account (login+password)
                result.put(data[0], data[1]);

                // next denisline in format "login;format"
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Checks if external storage is available for read and write
     * @return flag: storage ready to write
     */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available to at least read
     * @return flag: storage ready to read
     */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}