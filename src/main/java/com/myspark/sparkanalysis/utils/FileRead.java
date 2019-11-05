package com.myspark.sparkanalysis.utils;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import java.io.File;


/**
 * @author Jayl1n
 * @date 2019/7/5 21:36
 */
public class FileRead {

    private static void monitor(String inputFile, int sleepInterval) {

        System.out.println("start");
        TailerListener listener = new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                System.out.println(line);
            }
        };
        Tailer tailer = new Tailer(new File(inputFile), listener, sleepInterval, true);
        tailer.run();

    }

    public static void main(String[] args) {
        monitor("c:\\Users\\Jaylin\\1.txt", 100);
    }
}


