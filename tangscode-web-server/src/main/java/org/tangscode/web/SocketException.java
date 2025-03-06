package org.tangscode.web;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/6
 */
public class SocketException extends RuntimeException {
    public SocketException(String message) {
        super(message);
    }

    public SocketException(String message, Throwable cause) {
        super(message, cause);
    }
}
