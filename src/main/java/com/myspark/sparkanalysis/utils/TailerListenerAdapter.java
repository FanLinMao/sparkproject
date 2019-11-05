package com.myspark.sparkanalysis.utils;

/**
 * @author Jayl1n
 * @date 2019/7/5 21:45
 */

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

/**
 * @author Jayl1n
 * @date 2019/7/5 21:36
 */
public class TailerListenerAdapter implements TailerListener {
    /**
     * The tailer will call this method during construction,
     * giving the listener a method of stopping the tailer.
     *
     * @param tailer the tailer.
     */
    public void init(Tailer tailer) {
    }

    /**
     * This method is called if the tailed file is not found.
     */
    public void fileNotFound() {
    }

    /**
     * Called if a file rotation is detected.
     * <p>
     * This method is called before the file is reopened, and fileNotFound may
     * be called if the new file has not yet been created.
     */
    public void fileRotated() {
    }

    /**
     * Handles a line from a Tailer.
     *
     * @param line the line.
     */
    public void handle(String line) {
    }

    /**
     * Handles an Exception .
     *
     * @param ex the exception.
     */
    public void handle(Exception ex) {
    }

}
