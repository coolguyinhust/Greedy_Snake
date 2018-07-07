package TanChishe;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
public class Yard extends Frame{
    PaintThread paintthread=new PaintThread();
    private boolean gameOver=false;
    //行号
    public static final int ROWS=40;
    //列号
    public static final int COLS=50;
    //活动区域
    public static final int BLOCK_SIZE=15;
    //字体属性：
    private Font fontGmaeOver=new Font("宋体",Font.BOLD,100);
    
    //分数：
    private int score=0;
    //记录开始时间
    private long beginTime=0;
    Snake s=new Snake(this);//new Snake(this)
    Egg e=new Egg();
    public int speedlevel=0;
    public int timeshort=0;
    Image offScreenImage=null;
    public void launch(){//设置窗口的大小，位置，可见，点击事件键盘事件，最后开启绘图线程
        this.setLocation(166,17);//设置窗口初始显示位置
        this.setSize(COLS*BLOCK_SIZE, ROWS*BLOCK_SIZE);//宽度和高度
         this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        this.setVisible(true);
        this.addKeyListener(new KeyMonitor());
        new Thread(paintthread).start();//act start(),线程进入就绪状态
    }
    public static void main(String[] args){
        Yard y=new Yard();
        y.beginTime=System.currentTimeMillis();
        y.launch();
    }
    public void stop(){
        gameOver=true;
    }
    @Override 
    public void paint(Graphics g){//graphics画笔颜色先保存好
        Color c=g.getColor();
        g.setColor(Color.GRAY);
        //用画笔当前颜色填充，x,y左上角位置
        g.fillRect(0, 0,  COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        g.setColor(Color.DARK_GRAY);
        //画表格
        for(int i=1;i<ROWS;i++){
            g.drawLine(0,BLOCK_SIZE*i,COLS*BLOCK_SIZE,BLOCK_SIZE*i);
        }
        for(int i=1;i<COLS;i++){
            g.drawLine(BLOCK_SIZE*i,0,BLOCK_SIZE*i,BLOCK_SIZE*ROWS);
        }
        g.setColor(Color.YELLOW);
        g.drawString("使用说明：使用方向键控制方向，P--停止，再按后恢复，R--重新开始" , 10, 60);
        g.drawString("目前分数:" + score, 10, 80);
        g.drawString("加分规则：每吃一个加5分，加油！" , 10, 100);
        g.drawString("您已存活："+(System.currentTimeMillis()-beginTime)/1000+"秒" , 10, 120);
        if(gameOver){
            g.setFont(new Font("宋体",Font.BOLD,45));
            g.drawString("Once again,press down F5", 10, 250);
            if(score<100){
                g.setFont(fontGmaeOver);
                g.drawString("game over", 90, 170);
            }
            else{
                g.setColor(Color.RED);
                g.setFont(fontGmaeOver);
                g.drawString("hoxian,wei nie", 90, 170);
            }
           paintthread.pause();
        }
        g.setColor(c);
        s.eat(e);
        e.draw(g);
        s.draw(g);
    }
    @Override 
    public void update(Graphics g){
        if(offScreenImage==null){
             /*public Image createImage(int width,int height),创建一幅用于双缓冲的、可在
            屏幕外绘制的图像,具体表现为在内存中开辟一块和窗口大小一样的空间*/
            offScreenImage=this.createImage(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        }    
            Graphics goff=offScreenImage.getGraphics();//获取画笔
            paint(goff);//将缓冲图像的画笔传入
            g.drawImage(offScreenImage, 0, 0, null);//将缓冲图像一次画到屏幕上
    }
    private class PaintThread implements Runnable{
        private boolean running =true;
        private boolean pause=false;
        public void run(){
            while(running )
            {
                if(pause) 
                    continue;
                else
                    repaint();
                try{
                    Thread.sleep(250-timeshort*15);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        public void pause(){
            this.pause=true;
        }
        public void reStart(){
            this.pause=false;
            s=new Snake(Yard.this);
            gameOver=false;
            score=0;
            beginTime=0;
        }
        public void gameOver(){
            running =false;
        }
    }
    static boolean paus=false;
    private class KeyMonitor extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            int key=e.getKeyCode();
            
            if(key==KeyEvent.VK_R){
                paintthread.reStart();
            }
            else if(key==KeyEvent.VK_P&&!paus){
                paus=true;
                paintthread.pause=true;//暂停     
            }
            else if(key==KeyEvent.VK_P&&paus){
                paintthread.pause=false;//从暂停中恢复
                paus=false;
            }
            s.keyPressed(e);
        }
    }
    public int getScore(){
        return this.score;
    }
    public void setScore(int score){
        this.score =score;
    }
}


