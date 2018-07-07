package TanChishe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Egg {
    int row, col;
    int w = Yard.BLOCK_SIZE;
    int h = Yard.BLOCK_SIZE;
    private Color color = Color.PINK;
    //运动时的位置
    public Egg(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    private static Random r = new Random();
    //初始时在位置
    public Egg() {
        this(r.nextInt(Yard.ROWS) , r.nextInt(Yard.COLS));
    }

    public void reAppear() {
        this.row = r.nextInt(Yard.ROWS);
        this.col = r.nextInt(Yard.COLS);
    }

    public Rectangle getRect() {
        return new Rectangle(Yard.BLOCK_SIZE * col, Yard.BLOCK_SIZE * row, w, h);
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(color);
        /*
         * public abstract void fillOval(int x,
                              int y,
                              int width,
                              int height)使用当前颜色填充外接指定矩形框的椭圆。
         * */
        g.fillOval(Yard.BLOCK_SIZE * col, Yard.BLOCK_SIZE * row, w, h);
        g.setColor(c);
       // if(color == Color.GREEN) 
            color = Color.RED;
      //  else color = Color.GREEN;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

}
