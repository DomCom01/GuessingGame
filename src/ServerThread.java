import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;

public class ServerThread extends Thread
{
	private Socket skt = null;				//Inizializza il socket a null
	
	public ServerThread(Socket socket)
	{
		skt = socket;
	}
	
	public void run()
	{
		System.err.println("Un nuovo client si e' connesso");	//Stampa nella console del server
		
		String inputLine;			//Questo è l'input dell'utente come stringa
		int playerguess;			//Questo come intero
		String playerName;	
		
		int random =(int) (Math.random() * 100); //Crea un numero random
		
		try
		{
			Scanner in = new Scanner(skt.getInputStream());	//Prende l'input del client		
			PrintWriter out = new PrintWriter(skt.getOutputStream(), true); //Mandera' i messaggi al client
			
			// Prendo in input lo username
			inputLine = in.nextLine();
			playerName = inputLine;
			
			//Controllo che qualcuno non abbia gia' vinto
			if(Server.qualcunoHaVinto()) {				//Se qualcuno ha gia' vinto avvisa il client e chiude scanner, printer e socket
				out.println("Hai perso!");
				out.close();			
				in.close();
				skt.close();
			}else {
				out.println("Ciao " + playerName + " Prova ad indovinare il numero!");
			}
			
			// Inizia il gioco
			while (true)
			{
				inputLine = in.nextLine();					//Prende il numero dato in input dall'utente
				playerguess = Integer.parseInt(inputLine);	//Lo converte in Integer
				if(Server.qualcunoHaVinto()) {				//Se qualcuno ha gia' vinto avvisa il client e chiude scanner, printer e socket
					out.println("Hai perso!");
					out.close();			
					in.close();
					skt.close();
				}
				
				if (playerguess == random)					//Se l'utente indovina 
				{
					out.println("Congratulazioni, hai vinto!"); 
					Toolkit.getDefaultToolkit().beep();     
					Server.updateVittoria(playerName);					//Aggiorna il booleano nel server per dirgli che qualcuno ha vinto
					out.close();								//Termina l'esecuzione
					in.close();
					Server.closeServer();
					skt.close();
					break;									//Esce dal while
				}
				else if (playerguess < random)					//Se il numero provato è piu' basso
				{
					out.println(playerguess + " è più basso del numero da indovinare");
				}
				else if (playerguess > random)					//Se il numero provato è piu' alto
				{
					out.println(playerguess + " è più alto del numero da indovinare");
				}
			}
			//Chiude le connessioni
			out.close();			
			in.close();
			skt.close();
		}
		catch (Exception e)											//Se il client si disconnette
		{
			System.err.println("La connessione del client è terminata");	//Avvisa il server
		}
	}
}