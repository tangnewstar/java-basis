package org.tangscode.spi.service.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2024/12/27
 */
public class LoggerService {
    private static final LoggerService SERVICE = new LoggerService();
    private final Logger logger;
    private final List<Logger> loggerList;

    private LoggerService() {
        ServiceLoader<Logger> loader = ServiceLoader.load(Logger.class);
        List<Logger> list = new ArrayList<>();
        for (Logger log: loader) {
            list.add(log);
        }
        loggerList = list;
        logger = loggerList.stream().findFirst().orElse(null);
    }

    public static LoggerService getService() {
        return SERVICE;
    }

    public void info(String msg) {
        if (logger == null) {
            System.out.println("info level no Logger service provider");
        } else {
            logger.info(msg);
        }
    }

    public void debug(String msg) {
        if (loggerList.isEmpty()) {
            System.out.println("debug level no Logger service provider");
        } else {
            loggerList.forEach(log -> log.debug(msg));
        }
    }
}
