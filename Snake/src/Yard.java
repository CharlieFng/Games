import java.awt.*;
import java.awt.event.*;

/**
 * @param args
 */
public class Yard extends Frame {
	
	PaintThread paintThread =new PaintThread();
	private boolean gameOver = false;

	public static final int ROWS = 30;
	public static final int COLS = 30;

	public static int BLOCK_SIZE = 15;
	private int score =0;

	public void setScore(int score) {
		this.score = score;
	}
	public int getScore(){
		return score;
	}


	Snake s = new Snake(this);
	Egg e = new Egg();

	public void launchFrame() {

		this.setLocation(200, 200);
		this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		this.addWindowListener(new WindowAdapter() {

			@Override     方法
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
		this.addKeyListener(new KeyMoniter());
		this.setVisible(true);
		
		new Thread(paintThread).start();

	}
	
	
	
	public void stop(){
		gameOver= true;
	}

	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		g.setColor(Color.DARK_GRAY);
		for (int i = 1; i < COLS; i++) {
			g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, ROWS * BLOCK_SIZE);
		}
		for (int i = 1; i < ROWS; i++) {
			g.drawLine(0, BLOCK_SIZE * i, COLS * BLOCK_SIZE, BLOCK_SIZE * i);
		}
		g.setColor(Color.YELLOW);
		g.drawString("score"+score, 10, 60);
		g.setColor(c);
		
		if(gameOver){ 
			g.setFont(new Font(" 华文彩云",Font.BOLD,50));
			g.drawString("game over", 60, 180);
			paintThread.pause();
		}
		

		s.draw(g);
		e.draw(g);
		s.eat(e);
		
		
	}

	private class PaintThread implements Runnable {
		private boolean pause = false;
		private boolean running  = true;
		@Override
		public void run() {
			while (running) {
				if(pause) continue;
				else repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		public void pause() {
			this.pause = true;
			
		}

		public void gameOver() {
			running = false;
			
		}
		
		public void reStart(){
			this.pause = false;
			s = new Snake(Yard.this);
			gameOver = false;
		}

	}

	
	
	private class KeyMoniter extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key== KeyEvent.VK_F2);{
				paintThread.reStart();
			}
			s.keyPressed(e);
		}
	
	}
	
	
	public static void main(String[] args) {

		new Yard().launchFrame();

	}

}
