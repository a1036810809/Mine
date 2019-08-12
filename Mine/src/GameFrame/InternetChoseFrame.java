package GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class InternetChoseFrame extends JDialog implements ActionListener {
    private JRadioButton choseServer,choseClient;
    private ButtonGroup buttonGroup;
    private JLabel port,host;
    private JTextField jport,jhost;
    private JButton save,close;
    private JPanel radio,jp2,main,jphost,jpport,jb;
    private int options = 0;
    private String hostip = "";
    private int conPort = 8888;
    
    public InternetChoseFrame(){
        this.setModal(true);
        this.setTitle("设置");
        this.init();
        this.setSize(200,150);
        this.setVisible(false);
        this.setDefaultCloseOperation(0);
    }
    
    public void init(){
        choseServer = new JRadioButton("服务器");
        choseClient = new JRadioButton("客户机");
        
        radio = new JPanel();
        radio.setBorder(BorderFactory.createTitledBorder("选择类型："));
        radio.setLayout(new BoxLayout(radio,BoxLayout.X_AXIS));
        buttonGroup = new ButtonGroup();
        buttonGroup.add(choseClient);
        buttonGroup.add(choseServer);
        radio.add(choseClient);
        radio.add(choseServer);
        
        jphost = new JPanel();
        jpport = new JPanel();
        jpport.setLayout(new BoxLayout(jpport,BoxLayout.X_AXIS));
        jphost.setLayout(new BoxLayout(jphost,BoxLayout.X_AXIS));
        jp2 = new JPanel();
        jp2.setLayout(new BoxLayout(jp2,BoxLayout.Y_AXIS));
        jp2.setBorder(BorderFactory.createTitledBorder("输入相关设置："));
        port = new JLabel("端口号码:");
        host = new JLabel("主机地址:");
        jport = new JTextField();
        jhost = new JTextField();
        jport.setSize(55,10);
        jhost.setSize(55,10);
        jport.setEnabled(false);
        jhost.setEnabled(false);
        jport.setBackground(Color.lightGray);
        jhost.setBackground(Color.lightGray);
        jphost.add(host);
        jphost.add(jhost);
        jpport.add(port);
        jpport.add(jport);
        jp2.add(jphost);
        jp2.add(jpport);
        
        save = new JButton("保存");
        close = new JButton("关闭");
        jb = new JPanel();
        jb.add(save);
        jb.add(close);
        
        main = new JPanel();
        main.setLayout(new BoxLayout(main,BoxLayout.Y_AXIS));
        main.add(radio);
        main.add(jp2);
        main.add(jb);
        
        this.add(main);
        
        save.addActionListener(this);
        close.addActionListener(this);
        choseClient.addActionListener(this);
        choseServer.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save){
            if (!this.jhost.isEnabled()){
                if (this.jport.isEnabled() && Pattern.matches("^[0-9]{1,5}$",this.jport.getText())){
                    this.options = 1;
                    this.conPort = Integer.parseInt(this.jport.getText());
                    this.setVisible(false);
                }else{
                    this.jport.setText(""+this.conPort);
                    JOptionPane.showConfirmDialog(null,"输入错误或请选择类型!","错误窗口",JOptionPane.CLOSED_OPTION);
                }
            }else{
                if (Pattern.matches("^[0-9]{1,4}$",this.jport.getText()) && Pattern.matches(
                        "^((0?(0?[0-9])|([1-9][0-9]))|(1[0-9]{2})|(2([0-4][0-9])|(5[0-5])))(\\.((0?(0?[0-9])|([1-9][0-9]))|(1[0-9]{2})|(2([0-4][0-9])|(5[0-5])))){3}$",
                        this.jhost.getText())){
                    this.options = 2;
                    this.conPort = Integer.parseInt(this.jport.getText());
                    this.hostip = this.jhost.getText();
                    this.setVisible(false);
                }else{
                    this.jport.setText(""+this.conPort);
                    this.jhost.setText(this.hostip);
                    JOptionPane.showConfirmDialog(null,"输入错误!","错误窗口",JOptionPane.CLOSED_OPTION); 
                }
            }
        }else if (e.getSource() == close){
            this.setVisible(false);
        }else if (e.getSource() == choseClient){
            this.jport.setEnabled(true);
            this.jhost.setEnabled(true);
            this.jhost.setBackground(Color.WHITE);
            this.jport.setBackground(Color.WHITE);
        }else if (e.getSource() == choseServer){
            this.jport.setEnabled(true);
            this.jport.setBackground(Color.WHITE);
            this.jhost.setEnabled(false);
            this.jhost.setBackground(Color.lightGray);
        }
    }
    
    public int getOptions(){
        return  this.options;
    }
    
    public int getPort(){
        return this.conPort;
    }
    
    public String getHostip(){
        return this.hostip;
    }
}
