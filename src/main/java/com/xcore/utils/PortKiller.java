package com.xcore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PortKiller {
    public static void killProcessOnPort(int port) {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.printf("os: %s%n", os);
        String processCommand;
        if (os.contains("win")) {
            processCommand = "cmd /c netstat -ano | findstr :" + port;
        } else {
            processCommand = "lsof -t -i:" + port;
        }

        try {
            Process process = Runtime.getRuntime().exec(processCommand);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String pid = line.trim();
                killProcess(pid);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void killProcess(String pid) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String killCommand;
            if (os.contains("win")) {
                killCommand = "taskkill /F /PID " + pid;
            } else {
                killCommand = "kill -9 " + pid;
            }
            Process killProcess = Runtime.getRuntime().exec(killCommand);
            killProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
