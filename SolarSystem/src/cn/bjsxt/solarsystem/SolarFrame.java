package cn.bjsxt.solarsystem;

import java.awt.Graphics;
import java.awt.Image;

import cn.bjsxt.util.Constant;
import cn.bjsxt.util.GameUtil;
import cn.bjsxt.util.MyFrame;

/**
 * 
 * 太阳系的主窗口
 * @param args
 */
public class SolarFrame extends MyFrame {
	Image bg=GameUtil.getImage("images/bg.jpg");
	Star sun= new Star("images/sun.jpg",Constant.GAME_WIDTH/2,Constant.GAME_HEIGHT/2);
	
	Planet Mercury=new Planet(sun,"images/Mercury.jpg",50,40,0.47);
	Planet Venus=new Planet(sun,"images/Venus.jpg", 90,72,0.35);
	Planet Earth=new Planet(sun,"images/Earth.jpg",125,100,0.29);
	Planet Mars=new Planet(sun,"images/Mars.jpg",150,130,0.24);
	Planet Jupiter=new Planet(sun,"images/Jupiter.jpg",220,160,0.13);
	Planet Saturn=new Planet(sun,"images/Saturn.jpg",280,200,0.09);
	Planet Uranus=new Planet(sun,"images/Uranus.jpg",330,240,0.06);
	Planet Neptune=new Planet(sun,"images/Neptune.jpg",380,280,0.05);
	
	
	Planet Moon=new Planet(Earth,"images/moon.jpg",20,15,0.3,true);
	
	
	
	
	
	
	
	public void paint(Graphics g){
		g.drawImage(bg, 0, 0, null);
		sun.draw(g);
		
		Mercury.draw(g);
		Venus.draw(g);
		Earth.draw(g);
		Mars.draw(g);
		Jupiter.draw(g);
		Saturn.draw(g);
		Uranus.draw(g);
		Neptune.draw(g);
		
		Moon.draw(g);
	
	}
 
	public static void main(String[] args){
		new SolarFrame().launchFrame();

	
	}
	
}
	
