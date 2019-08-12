package GameFrame;

public interface User {
	public void createConnect();
	public void sendAMessage(String message);
	public String receiveAMessage();
	public int getId();
}
