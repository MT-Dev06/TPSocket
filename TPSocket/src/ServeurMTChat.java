import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurMTChat extends Thread {

	private int nbClient;

	private ArrayList<Socket> sockets = new ArrayList<>();

	public static void main(String[] args) {

		new ServeurMTChat().start();

	}

	@Override
	public void run() {

		try {
			ServerSocket ss = new ServerSocket(1234);
			System.out.println("Demarrage du serveur multi thread");

			while (true) {
				Socket s = ss.accept();
				sockets.add(s);
				++nbClient;
				new Conversation(s, nbClient).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void broadcastMessage(String str, Socket source) {
		for (Socket socket : sockets) {
			try {
				if (socket != source) {
					PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
					pw.println(str);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class Conversation extends Thread {
		private Socket socket;
		private int numero;

		public Conversation(Socket s, int n) {
			this.socket = s;
			this.numero = n;
		}

		@Override
		public void run() {
			try {
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os, true);

				String ip = socket.getRemoteSocketAddress().toString();

				System.out.println("connexion du client numero " + numero + ",IP = " + ip);

				pw.print("Bienvenue, vous etes client numero : " + numero + "\n");

				while (true) {

					String req = br.readLine();
					broadcastMessage(ip + " a envoyé le message :  " + req, socket);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
