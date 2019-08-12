package Internet;

import javax.swing.*;

import GameFrame.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server implements User{
    private int port = 8888;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private DataOutputStream currentOut=null;
    private DataInputStream currentIn=null;
    public boolean isConnected;
    
    public Server(int port){
        this.port = port;
        isConnected = false;
    }
    
    public int getId() {
    	return 1;
    }
    
    public void createConnect(){
        try {
            serverSocket = new ServerSocket(this.port);
            JOptionPane.showConfirmDialog(null,"创建成功！等待客户机连接！","提示",JOptionPane.CLOSED_OPTION);
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null,"不能监听端口:" + port,"提示",JOptionPane.CLOSED_OPTION);
            //System.err.println("Could not listen on port:" + port);
            return;
        }
        try{
            socket = serverSocket.accept();
            JOptionPane.showConfirmDialog(null,"连接成功!","提示",JOptionPane.CLOSED_OPTION);
            currentIn = new DataInputStream(new BufferedInputStream(
                    socket.getInputStream()));
            currentOut =new DataOutputStream(new BufferedOutputStream(
                    socket.getOutputStream()));
            isConnected = true;
        }catch (IOException e){
            System.err.println("Accept failed.");
            System.exit(1);
        }
    }

    public void sendAMessage(String message){
        try {
            currentOut.writeUTF(message);
            currentOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String receiveAMessage(){
        String message;
        try{
            message = currentIn.readUTF();
        }catch (IOException e){
            System.err.println("Cannot receive a message!");
            e.printStackTrace();
            return null;
        }
        return message;
    }
    
    public void sendData(Object data) {
		ObjectOutputStream out;
		DataOutputStream dos = this.currentOut;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(dos));
			out.writeObject(data);
			out.flush();
		} catch (SocketException exc){
			
		} catch (IOException e) {
			
		}
	}
    
    public void close() {
    	try {
    		isConnected = false;
			currentIn.close();
			currentOut.close();
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
