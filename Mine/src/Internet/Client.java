package Internet;

import javax.swing.*;

import GameFrame.User;

import java.io.*;
import java.net.*;
public class Client implements User{
    Socket clientSocket = null;
    DataOutputStream out;
    DataInputStream in;
    private String serverIP;
    int port;
    public boolean isConnected;
    //HistoryState state = null;
    
    public Client(String ipAdd,int portnum){
        this.serverIP = ipAdd;
        port = portnum;
        isConnected = false;
    }
    
    public int getId() {
    	return 2;
    }
    
    public void createConnect(){
        try {
            clientSocket = new Socket(InetAddress.getByName(serverIP),port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            isConnected = true;
        }catch (UnknownHostException e) {
            JOptionPane.showConfirmDialog(null,"未知主机地址："+this.serverIP,"提示",JOptionPane.CLOSED_OPTION);
            //System.err.println("Don't know about host: localhost");
        }catch (IOException e) {
            JOptionPane.showConfirmDialog(null,"不能从目的主机获取到I/O连接: "+this.serverIP+":"+this.port,"提示",JOptionPane.CLOSED_OPTION);
            //System.err.println("Couldn't get I/O for the connection to: localhost.");
        }
    }
    public void sendAMessage(String message){
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String receiveAMessage(){
        String message;
        try{
            message = in.readUTF();
        }catch (IOException e) {
            System.err.println("Cannot receive a message!");
            return null;
        }
        return message;
    }
    
    public Object receiveData() {
		ObjectInputStream in ;
		DataInputStream dis = this.in;
		Object result = null;	
		try {
			in = new ObjectInputStream(new BufferedInputStream(dis));
			result = in.readObject();
		} catch (SocketException e){
			
		} catch (IOException e) {
			
			return null;
		} catch (ClassNotFoundException e) {
			
			return null;
		}
		return result;
	}
    
    public void close() {
    	try {
    		isConnected = false;
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

