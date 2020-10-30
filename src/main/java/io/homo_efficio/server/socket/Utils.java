package io.homo_efficio.server.socket;

import java.time.LocalDateTime;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-10-30
 */
public abstract class Utils {

    public static void serverTimeStamp(String msg) {
        System.out.println(buildMessage(msg));
    }

    public static void clientTimeStamp(String msg) {
        System.out.println(buildMessage(msg));
    }

    private static String buildMessage(String msg) {
        return String.format("[%s] %s - %s",
                Thread.currentThread().getName(),
                LocalDateTime.now(),
                msg);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
