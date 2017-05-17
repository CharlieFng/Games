import java.awt.*; 
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.io.*;


/**
 * @param args
 */
public class ChatClient extends Frame {

	TextField tf = new TextField();
	TextArea ta = new TextArea();
	
	Socket s=null;
	DataOutputStream dos=null;
	DataInputStream dis=null; 
	private boolean bConnected = false;
	
	Thread tRecvThread = new Thread(new RecvThread());
	
	

	public void launchFrame() {
		this.setLocation(400, 300);
		this.setSize(300, 300);
		add(tf, BorderLayout.SOUTH);
		add(ta, BorderLayout.NORTH);
		this.pack();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
				disconnect();
				System.exit(0);
			}
		});
		
		ta.setEditable(false);
		tf.addActionListener(new TFActionListener());
		
		this.setVisible(true);
		connect();
		
		tRecvThread.start();

		
	}
	
	
	public void connect(){
		try {
			s= new Socket("127.0.0.1",8888);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
System.out.println("connected!");
			bConnected = true;
		} catch (UnknownHostException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		try {
			dos.close();
			dis.close();
			s.close();
		}catch(IOException e){
			 e.printStackTrace();
		}
		
	}
	
	private class TFActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			 
			String str= tf.getText();
			//ta.setText(str.trim());
			tf.setText("");
			try {
				dos.writeUTF(str);
				dos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}
	
	
	
	private class RecvThread implements Runnable{

		@Override
		public void run() {
			try{
				while(bConnected){
					String str = dis.readUTF();
					//System.out.println(str);
					ta.setText(ta.getText()+str+"\n");
				}
			}catch (SocketException e){   
				System.out.println("退出了，bye！");
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {

		ChatClient cc = new ChatClient();
		cc.launchFrame();
	}

}
