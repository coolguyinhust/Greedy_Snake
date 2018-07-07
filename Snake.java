package TanChishe;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
 enum Dir {
    L, U, R, D
}
public class Snake{
    private class Node{
        int w = Yard.BLOCK_SIZE;//w为节点的宽度
        int h = Yard.BLOCK_SIZE;//h为节点的高度
        int row,col;
        Dir dir =Dir.L;
        Node next=null;
        Node prev=null;
        
        Node(int row,int col,Dir dir){
            this.row=row;
            this.col=col;
            this.dir=dir;
        }
        void draw(Graphics g){
            Color c=g.getColor();
            g.setColor(Color.BLACK);
            g.fillRect(w*col, h*row, w,h );//(x,y,width,height) x,y为绘制实心矩形的左上起点，后为宽，高
            g.setColor(c);
        }
    }
    
    private Node head=null;
    private Node tail=null;
    private int Size=0;
    private Node n=new Node(20,30,Dir.L);
    private Yard y;
    public Snake(Yard y){
        head =n;
        tail= n;
        Size=1;
        this.y=y;
    }
    
    public void addToTail(){
        Node node=null;
        switch(tail.dir){
            case L:
               node=new Node(tail.row,tail.col+1,tail.dir);
               break;
            case U:
               node =new Node(tail.row+1,tail.col,tail.dir);
               break;
            case R:
                node =new Node(tail.row,tail.col-1,tail.dir);
                break;
            case D:
                node =new Node(tail.row-1,tail.col,tail.dir);
                break;
       }
        tail.next=node;
        node.prev=tail;
        tail=node;
        Size++;
    }
     public void addToHead() {
        Node node = null;
        switch(head.dir) {
        case L :
            node = new Node(head.row, head.col - 1, head.dir);
            break;
        case U :
            node = new Node(head.row - 1, head.col, head.dir);
            break;
        case R :
            node = new Node(head.row, head.col + 1, head.dir);
            break;
        case D :
            node = new Node(head.row + 1, head.col, head.dir);
            break;
        }
       head.prev=node;
       node.next=head;
       head=node;
       Size++;
     }
     private void deleteFromTail(){
         if(Size==0) return;
         tail=tail.prev;
         tail.next=null;
     }
     public void draw(Graphics g){
         if(Size<=0) return;
         move();
         for (Node n=head; n!= null ;n=n.next){
             n.draw(g);
         }  
     }
     
     private void move(){
         addToHead();
         deleteFromTail();
         checkDead();
     }
     
     private void checkDead(){
         if(head.col<0||head.row<0||head.row>Yard.ROWS || head.col > Yard.COLS){
             y.stop();
         }
         for(Node n=head.next;n!=null;n=n.next){
             if(head.row==n.row&&head.col==n.col)
                 y.stop();
         }
     }
     
     public void keyPressed(KeyEvent e){
         int key=e.getKeyCode();
         switch(key){
             case KeyEvent.VK_LEFT:
                  /*
             * 当按键为左的时候，只要前进方向不是右，即可转向
             * */
            if(head.dir != Dir.R)
                head.dir = Dir.L;
            break;
        case KeyEvent.VK_UP  :
            /*
             * 当按键为"上"，只要前进方向不是"下"，就可以转向
             * */
            if(head.dir != Dir.D)
                head.dir = Dir.U;
            break;
        case KeyEvent.VK_RIGHT  :
            /*
             * 当按键为"右"的时候，只要前进方向不是"左",就可以转向
             * */
            if(head.dir != Dir.L)
                head.dir = Dir.R;
            break;
        case KeyEvent.VK_DOWN :
            /*
             * 当按键为"下"的时候，只要前进方向不是"上",就可以转向
             * */
            if(head.dir != Dir.U)
                head.dir = Dir.D;
            break;
        }
    }
         
     
       public void eat(Egg e) {
        /*
         * boolean intersects(Rectangle r) 
         *  确定此 Rectangle 是否与指定的 Rectangle 相交。
         * 若相交,表示我们吃到了一个点 ，则导致蛇的长度变长并且在出现一个点，并且加5分，否则什么也不做
         * */
        if(this.getRect().intersects(e.getRect())) {
            e.reAppear();
            this.addToTail();
            y.setScore(y.getScore() + 5);//吃了加5分
            y.speedlevel++;
            if(y.speedlevel%4==0)   y.timeshort++;
        }
    }

    private Rectangle getRect() {
        /*
         * 构造了一个格子大小的区域
         * */
        return new Rectangle(Yard.BLOCK_SIZE * head.col, Yard.BLOCK_SIZE * head.row, head.w, head.h);
    }
}