package cn.bjsxt.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;




	/**
	 * 
	 * 游戏开发中常用的工具类（比如：加载图片的方法）
	 * @param args
	 */
public class GameUtil {
	
	private  GameUtil(){}  // 工具类通常将构造方法私有了
	
	public static Image getImage(String path){
		URL u=GameUtil.class.getClassLoader().getResource(path);
		BufferedImage img=null;
		
		try {
			img= ImageIO.read(u);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return img;
	}
}
