package cn.bjsxt.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

/**
 * 这个类的作用是坦克游戏的主窗口
 * @author fengsiyu
 *
 */


public class TankClient extends Frame {

	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 600;
	
	
	Tank myTank = new Tank(400,550, true,Direction.STOP,this);
	Wall wall = new Wall(400,400,this);
	
	Blood b = new Blood();
	
	
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank>  tanks = new ArrayList<Tank>();

	Image offScreenImage = null;

	public void paint(Graphics g) {
		Color c=g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("missiles count:" + missiles.size(), 10, 50);
		g.drawString("explodes count:" + explodes.size(), 10, 70);
		g.drawString("tanks count:" + tanks.size(), 10, 90);
		g.drawString("myTank life:" + myTank.getLife(), 10, 110);
		g.setColor(c);
		
		if(this.tanks.size()<=0){
			
			int reproduceTankCount= Integer.parseInt(PropertyMgr.getProperty("reproduceTankCount"));
			for(int i=0;i<reproduceTankCount;i++){
				tanks.add(new Tank(50+40*(i+1),50,false,Direction.D, this));
			}
		}
		
		for(int i=0;i<missiles.size();i++){
			Missile m= missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(wall);
			m.draw(g);
		}
		for(int i=0;i<explodes.size();i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		for(int i=0;i<tanks.size();i++){
			Tank t = tanks.get(i);
			t.draw(g);
			t.collideWithWall(wall);
			t.collideWithTank(tanks);
		}
		myTank.draw(g);
		myTank.eatBlood(b);
		wall.draw(g);
		b.draw(g); 
		
		
	}

	@Override
	public void update(Graphics g) { // 消除双缓冲,现在一张虚拟图片上画，再将这张图片一次性地添加到frame中
		if (offScreenImage == null) {
			offScreenImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		}

		Graphics goffScreen = offScreenImage.getGraphics();
		Color c = goffScreen.getColor();
		goffScreen.setColor(Color.BLACK);
		goffScreen.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		goffScreen.setColor(c);
		paint(goffScreen);
		g.drawImage(offScreenImage, 0, 0, null);

	}
	/**
	 * 本方法显示坦克主窗口
	 * 
	 */
	public void launchFrame() {
		
		/*
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		int initTankCount= Integer.parseInt(props.getProperty("initTankCount"));
		*/
		int initTankCount= Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
		
		
		for(int i=0;i<initTankCount;i++){
			tanks.add(new Tank(50+40*(i+1),50,false,Direction.D, this));
		}
		
		
		this.setBounds(400, 200, FRAME_WIDTH, FRAME_HEIGHT);
		this.setTitle("坦克大战");
		this.setBackground(Color.GREEN);
		this.setVisible(true);
		this.setResizable(false); // 窗口不允许改变大小
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMoniter());
		new Thread(new PaintThread()).start();

	}

	private class PaintThread implements Runnable {

		@Override
		public void run() {

			while (true) {
				repaint(); // 先调用update(),update()再调用paint()
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private class KeyMoniter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyControl(e);
			System.out.println(e.getKeyCode());
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyCancel(e);
		}

	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
 
	}

}
