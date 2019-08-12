package GameFrame;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    private GamePanel gp;
    private Container c;
    private int Width,Height;
    private JLabel numLabel,timeLabel;
    private JPanel panel,numPanel,timePanel,main;
    
    public MainFrame(int rows,int cols,int bombCount,JMenuBar jmb){
    	gp = new GamePanel(rows, cols, bombCount);
    	this.init(jmb);
    }
    
    public MainFrame(GamePanel gp,JMenuBar jmb) {
    	this.gp = new GamePanel(gp.getRows(), gp.getCols(), gp.getBombCount(),gp.getLabels());;
    	this.init(jmb);
    }
    
    public void init(JMenuBar jmb) {
    	//防止菜单被覆盖
    	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        main = new JPanel();
        main.setLayout(new BorderLayout());
        this.setTitle("Mine");
        this.setJMenuBar(jmb);
        this.initScore(""+gp.getBombCount(),"0");
        this.initSize();
        this.setResizable(false);
        main.add(panel,BorderLayout.NORTH);
        main.add(gp,BorderLayout.CENTER);
        this.add(main);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void initSize() {
    	int[] a = gp.returnSize();
        Height = a[1];
        Width = a[0];
        this.setSize(Width, Height+25);
    }
    
    public void initScore(String num,String time){
        numLabel = new JLabel("雷数："+num);
        timeLabel = new JLabel("耗时："+time);
        numLabel.setFont(new Font("宋体",Font.PLAIN,14));
        timeLabel.setFont(new Font("宋体",Font.PLAIN,14));
        numPanel = new JPanel(new FlowLayout());
        numPanel.add(numLabel);
        timePanel = new JPanel(new FlowLayout());
        timePanel.add(timeLabel);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        panel.add(numPanel);
        panel.add(timePanel);
        panel.setSize(100,15);
    }
    
    public void setScore(int num,int time){
        numLabel.setText("雷数："+num);
        timeLabel.setText("耗时："+time);
    }
    
    public GamePanel getGame(){
        return gp;
    }
}
