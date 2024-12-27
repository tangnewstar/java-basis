package org.tangscode.spi.service.provider.impl;

import org.tangscode.spi.service.provider.Logger;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2024/12/27
 */
public class Logback implements Logger {
    @Override
    public void info(String s) {
        System.out.println("Logback info：" + s);
    }

    @Override
    public void debug(String s) {
        System.out.println("Logback debug: " + s);
    }
}
