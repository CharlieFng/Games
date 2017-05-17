package cn.bjsxt.tank;
import java.awt.*;


public class Wall {
      int x, y;
    final int WIDTH=50,HEIGHT=150;
    
	TankClient tc;
	
	public Wall(int x, int y,TankClient tc ){
		this.x=x;
		this.y=y;
		this.tc=tc;
	}
	
	public void draw(Graphics g){
		
		Color c=g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(x, y, WIDTH, HEIGHT);
		g.setColor(c);
	}
	
	
	public Rectangle getRect(){
		return  new Rectangle(x,y,WIDTH,HEIGHT);
	}
	

}
