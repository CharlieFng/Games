package cn.bjsxt.tank;
import java.awt.*;
public class Explode {
	
	TankClient tc;

	 int x, y;
	 private static Toolkit tk = Toolkit.getDefaultToolkit();
	 private static Image[] imgs = new Image[11];								
	 int step ;
	 private boolean live = true;
	 private static boolean init =false;
	 
	 static{
		 for(int i=0;i<=9;i++){
			 imgs[i]=tk.getImage(Explode.class.getClassLoader().getResource("images/"+i+".gif"));
		 }
		
	 }
	 
	 public Explode(int x, int y, TankClient tc){
		this.x=x;
		this.y=y;
		this.tc = tc;
	 }
	 
	 public void draw(Graphics g){
		 
		 if(!init){
			 for (int i = 0; i < imgs.length; i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}
			init = true;
		 }
 		 if(!live) {
			 tc.explodes.remove(this);
			 return;
		 }
		 
		 if(step == imgs.length){
			 live = false;
			 step =0;
		 }
		 g.drawImage(imgs[step], x, y, null);
		 
		 step++;
	 }

}
