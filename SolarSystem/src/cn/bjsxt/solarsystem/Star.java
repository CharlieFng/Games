package cn.bjsxt.solarsystem;

import java.awt.Graphics;
import java.awt.Image;

import cn.bjsxt.util.GameUtil;

/**
	 * @param args
	 */
public class Star {

	
	Image img;
	double x,y;
	double imgWidth, imgHeight;
	
	
	public void draw(Graphics g){
		g.drawImage(img, (int)x, (int)y, null);
	}
	
	public Star(){
		
	}
	
	public Star(Image img){
		this.img=img;
		this.imgWidth=img.getWidth(null);
		this.imgHeight=img.getHeight(null);
	}
	
	public Star(Image img,double x,double y){
		this(img);
		this.x=x;
		this.y=y;
	
	}
	public Star(String imgpath,double x,double y){
		this(GameUtil.getImage(imgpath),x,y); //通过this 调用另一个构造方法
		
	}

}
