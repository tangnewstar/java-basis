package org.tangscode.spring.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloService extends Remote {
    String sayHello(String name) throws RemoteException;
}