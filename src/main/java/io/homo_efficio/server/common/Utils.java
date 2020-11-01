package io.homo_efficio.server.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-10-30
 */
public abstract class Utils {

    public static void serverTimeStamp(String msg) {
        System.out.println(buildMessage("SERVER", msg));
    }

    public static void serverTimeStamp(String msg, OutputStream os) {
        try {
            os.write(appendNewline(buildMessage("SERVER", msg)).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clientTimeStamp(String msg) {
        System.out.println(buildMessage("CLIENT", msg));
    }

    public static void clientTimeStamp(String msg, OutputStream os) {
        try {
            os.write(appendNewline(buildMessage("CLIENT", msg)).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String buildMessage(String who, String msg) {
        return String.format("[%s - %15s] %s - %s",
                who,
                Thread.currentThread().getName(),
                LocalDateTime.now(),
                msg);
    }

    private static String appendNewline(String str) {
        return str + System.lineSeparator();
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileOutputStream getCommonFileOutputStream() {
        try {
            return new FileOutputStream("temp.log", true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExecutorService getCommonExecutorService() {
        return Executors.newFixedThreadPool(50);
    }
}
