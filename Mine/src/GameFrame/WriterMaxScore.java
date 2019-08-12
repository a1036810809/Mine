package GameFrame;

import java.io.*;

public class WriterMaxScore {
    private File file = new File("src/maxScore.txt");//文件地址


    public String[] readerMaxScore() {
        String[] s = null;
        FileReader in = null;
        try {
            in = new FileReader(file);
            BufferedReader bre = new BufferedReader(in);
            try {
                String str = bre.readLine();
                s = str.split(" ");
                in.close();
                bre.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            System.out.println("打开失败！");
            e1.printStackTrace();
            return null;
        }
        return s;
    }
    
    public int getMaxScoreByIndex(int index){
        return Integer.parseInt(readerMaxScore()[index]);
    }
    
    public void writeMaxScore(int maxScore,int index) {
        String[] s = readerMaxScore();
        s[index] = maxScore+"";
        String str = s[0]+" "+s[1]+" "+s[2]+" "+s[3]+" "+s[4]+" "+s[5];
        FileWriter out = null;
        try {
            out = new FileWriter(file);
            out.write(""+str);
            out.close();
        } catch (IOException e) {
            System.out.println("没有找到文件！");
            e.printStackTrace();
        }
    }
    
}
