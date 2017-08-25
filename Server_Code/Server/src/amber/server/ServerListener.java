/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amber.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * Responsible for listening the events sent to the server
 */
class ServerListener extends Listener {

    private Server server;
    private Connection[] allClients;
    
    public ServerListener(Server server) {
        this.server = server;
    }
    
    @Override
    public void connected(Connection cnctn) {
        System.out.println("Client " + cnctn.getID() + " has connected at "+
        	 String.format("%02d:%02d:%02d", ((System.currentTimeMillis() / (1000 * 60 * 60))+3) % 24,
    		(System.currentTimeMillis() / (1000 * 60)) % 60, (System.currentTimeMillis() / 1000) % 60));
    }
        
    @Override
    public void received(Connection cnctn, Object o) {
    	if(o instanceof Packet.RequestInsertProduct){
        	System.out.println("Client "+ cnctn.getID()+">got product to insert");
        	Packet.ResponseInsertProduct sendable=new Packet.ResponseInsertProduct();
        	sendable.bool=SQLiteDB.insertProduct(((Packet.RequestInsertProduct) o).product);
        	server.sendToTCP(cnctn.getID(),sendable);
        }else if(o instanceof Packet.RequestGetProduct){
        	System.out.println("Client "+ cnctn.getID()+">got barcode");
        	Packet.ResponseGetProduct sendable=new Packet.ResponseGetProduct();
        	sendable.product=SQLiteDB.findProduct(((Packet.RequestGetProduct) o).barCode);
        	server.sendToTCP(cnctn.getID(), sendable);
        }else  if(o instanceof Packet.RequestAddAmount){
        	System.out.println("Client "+ cnctn.getID()+">got add amount request");
    		Packet.ResponseAddAmount sendable=new Packet.ResponseAddAmount();
    		sendable.bool=SQLiteDB.changeProductAmount(((Packet.RequestAddAmount) o).barCode,
    												   ((Packet.RequestAddAmount) o).amount,true);
    		server.sendToTCP(cnctn.getID(), sendable);
        }else if(o instanceof Packet.RequestSubtractAmount){
        	System.out.println("Client "+ cnctn.getID()+">got sutract amount request");
        	Packet.ResponseSubtractAmount sendable= new Packet.ResponseSubtractAmount();
        	sendable.bool=SQLiteDB.changeProductAmount(((Packet.RequestSubtractAmount) o).barCode,
        											   ((Packet.RequestSubtractAmount) o).amount,false);
        	server.sendToTCP(cnctn.getID(), sendable);
        }else if(o instanceof Packet.RequestUpdateProduct){
        	System.out.println("Client "+ cnctn.getID()+">got update request");
        	Packet.ResponseUpdateProduct sendable=new Packet.ResponseUpdateProduct();
        	sendable.bool=SQLiteDB.UpdateProduct(((Packet.RequestUpdateProduct) o).product);
        	server.sendToTCP(cnctn.getID(), sendable);
        }else if(o instanceof Packet.RequestGetList){
        	System.out.println("Client "+ cnctn.getID()+">got list request");
        	Packet.ResponseGetList sendable=new Packet.ResponseGetList();
        	sendable.list=SQLiteDB.GetList(((Packet.RequestGetList) o).str);
        	server.sendToTCP(cnctn.getID(), sendable);
        }else if(o instanceof Packet.RequestDeleteProduct) {
        	System.out.println("Client "+ cnctn.getID()+">got delete request");
        	Packet.ResponseDeleteProduct sendable=new Packet.ResponseDeleteProduct();
        	sendable.bool=SQLiteDB.deleteProduct(((Packet.RequestDeleteProduct) o).barcode);
        	server.sendToTCP(cnctn.getID(), sendable);
        }
    }

    @Override
    public void disconnected(Connection cnctn) {
    	System.out.println("Client " + cnctn.getID() + " has disconnected at "+
    	String.format("%02d:%02d:%02d", ((System.currentTimeMillis() / (1000 * 60 * 60))+3) % 24,
    			(System.currentTimeMillis() / (1000 * 60)) % 60, (System.currentTimeMillis() / 1000) % 60));
    }
    
}
