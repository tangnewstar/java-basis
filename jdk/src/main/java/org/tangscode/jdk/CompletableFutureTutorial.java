package org.tangscode.jdk;

import java.util.concurrent.CompletableFuture;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/26
 */
public class CompletableFutureTutorial {

    public static void main(String[] args) {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 15);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 10);

        CompletableFuture<Integer> allFutures = CompletableFuture.allOf(future1, future2).thenApply(res -> {
            return future1.join()  + future2.join();
        });

        System.out.println(allFutures.join());
    }
}
