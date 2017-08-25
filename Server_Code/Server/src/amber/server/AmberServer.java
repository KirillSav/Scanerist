/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amber.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;

/**
 * Responsible for server connection
 */
public class AmberServer {
    
    private Server server;
    private Kryo kryo;
    private ServerListener serverListener;

    public AmberServer() throws IOException {
        server = new Server();
        serverListener = new ServerListener(server);
        server.addListener(serverListener);
        registerPackets();
        server.bind(9999);
        server.start();
        System.out.println("Started...");
    }
    
    private void registerPackets() {
        kryo = server.getKryo();
        kryo.register(Packet.RequestInsertProduct.class);
        kryo.register(Packet.RequestGetProduct.class);
        kryo.register(Packet.ResponseGetProduct.class);
        kryo.register(Packet.ResponseInsertProduct.class);
        kryo.register(Packet.RequestAddAmount.class);
        kryo.register(Packet.ResponseAddAmount.class);
        kryo.register(Packet.RequestSubtractAmount.class);
        kryo.register(Packet.ResponseSubtractAmount.class);
        kryo.register(Packet.RequestUpdateProduct.class);
        kryo.register(Packet.ResponseUpdateProduct.class);
        kryo.register(Packet.RequestGetList.class);
        kryo.register(Packet.ResponseGetList.class);
        kryo.register(ProductMini.class);
        kryo.register(Product.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(Packet.RequestDeleteProduct.class);
        kryo.register(Packet.ResponseDeleteProduct.class);
    }
    
}
