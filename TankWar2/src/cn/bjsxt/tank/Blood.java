package cn.bjsxt.tank;
import java.awt.*;


public class Blood {
	int x,y, w,h;
	int step=0;
	 private boolean live = true;
	
	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isLive() {
		return live;
	}


	private int[][] position={
			{350,300},{370,250},{346,200,},{400,220},{340,220},{240,220}
					};
	
	public void draw(Graphics g){
		
		if(!live){
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}
	
	public Blood(){
		x=position[0][0];
		y=position[0][1];
		w=h=15;
	}
	
	private void move(){
		step++;
		if(step==position.length){
			step=0;
		}
		x=position[step][0];
		y=position[step][1];
	}
	
	
	public Rectangle getRect(){
		return  new Rectangle(x,y,w,h);
	}
}
