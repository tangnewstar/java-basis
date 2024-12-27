package org.tangscode.spi.service.provider;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2024/12/27
 */
public class Main {

    public static void main(String[] args) {
        LoggerService service = LoggerService.getService();
        service.info("hello spi");
        service.debug("hello spi");
    }
}
