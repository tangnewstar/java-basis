package org.tangscode.spi.test;

import org.tangscode.spi.service.provider.LoggerService;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2024/12/27
 */
public class Main {

    public static void main(String[] args) {
        LoggerService loggerService = LoggerService.getService();
        loggerService.info("hello");
        loggerService.debug("test java spi");
    }
}
