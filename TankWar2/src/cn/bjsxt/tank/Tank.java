package cn.bjsxt.tank;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class Tank {

	int x, y;
	int oldX,oldY;
	boolean bL, bU, bR, bD;
	private boolean good;
	private boolean live = true;
	private int life =100;
	private BloodBar bb = new BloodBar();

	
	private Direction dir = Direction.STOP;
	private Direction ptdir= Direction.D;
	
	private static  Random r = new Random();  //   多个对象共享一个随即数发生器
	int step = r.nextInt(12)+3;       // 产生3到15的随即整数
	
	final int X_SPEED = 4;
	final int Y_SPEED = 4;
	
	TankClient tc;
	
	 private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	
	 private static Image[] tankImages = null;	
	 private static  Map<String, Image> imgs = new HashMap<String, Image>();
	 
	 static{
		 tankImages = new Image[]{
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")), 
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")), 
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")), 
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")), 
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")), 
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")), 
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif")), 	
					
		 };
		 
		 imgs.put("L", tankImages[0]);
		 imgs.put("LU", tankImages[1]);
		 imgs.put("U", tankImages[2]);
		 imgs.put("RU", tankImages[3]);
		 imgs.put("R", tankImages[4]);
		 imgs.put("RD", tankImages[5]);
		 imgs.put("D", tankImages[6]);
		 imgs.put("LD", tankImages[7]);
		
	 }
	 
	 
	final static int WIDTH=30;
	final static int HEIGHT=30;
	

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX=x;
		this.oldY=y;
		this.good=good;
	}
	
	public Tank(int x, int y,boolean good, Direction dir,TankClient tc ){    // 持有对方的引用
		this(x,y,good);
		this.tc = tc;
		this.dir=dir;
	}
	
	public void draw(Graphics g) {
		if(!live){
			if(!good){
				tc.tanks.remove(this);     
			}
		return;	 						//如果坦克被打中，就不再画了
		}
		
		if(good){
			bb.draw(g);
		}
		
		switch(ptdir){
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
	
		
		}
		move();
	}
	
	public void move(){
		this.oldX=x;
		this.oldY=y;
		switch(dir){
		case L:
			x-=X_SPEED;
			break;
		case LU:
			x-=X_SPEED;
			y-=Y_SPEED;
			break;
		case U:
			y-=Y_SPEED;
			break;
		case RU:
			x+=X_SPEED;
			y-=Y_SPEED;
			break;
		case R:
			x+=X_SPEED;
			break;
		case RD:
			x+=X_SPEED;
			y+=Y_SPEED;
			break;
		case D:
			y+=Y_SPEED;
			break;
		case LD:
			x-=X_SPEED;
			y+=Y_SPEED;
			break;
		case STOP:
			break;
		}
		if(this.dir!=Direction.STOP){
			this.ptdir=dir;
		}
		if(x<0) x=0;
		if(y<20) y=20;
		if(x+Tank.WIDTH>TankClient.FRAME_WIDTH) x=TankClient.FRAME_WIDTH-Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.FRAME_HEIGHT) y=TankClient.FRAME_HEIGHT-Tank.HEIGHT;
		
		
		
		if(!good){
			
			Direction[] dirs= Direction.values(); //   将枚举类型转换为数组
			if(step==0){
				step= r.nextInt(12)+2;
				int randomNumber = r.nextInt(dirs.length); //产生从0到dirs.length的随即数
				dir= dirs[randomNumber];
				
			}     // 走一些步数再改变方向
			
			step--;
			if(r.nextInt(40)>35){
				this.fire();
			}
		}
	}

	public void keyControl(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case(81):
			if(!this.live){
				this.live=true;
				this.life=100;
			}
			break;
		case (KeyEvent.VK_LEFT):
			bL = true;
			break;
		case (KeyEvent.VK_UP):
			bU = true;
			break;
		case (KeyEvent.VK_RIGHT):
			bR = true;
			break;
		case (KeyEvent.VK_DOWN):
			bD = true;
			break;
		default:break;

		}
		locateDirection();

	}

	public void keyCancel(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case(157):             // 157为mac中command的keycode
			fire();      // 将 TankClient中的 missle初始化
			break;
		case(32):          // 空格键大招
			superFire();
			break;
		case (KeyEvent.VK_LEFT):
			bL = false;
			break;
		case (KeyEvent.VK_UP):
			bU = false;
			break;
		case (KeyEvent.VK_RIGHT):
			bR = false;
			break;
		case (KeyEvent.VK_DOWN):
			bD = false;
			break;
		default: break;

		}
		locateDirection();

	}
	
	public void locateDirection(){
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		if(bL && bU && !bR && !bD) dir = Direction.LU;
		if(!bL && bU && !bR && !bD) dir = Direction.U;
		if(!bL && bU && bR && !bD) dir = Direction.RU;
		if(!bL && !bU && bR && !bD) dir = Direction.R;
		if(!bL && !bU && bR && bD) dir = Direction.RD;
		if(!bL && !bU && !bR  && bD) dir = Direction.D;
		if(bL && !bU && !bR && bD) dir = Direction.LD;
		if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
		
	}
	
	public Missile fire(){
		if(!live) {
			return null;
		}
		int x= this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y= this.y+ Tank.WIDTH/2-Missile.WIDTH/2;
	
		Missile m = new Missile(x,y,ptdir,good,tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Missile fire(Direction dir){
		if(!live) {
			return null;
		}
		int x= this.x+ Tank.WIDTH/2-Missile.WIDTH/2;
		int y= this.y+ Tank.WIDTH/2-Missile.WIDTH/2;
	
		Missile m = new Missile(x,y,dir,good,tc);
		tc.missiles.add(m);
		return m;
	}
	
	
	public Rectangle getRect(){
		return  new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public void stay(){
		this.x=oldX;
		this.y=oldY;
	}
	
	public boolean collideWithWall(Wall w){
		if(this.live&&this.getRect().intersects(w.getRect())){
			stay();
			return true;
		}
		return false;
	}
	
	public boolean collideWithTank(java.util.List<Tank> tanks){
		 for(int i=0;i<tanks.size();i++){
			 Tank t = tanks.get(i);
			 if(this!=t){
				 if(this.live&&t.isLive()&&this.getRect().intersects(t.getRect())){
					 this.stay();
					 t.stay();
					 return true;
				 }
			 }
		 }
		 return false;
	}
	
	private void superFire(){
		Direction[] dirs = Direction.values();
		for(int i=0; i<8;i++){
			fire(dirs[i]) ;
		}
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	private class BloodBar {
		private void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 10);
			int w=WIDTH *life/100;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}
	}

	public boolean eatBlood(Blood b){
		if(this.live&&b.isLive()&&this.getRect().intersects(b.getRect())){
			this.life=100; 
			b.setLive(false);
			return true;
		}
		return false;
	}
	
	
}
