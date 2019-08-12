package GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel implements MouseListener{
    private static final long serialVersionUID = 1L;
    private final int[][] offset = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
    private int rows;
    private int cols;
    private int bombCount;
    private int markNumber = 0;
    private int surplusB;
    private boolean isStart = false;
    private boolean firstClick = true;
    private final int BLOCKWIDTH = 20;
    private final int BLOCKHEIGHT = 20;
    private JLabel[][] labels;
    private MyButton[][] buttons;
    private User user = null;
    private boolean isEnable = true;
    private String sendMsg = null;

    public JLabel[][] getLabels() {
		return labels;
	}
    
    public void setUser(User user) {
    	this.user = user;
    	if (user.getId() == 2) {
    		this.isStart = true;
    		this.isEnable = false;
    	}
    }
    
    public boolean isEnable() {
    	return this.isEnable;
    }
    
    public void readAMove(String str) {
    	String[] a = str.split(" ");
    	if (str.startsWith("TIME")) return;
    	if (a[0].equals("MARK")) {
    		markMine(buttons[Integer.parseInt(a[1])][Integer.parseInt(a[2])]);
    	}else if (a[0].equals("OPEN")) {
    		openByOther(buttons[Integer.parseInt(a[1])][Integer.parseInt(a[2])]);
    	}else if (a[0].equals("CHANGE")) {
    		this.writeNumberByOne(Integer.parseInt(a[1]),Integer.parseInt(a[2]));
            this.writeNumberByOther(Integer.parseInt(a[1]),Integer.parseInt(a[2]));
            labels[Integer.parseInt(a[1])][Integer.parseInt(a[2])].setBackground(Color.YELLOW);
            labels[Integer.parseInt(a[1])][Integer.parseInt(a[2])].setForeground(Color.BLACK);
            this.labels[Integer.parseInt(a[3])][Integer.parseInt(a[4])].setText("*");
            this.labels[Integer.parseInt(a[3])][Integer.parseInt(a[4])].setBackground(Color.DARK_GRAY);
            this.labels[Integer.parseInt(a[3])][Integer.parseInt(a[4])].setForeground(Color.RED);
            this.writeNumberByOther(Integer.parseInt(a[3]),Integer.parseInt(a[4]));
            this.open(buttons[Integer.parseInt(a[1])][Integer.parseInt(a[2])]);
    	}
    	this.isEnable = true;
    	if (a[0].equals("OPENL")) {
    		surplusB = rows*cols;
    		openByOther(buttons[Integer.parseInt(a[1])][Integer.parseInt(a[2])]);
    		this.isEnable = false;
    	}
    }
    
    public void openByOther(MyButton btn) {
    	btn.setVisible(false);
    	this.surplusB--;
        // 判断按钮中 是否为数字还是空
        switch (labels[btn.row][btn.col].getText()) {
            // 如果是炸弹则将全部按钮都打开,游戏结束
            case "*" :
                this.surplusB = bombCount;
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        buttons[i][j].setVisible(false);
                    }
                }
                break;
            // 如果是空的则将他周围空的按钮都打开,进入递归
            case "" :
                for (int[] off: offset) {
                    int newRow = btn.row + off[0];
                    int newCol = btn.col + off[1];
                    if (verify(newRow, newCol)) {
                        MyButton sButton = buttons[newRow][newCol];
                        if (sButton.isVisible()) {
                            open(sButton);
                        }
                    }
                }
            default:
        }
    }
    
    public GamePanel(int rows, int cols,int bombCount,JLabel[][] labels) {
        this.rows = rows;
        this.cols = cols;
        this.bombCount = bombCount;
        this.surplusB = rows*cols;
        //this.labels = new JLabel[rows][cols];
        this.initButtons();
        this.labels = labels;
        this.initLabelsForTwo();
        //this.randomBomb(this.bombCount);
        //this.writeNumber();
        this.setSize(this.cols*BLOCKWIDTH,this.rows*BLOCKHEIGHT);
        this.setLayout(null);
    }

	public GamePanel(int rows, int cols,int bombCount) {
        this.rows = rows;
        this.cols = cols;
        this.bombCount = bombCount;
        this.surplusB = rows*cols;
        this.labels = new JLabel[rows][cols];
        this.initButtons();
        this.initLabels();
        this.randomBomb(this.bombCount);
        this.writeNumber();
        this.setSize(this.cols*BLOCKWIDTH,this.rows*BLOCKHEIGHT);
        this.setLayout(null);
    }
	
	private void initLabelsForTwo() {
		for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
            	this.add(this.labels[i][j]);
            }
		}
	}
	
    // 界面初始化,绘制扫雷的边框
    private void initLabels(){
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                JLabel l = new JLabel("", JLabel.CENTER);
                // 设置每个小方格的边界
                l.setBounds(j * BLOCKWIDTH, i * BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
                // 绘制方格边框
                l.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                // 设置方格为透明,便于我们填充颜色
                l.setOpaque(true);
                // 背景填充为黄色
                l.setBackground(Color.YELLOW);
                // 将方格加入到容器中(即面板JPanel)
                this.add(l);
                // 将方格存到类变量中,方便公用
                labels[i][j] = l;
            }
        }

    }

    public int[] returnSize() {
        int[] a = {this.cols * BLOCKWIDTH, (this.rows+1) * BLOCKHEIGHT};
        return a;
    }

    // 产生bombCount个炸弹,并在labels中用"*"标注出来
    private void randomBomb(int count) {
        for (int i = 0; i < count; i++) {
            // 生成一个随机数表示行坐标
            int rRow = (int) (Math.random() * this.rows);
            // 生成一个随机数表示列坐标
            int rCol = (int) (Math.random() * this.cols);
            // 根据坐标确定JLabel的位置,并显示*
            if (labels[rRow][rCol].getText().equals("*")){
                i--;continue;
            }
            this.labels[rRow][rCol].setText("*");
            // 设置背景颜色
            this.labels[rRow][rCol].setBackground(Color.DARK_GRAY);
            // 设置*的颜色
            this.labels[rRow][rCol].setForeground(Color.RED);
            if (count == 1){
            	this.sendMsg += " "+rRow+" "+rCol;
                this.writeNumberByOther(rRow,rCol);
                
            }
        }
    }

    // 初始化按钮,将JLabel覆盖住
    private void initButtons() {
        buttons = new MyButton[rows][cols];
        // 循环生成按钮
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                MyButton btn = new MyButton();
                // 根据JLabel大小设置按钮的大小边界
                btn.setBounds(j * BLOCKWIDTH, i * BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
                this.add(btn);
                // 将按钮存在类变量中(当然,实际上存的是对象的引用地址)
                buttons[i][j] = btn;
                btn.row = i;
                btn.col = j;
                // 给按钮添加监听器,注册点击事件
                // (单机按钮时,将执行内部类ActionListener()中的actionPerformed(ActionEvent e)方法)
                btn.addMouseListener(this);
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
		//System.out.println("YES: "+((MyButton)e.getSource()).row+" "+((MyButton)e.getSource()).col);
    	if(this.isEnable) {
	        if (e.isMetaDown()){
	            markMine((MyButton)e.getSource());
	            if (user != null) {
	            	user.sendAMessage("MARK "+((MyButton)e.getSource()).row+" "+((MyButton)e.getSource()).col);
	            	this.isEnable = false;
	            }
	        }else{
	        	if (this.firstClick && labels[((MyButton)e.getSource()).row][((MyButton)e.getSource()).col].getText().equals("*")) {
	        		sendMsg = "CHANGE "+((MyButton)e.getSource()).row+" "+((MyButton)e.getSource()).col;
	        	}else {
	        		sendMsg = "OPEN "+((MyButton)e.getSource()).row+" "+((MyButton)e.getSource()).col;
	        	}
	            open((MyButton)e.getSource());
	            if (user != null) {
	            	if (surplusB == bombCount) sendMsg = "OPENL "+((MyButton)e.getSource()).row+" "+((MyButton)e.getSource()).col;
	            	user.sendAMessage(sendMsg);
	            	if (surplusB == bombCount) {
	            		surplusB = bombCount+1;
	            		user.sendAMessage("TIME");
	            	}
	            	this.isEnable = false;
	            }
	        }
    	}else {
    		JOptionPane.showConfirmDialog(null, "当前你不能操作！","提示",JOptionPane.CLOSED_OPTION);
    	}
    }
    
    //右键按钮
    private void markMine(MyButton btn){
        if (this.firstClick) this.isStart = true;
        if (btn.getBackground() == Color.RED){
            btn.setBackground(new Color(238,238,238));
            this.markNumber--;
        }else{
            btn.setBackground(Color.RED);
            this.markNumber++;
        }
    }
    
    // 单击按钮时打开或成片打开
    private void open(MyButton btn) {
    	//System.out.println("open "+btn.row+" "+btn.col);
        //判断是否被标记
        if (btn.getBackground() == Color.RED){
            return;
        }
        
        this.surplusB --;
        if (this.firstClick) this.isStart = true;
        
        //判断第一次点击
        if (this.firstClick && labels[btn.row][btn.col].getText().equals("*")){
        	System.out.println(11);
            this.randomBomb(1);
            this.writeNumberByOne(btn.row,btn.col);
            this.writeNumberByOther(btn.row,btn.col);
            labels[btn.row][btn.col].setBackground(Color.YELLOW);
            labels[btn.row][btn.col].setForeground(Color.BLACK);
        }
        firstClick = false;
        // 先将当期按钮设置为不可见,即打开了按钮
        btn.setVisible(false);
        // 判断按钮中 是否为数字还是空
        switch (labels[btn.row][btn.col].getText()) {
            // 如果是炸弹则将全部按钮都打开,游戏结束
            case "*" :
                this.surplusB = 0;
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        buttons[i][j].setVisible(false);
                    }
                }
                break;
            // 如果是空的则将他周围空的按钮都打开,进入递归
            case "" :
                for (int[] off: offset) {
                    int newRow = btn.row + off[0];
                    int newCol = btn.col + off[1];
                    if (verify(newRow, newCol)) {
                        MyButton sButton = buttons[newRow][newCol];
                        if (sButton.isVisible()) {
                            open(sButton);
                        }
                    }
                }
            default:
        }
    }
    
    // 判断位置是否越界
    private boolean verify(int row, int col) {
        return row >= 0 && row < this.rows && col >= 0 && col < this.cols;
    }

    // 将炸弹的周围标注上数字
    private void writeNumber() {
        for (int  i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                // 如果是炸弹,不标注任何数字
                if (labels[i][j].getText().equals("*")) {
                    continue;
                }
                // 如果不是炸弹,遍历它周围的八个方块,将炸弹的总个数标注在这个方格上
                writeNumberByOne(i,j);
            }
        }
    }
    
    private void writeNumberByOne(int i,int j){
        int bombCount = 0;
        // 通过偏移量数组循环遍历8个方块
        for (int[] off: offset) {
            int row = i + off[1];
            int col = j + off[0];
            // 判断是否越界,是否为炸弹
            if (verify(row, col) && labels[row][col].getText().equals("*")) {
                bombCount++;
            }
        }
        // 如果炸弹的个数不为0,标注出来
        if (bombCount > 0) {
            labels[i][j].setText(String.valueOf(bombCount));
        }else{
            labels[i][j].setText("");
        }
    }
    
    private void writeNumberByOther(int i,int j){
        for (int[] off: offset) {
            int row = i + off[1];
            int col = j + off[0];
            if (verify(row, col) && !labels[row][col].getText().equals("*")) {
                this.writeNumberByOne(row,col);
            }
        }
    }
    
    public int isEnd(){
        return surplusB;
    }
    
    public int getMarkNumber(){
        return markNumber;
    }
    
    public boolean getStart(){
        return isStart;
    }
    
    public int getBombCount() {
    	return this.bombCount;
    }

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
