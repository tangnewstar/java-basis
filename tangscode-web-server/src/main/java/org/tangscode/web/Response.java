package org.tangscode.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangxinxing
 * @version 1.0
 * @description 响应的演示，负责响应报文到客户端
 * @date 2025/3/6
 */
public class Response {
    //    response header
    private Map<String, String> headers = new HashMap<>();

    public Response setHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    // response status code
    private int status;
    public Response setStatus(int status) {
        this.status = status;
        return this;
    }

    // response stream
    private OutputStream outputStream;
    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void send(String data) {
        try {
            StringBuilder sb = new StringBuilder();
            // 响应行
            sb.append("HTTP1.1 ").append(this.status).append("\n");
            // 响应头
            for (Map.Entry entry: this.headers.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            // 响应体
            sb.append("\n").append(data);
            outputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            throw new SocketException("write socket error", e);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
