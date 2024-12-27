package org.tangscode.spi.service.provider.impl;

import org.tangscode.spi.service.provider.Logger;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2024/12/27
 */
public class Log4J implements Logger {
    @Override
    public void info(String s) {
        System.out.println("log4j info: " + s);
    }

    @Override
    public void debug(String s) {
        System.out.println("log4j debug: " + s);
    }
}
