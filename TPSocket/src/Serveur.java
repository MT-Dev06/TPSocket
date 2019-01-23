import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

	public static void main(String[] args) {

		
		try {
			ServerSocket ss = new ServerSocket(1234) ;
			System.out.println("J'attends une connexion...");
			Socket s = ss.accept() ; 
			InputStream is = s.getInputStream() ; 
			OutputStream os = s.getOutputStream();
			System.out.println("J'attends un nombre");
			int nb = is.read() ; 
			System.out.println("Je viens de reçevoir le nombre : "+nb);
			int res = nb*12 ; 
			System.out.println("J'envois la réponse");
			os.write(res) ; 
			s.close();
		} catch (IOException e) {

			
			e.printStackTrace();
		}
	}

}
