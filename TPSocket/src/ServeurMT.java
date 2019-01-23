import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurMT extends Thread{

	private int nbClient ; 
	private int nbSecret ; 
	private boolean fin ; 
	private String gagnant ;
	
	public static void main(String[] args) {

		new ServeurMT().start();
		
		
	}
	@Override
	public void run() {
		
		try {
			ServerSocket ss = new ServerSocket(1234) ;
			System.out.println("Demarrage du serveur multi thread");
			
			//nbSecret = (int)(Math.random()*1000);
			
			nbSecret = new Random().nextInt(1000);
			
			System.out.println("Le serveur a généré un nombre secret");
			
			while(true) {
				Socket s = ss.accept();
				++nbClient;
				new Conversation(s,nbClient).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	class Conversation extends Thread {
		private Socket socket ; 
		private int numero ;
		
		public Conversation(Socket s, int n) {
			this.socket = s;
			this.numero = n ; 
		}

		@Override
		public void run() {
			try {
				
				InputStream is = socket.getInputStream() ;
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr) ;
				OutputStream os = socket.getOutputStream(); 
				PrintWriter pw = new PrintWriter(os,true);
				
				String ip = socket.getRemoteSocketAddress().toString();
				
				System.out.println("connexion du client numero "+numero+",IP = "+ip);
				
				pw.print("Bienvenue, vous etes client numero : "+numero+"\n");
				pw.println("Devine le nombre secret entre 0 et 1000");
				while(true) {
					
					String requete = br.readLine() ;
					int nb = Integer.parseInt(requete);
					
					if(fin == false) {
						if(nb<nbSecret) pw.println("votre nombre est petit");
						else if (nb > nbSecret) pw.println("votre nombre est grand ");
						else {
							fin = true ; 
							System.out.println("BRAVO a "+ip);
							gagnant = ip; 
							pw.println("Bravo vous avez gagné !");
						}
						
					}else {
						pw.println("Jeux terminé, le gagant est le "+gagnant);
					}
					
					//pw.println(requete.length());
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}

}
