import java.io.IOException;
import java.net.*;

public class Server
{
	
	private static Boolean qualcunoHaVinto;
	static ServerSocket svrsk;
	
	public static void main(String[] args)
	{
		qualcunoHaVinto = false;
		
		try
		{
			Server.svrsk = new ServerSocket(4444);		//Crea un nuovo server socket sulla porta 4444
			svrsk.setReuseAddress(true);
			
			System.out.println("Server attivo");
			System.out.println("");
			
			while (!qualcunoHaVinto)
			{
				try
				{
					Socket client = svrsk.accept();	//Attende fin quando non si connette un nuovo dispositivo
					ServerThread clientSock = new ServerThread(client); 
					
					new Thread(clientSock).start();
				}
				catch (IOException e)
				{
					System.out.println("Errore durante l'attesa di una connessione: " + e.getMessage());
				}
			}
			
			//Chiude il server socket
			svrsk.close();
		}
		catch (IOException e)
		{
			System.out.println("Errore durante la creazione del server socket: " + e.getMessage());
		}
		
	}
	
	//Ritorna true se qualcuno ha gia' vinto, false altrimenti
	public static Boolean qualcunoHaVinto(){
		return qualcunoHaVinto;
	}
	
	//Se qualcuno vince imposta il boolean qualcunoHaVinto su true
	public static void updateVittoria(String nome){
		qualcunoHaVinto = true;
		System.out.println("Partita finita, " + nome + " ha vinto!");
	}
	
	//Chiamo la funzione per la chiusura del server dal thread che vince
	public static void closeServer() {
		try {
			svrsk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

