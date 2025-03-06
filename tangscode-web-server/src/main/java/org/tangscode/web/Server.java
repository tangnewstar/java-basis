package org.tangscode.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangxinxing
 * @version 1.0
 * @description web server simple demo
 * @date 2025/3/6
 */
public class Server {

    // routes map
    private final Map<String, Processor> routesMap = new HashMap<>();

    public void use(String route, Processor processor) {
        routesMap.put(route, processor);
    }

    // socket listen
    public void listen(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Now server is listening on port: " + port);
            printAvailableUrl();
            // read socket linux-> accept
            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> {
                    try {
                        Request request = new Request(client.getInputStream());
                        Response response = new Response(client.getOutputStream());
                        try {
                            if (routesMap.containsKey(request.getUrl())) {
                                routesMap.get(request.getUrl()).callback(request, response);
                            } else {
                                response.setStatus(404).send(request.getUrl() + " Not Found");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            response.setStatus(200).send("发生异常");
                        }
                    } catch (IOException e) {
                        throw new SocketException("read socket error", e);
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new SocketException("listen on port: " + port + " failed", e);
        }
    }

    private void printAvailableUrl() {
        System.out.println("Available urls: ");
        routesMap.keySet().forEach(url -> System.out.println(" - " + url));
    }
}
