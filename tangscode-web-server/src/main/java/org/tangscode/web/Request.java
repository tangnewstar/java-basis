package org.tangscode.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author tangxinxing
 * @version 1.0
 * @description 请求的演示，负责解析请求报文
 * @date 2025/3/6
 */
public class Request {
    private String url;
    private String params;
    private String method;

    public String getUrl() {
        return url;
    }

    public String getParams() {
        return params;
    }

    public String getMethod() {
        return method;
    }

    // accept request stream
    public Request(InputStream inputStream) {
        try {
            // GET /test HTTP/1.1
            // HTTP请求报文是按行分隔的，第一行是请求方法和协议版本
            String[] requestLine = new BufferedReader(new InputStreamReader(inputStream)).readLine().split(" ");
            if (requestLine.length == 3 && requestLine[2].equals("HTTP/1.1")) {
                this.method = requestLine[0];
                String url = requestLine[1];
                // 解析参数
                if (url.contains("?")) {
                    this.url = url.substring(0, url.indexOf("?"));
                    this.params = url.substring(url.indexOf("?") + 1);
                } else {
                    this.url = url;
                }
            }
        } catch (IOException e) {
            throw new SocketException("input stream read exception", e);
        }
    }
}
