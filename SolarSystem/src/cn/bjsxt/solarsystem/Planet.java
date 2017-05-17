package cn.bjsxt.solarsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import cn.bjsxt.util.GameUtil;

/**
	 * @param args
	 */

public class Planet extends Star{
	
	// 除了图片，坐标。行星沿着某个椭圆运行：长轴，短轴，速度，角度，绕着某个Star飞 
	double longAxis;
	double shortAxis;
	double speed;
	double degree;
	Star center;
	
	boolean satellite;
	
	
	

	public Planet(Star center,String imgpath, double longAxis,
			double shortAxis, double speed) {
		super(GameUtil.getImage(imgpath));
		this.center=center;
		this.x=center.x+longAxis;
		this.y=center.y;
		 
		this.longAxis = longAxis;
		this.shortAxis = shortAxis;
		this.speed = speed;
		
	}
	
	public Planet(Star center,String imgpath, double longAxis,
			double shortAxis, double speed,boolean satellite){
		this( center, imgpath,  longAxis,shortAxis, speed);
		this.satellite=satellite;
	}

	public Planet(Image img, double x, double y) {
		super(img, x, y);
		   
	}
	
	public Planet(String imgpath, double x, double y) {
		super(imgpath, x, y);
		
	}
	
	
	public void draw(Graphics g){
		if(!satellite){
			drawTrace(g);
		}
		
		super.draw(g);
		this.move();	
		
	}
	
	
	public void drawTrace(Graphics g){
		double ovalX,ovalY,ovalWidth,ovalHeight;
		
		ovalWidth=longAxis*2;
		ovalHeight=shortAxis*2;
		ovalX=(center.x+center.imgWidth/2)-longAxis;
		ovalY=(center.y+center.imgHeight/2)-shortAxis;
		
		Color c=g.getColor(); // 获得原始画笔颜色
		g.setColor(Color.cyan);//改变画笔颜色 
		g.drawOval((int)ovalX,(int)ovalY,(int)ovalWidth,(int)ovalHeight);
		g.setColor(c);  // 还原原始画笔颜色
		
	}
	public void move(){
		this.x=(center.x+center.imgWidth/2)+longAxis*Math.cos(degree);
		this.y=(center.y+center.imgHeight/2)+shortAxis*Math.sin(degree);
		
		degree+=speed;
		
	}
	
	
}
