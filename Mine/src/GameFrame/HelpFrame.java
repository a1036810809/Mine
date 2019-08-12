package GameFrame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpFrame extends JFrame {
	private JScrollPane jsp;
	private JTextArea textArea;
	private String fileName = "src/help.txt";
	
	public HelpFrame() {
		this.setTitle("帮助文档");
		this.init();
		this.setSize(800, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void init() {
		this.textArea = new JTextArea();
		this.textArea.setText(readerFile(this.fileName));
		this.textArea.setEditable(false);
		this.jsp = new JScrollPane(this.textArea);
		this.add(jsp);
	}
	
	public String readerFile(String fileName) {
		File f = new File(fileName);
		String str = "";
		try {
			BufferedReader bre = new BufferedReader(new FileReader(f));
			try {
				String s = bre.readLine();
				while (s!=null) {
					str += s+"\n";
					s  = bre.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
}
