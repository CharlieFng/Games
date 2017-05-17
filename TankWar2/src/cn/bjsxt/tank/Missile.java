package cn.bjsxt.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {

	int x, y;
	Direction dir;
	final static int X_SPEED = 10, Y_SPEED = 10;

	final static int WIDTH = 10;
	final static int HEIGHT = 10;

	private boolean isLive = true;
	private boolean good; // 设性子弹的好坏

	private TankClient tc;

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] missileImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();

	static {
		missileImages = new Image[] {
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileLU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileRU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileRD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource(
						"images/missileLD.gif")),

		};

		imgs.put("L", missileImages[0]);
		imgs.put("LU", missileImages[1]);
		imgs.put("U", missileImages[2]);
		imgs.put("RU", missileImages[3]);
		imgs.put("R", missileImages[4]);
		imgs.put("RD", missileImages[5]);
		imgs.put("D", missileImages[6]);
		imgs.put("LD", missileImages[7]);

	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, Direction dir, boolean good, TankClient tc) {
		this(x, y, dir);
		this.tc = tc;
		this.good = good;
	}

	public void draw(Graphics g) {
		if (!isLive) {
			tc.missiles.remove(this);
			return;
		}

		move(g);
	}

	private void move(Graphics g) {
	
		switch (dir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			x -= X_SPEED;
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			x -= X_SPEED;
			y -= Y_SPEED;
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			y -= Y_SPEED;
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			x += X_SPEED;
			y -= Y_SPEED;
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			x += X_SPEED;
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			x += X_SPEED;
			y += Y_SPEED;
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			y += Y_SPEED;
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			x -= X_SPEED;
			y += Y_SPEED;
			break;

		}
		if (x < 0 || y < 0 || x > TankClient.FRAME_WIDTH
				|| y > TankClient.FRAME_HEIGHT) {
			isLive = false;
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean hitTank(Tank t) {
		if (this.isLive && this.getRect().intersects(t.getRect()) && t.isLive()
				&& this.good != t.isGood()) {
			if (t.isGood()) {
				t.setLife(t.getLife() - 20);
				if (t.getLife() < 0) {
					t.setLive(false);
				}
			} else {
				t.setLive(false);
			}

			this.isLive = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}

	public boolean hitWall(Wall w) {
		if (this.isLive && this.getRect().intersects(w.getRect())) {
			this.isLive = false;
			return true;
		}
		return false;
	}

}
