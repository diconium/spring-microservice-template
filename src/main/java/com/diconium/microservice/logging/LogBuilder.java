package com.diconium.microservice.logging;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public final class LogBuilder {

    private static final String REQ_ID = "[REQ_ID: ";
    private static final String END = "[END]: ";
    private static final String ERROR = "[ERROR]: ";
    private static final String INPUT = "[INPUT]: ";
    private static final String START = "[START]: ";

    private static Logger log = LogManager.getLogger(LogBuilder.class);

    private LogBuilder() {
    }

    public static void logStart(String className, String id, String method) {
        log.debug(START + className + "." + method + " " + REQ_ID + id + "] ");
    }

    public static void logStartWithInput(String className, String id, String method,
        Map<String, String> args) {
        log.debug(START + className + "." + method + " " + REQ_ID + id + "] ");

        StringBuilder stb = new StringBuilder();
        for (Map.Entry<String, String> entry : args.entrySet()) {
            if (!entry.getKey().equals("Authorization")) {
                stb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" | ");
            }
        }
        log.debug("\t" + INPUT + stb.substring(0, stb.length() - 2));
    }

    public static void logEnd(String className, String id, String method) {
        log.debug(END + className + "." + method + " " + REQ_ID + id + "] ");
    }

    public static void logError(String className, String id, String errorMsg) {
        log.error(ERROR + "(" + className + ") " + errorMsg + " " + REQ_ID + id + "] ");
    }

}
