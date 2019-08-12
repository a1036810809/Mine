package GameFrame;

import Internet.Client;
import Internet.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMain extends Thread implements ActionListener {
    //窗体
    private MainFrame mf;
    //菜单
    private JMenuBar jmb;
    private InternetChoseFrame icf;
    private JMenuItem one,two,three,battle,maxScore,connect,helpContent,about;
    private int time,count,row,col;
    private Server server;
    private Client client;
    private boolean isTwo = false;
    
    public GameMain(){
    	initMenu();
        init(20,30,99);
        this.start();
    }

    private void initMenu(){
        JMenu onePeople,twoPeople,help;
        jmb = new JMenuBar();
        icf = new InternetChoseFrame();
        onePeople = new JMenu("单人");
        twoPeople = new JMenu("联机");
        help = new JMenu("帮助");
        one = new JMenuItem("初级");
        two = new JMenuItem("中级");
        three = new JMenuItem("高级");
        battle = new JMenuItem("对战模式");
        helpContent = new JMenuItem("帮助文档");
        about = new JMenuItem("关于");
        maxScore = new JMenuItem("游戏记录");
        connect = new JMenuItem("连接");
        onePeople.add(one);
        onePeople.add(two);
        onePeople.add(three);
        onePeople.add(maxScore);
        twoPeople.add(battle);
        twoPeople.add(connect);
        help.add(helpContent);
        help.add(about);
        jmb.add(onePeople);
        jmb.add(twoPeople);
        jmb.add(help);
        
        connect.setEnabled(false);
        
        
        one.addActionListener(this);
        two.addActionListener(this);
        three.addActionListener(this);
        battle.addActionListener(this);
        connect.addActionListener(this);
        helpContent.addActionListener(this);
        about.addActionListener(this);
        maxScore.addActionListener(this);
    }

    private void init(int rows,int cols,int bombCount){
        this.count = bombCount;
        this.time = 0;
        this.row = rows;
        this.col = cols;
        mf = new MainFrame(rows,cols,bombCount,jmb);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == one){
        	closeTwo();
            setNewGameMap(8,8,10);
        }else if (e.getSource() == two){
        	closeTwo();
            setNewGameMap(16,16,40);
        }else if (e.getSource() == three){
        	closeTwo();
            setNewGameMap(20,30,99);
        }else if (e.getSource() == battle){
        	icf.setVisible(true);
            if (icf.getOptions() != 0) {
            	this.connect.setEnabled(true);
            }
        }else if (e.getSource() == connect){
        	if (connect.getText().equals("断开连接")) {
        		closeTwo();
        	}else {
	            if (icf.getOptions()==1){
	                //创建server
	                server = new Server(icf.getPort());
	                server.createConnect();
	                if (server.isConnected) { 
	                	this.setNewGameMap(row, col, count);
	                	server.sendData(this.mf.getGame());
	                	this.mf.getGame().setUser(server);
	                	this.isTwo = true;
	                	connect.setText("断开连接");
	                }
	            }else if (icf.getOptions()==2){
	                //创建client
	                client = new Client(icf.getHostip(),icf.getPort());
	                client.createConnect();
	                if (client.isConnected) {
	                	GamePanel gp = (GamePanel)client.receiveData();
	                	this.mf.dispose();
	                	this.mf = new MainFrame(gp, jmb);
	                	this.mf.setVisible(true);
	                	this.count = mf.getGame().getBombCount();
	                	this.col = mf.getGame().getCols();
	                	this.row = mf.getGame().getRows();
	                	this.mf.getGame().setUser(client);
	                	this.isTwo = true;
	                	connect.setText("断开连接");
	                }
	            }
        	}
        }else if (e.getSource() == about) {
        	JOptionPane.showConfirmDialog(null,"Author ：LZF\nE-mail ：lzf1036810809@gmail.com\nVersion：1.0","关于",JOptionPane.CLOSED_OPTION);
        }else if (e.getSource() == helpContent) {
        	new HelpFrame();
        }else if (e.getSource() == maxScore){
            WriterMaxScore wms = new WriterMaxScore();
            String[] s = wms.readerMaxScore();
            String msg = s[0]+":"+s[1]+"\n"+s[2]+":"+s[3]+"\n"+s[4]+":"+s[5];
            JOptionPane.showConfirmDialog(null,msg,"关于",JOptionPane.CLOSED_OPTION);
        }
    }

    private void setNewGameMap(int rows,int cols,int bombCount){
        mf.dispose();
        this.init(rows,cols,bombCount);
    }

    private void writeNumebr() {
        int num;
    	try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.time++;
        num = this.count - this.mf.getGame().getMarkNumber();
        if (num<0) num = 0;
        this.mf.setScore(num,time);
    }

    private void writeMaxScore(){
        WriterMaxScore wms = new WriterMaxScore();
        int index = 0;
        if (this.count == 10) index = 1;
        if (this.count == 40) index = 3;
        if (this.count == 99) index = 5;
        if (this.time < wms.getMaxScoreByIndex(index)){
            JOptionPane.showConfirmDialog(null,"恭喜你刷新记录了！","提示",JOptionPane.CLOSED_OPTION);
            wms.writeMaxScore(time,index);
        }
    }
    
    @Override
    public void run() {
        while(true){
            //System.out.println(this.mf.getGame().getStart());
            if (this.mf.getGame().getStart() && !isTwo){
            	writeNumebr();
                if(this.mf.getGame().isEnd()==this.count){
                    JOptionPane.showConfirmDialog(null,"你赢了！","提示",JOptionPane.CLOSED_OPTION);
                    writeMaxScore();
                    setNewGameMap(row,col,count);
                }else if (this.mf.getGame().isEnd()< this.count){
                    JOptionPane.showConfirmDialog(null,"你输了！","提示",JOptionPane.CLOSED_OPTION);
                    setNewGameMap(row,col,count);
                }

            }
            System.out.println(isTwo);
            if (isTwo) {
            	System.out.println(this.mf.getGame().isEnable());
            	if (!this.mf.getGame().isEnable()) {
            		String str;
	            	if (this.icf.getOptions() == 2) {
	            		str = client.receiveAMessage();
	            		isClose(str);
	            		
	            	}else{
	            		str = server.receiveAMessage();
	            		isClose(str);
	            	}
            	}else if (this.mf.getGame().getStart()){
            		writeNumebr();
            	}
            	if(this.mf.getGame().isEnd()==this.count){
                    JOptionPane.showConfirmDialog(null,"你赢了！","提示",JOptionPane.CLOSED_OPTION);
                    JOptionPane.showConfirmDialog(null,"切换为单人模式！","提示",JOptionPane.CLOSED_OPTION);
                    closeTwo();
                    setNewGameMap(row,col,count);
                }else if (this.mf.getGame().isEnd()< this.count){
                    JOptionPane.showConfirmDialog(null,"你输了！","提示",JOptionPane.CLOSED_OPTION);
                    JOptionPane.showConfirmDialog(null,"切换为单人模式！","提示",JOptionPane.CLOSED_OPTION);
                    closeTwo();
                    setNewGameMap(row,col,count);
                }
            }
        }
    }
    
    private void isClose(String str){
        if(str == null) {
            JOptionPane.showConfirmDialog(null, "对手已经退出了！","提示",JOptionPane.CLOSED_OPTION);
            closeTwo();
        }else {
            succAndDef(str);
            this.mf.getGame().readAMove(str);
        }
    }

    private void closeTwo() {
    	if (icf.getOptions()==1 && server.isConnected) {
    		server.close();
    		isTwo = false;
    		connect.setText("连接");
    	}else if (icf.getOptions()==2 && client.isConnected) {
    		client.close();
    		isTwo = false;
    		connect.setText("连接");
    	}
    }

    private void succAndDef(String str) {
    	User u = icf.getOptions()==1 ? server : client;
    	if (str.equals("TIME")) {
			u.sendAMessage("TIMERR "+this.time);
		}else if (str.startsWith("TIMER")) {
			int rtime = Integer.parseInt(str.split(" ")[1]);
			if (this.time > rtime) {
				JOptionPane.showConfirmDialog(null, "对手用时："+rtime+"\n你的用时："+time+"\n你输了!","提示",JOptionPane.CLOSED_OPTION);
			}else if (this.time < rtime){
				JOptionPane.showConfirmDialog(null, "对手用时："+rtime+"\n你的用时："+time+"\n你赢了!","提示",JOptionPane.CLOSED_OPTION);
			}else {
				JOptionPane.showConfirmDialog(null, "对手用时："+rtime+"\n你的用时："+time+"\n平局!","提示",JOptionPane.CLOSED_OPTION);
			}
			if (str.startsWith("TIMERR")) {
				u.sendAMessage("TIMER "+this.time);
			}else {
				closeTwo();
			}
		}
    }
    public static void main(String[] args){
        new GameMain();
    }
    
    
}
