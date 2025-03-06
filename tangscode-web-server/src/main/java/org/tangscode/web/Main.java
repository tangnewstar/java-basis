package org.tangscode.web;

/**
 * @author tangxinxing
 * @version 1.0
 * @description web server 的简单演示
 * @date 2025/3/6
 */
public class Main {

    public static void main(String[] args) {

        Server server = new Server();
        // register routes
        server.use("/hello", new Processor() {
            @Override
            public void callback(Request request, Response response) {
                response.setStatus(200).send("<p1>Hello<p1>");
            }
        });

        server.use("/test", new Processor() {
            @Override
            public void callback(Request request, Response response) {
                response.setStatus(200).send("<p1>this is a test</p1>");
            }
        });

        server.listen(8080);
    }
}
