package org.tangscode.web;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/6
 */
public interface Processor {
    void callback(Request request, Response response);
}
