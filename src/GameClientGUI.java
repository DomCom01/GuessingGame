import java.util.Scanner;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameClientGUI extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Dichiara i campi testo per l'input utente e il server response
	private JTextField userInputField;
	private JTextField serverResponseField;
	
	// Bottone per inviare lo user input al server
	private JButton sendButton;
	
	//Dichiara la Label per il testo di benvenuto
	private JLabel testoBenvenuto = new JLabel("Benvenuto, inserisci prima il tuo nome e premi send "
			+ "poi prova ad indovinare");
	
	// Dichiara un socket per la connessione al server
	private Socket GameSocket;
	
	// Stream di input e output per il socket
	private Scanner skIn;
	private PrintWriter skOut;
	
	public GameClientGUI(){
		
	    // Titolo della finestra
	    super("Game Client");
	
	    // Imposta il layout come BorderLayout
	    setLayout(new BorderLayout());
	    
	    // Crea un pannello
	    JPanel inputPanel = new JPanel();
	    inputPanel.setLayout(new FlowLayout());
	    
	    // Crea un pannello per il testo in cima
	    inputPanel.add(testoBenvenuto);
	    
	    // Crea il campo per lo user input e lo aggiunge al pannello
	    userInputField = new JTextField(20);
	    inputPanel.add(userInputField);
	
	    // Crea il bottone e lo aggiunge al pannello
	    sendButton = new JButton("Send");
	    inputPanel.add(sendButton);
	
	    // Aggiunge l'input panel al centro
	    add(inputPanel, BorderLayout.CENTER);
	
	    // Crea un pannello per la risposta del server
	    JPanel responsePanel = new JPanel();
	    responsePanel.setLayout(new FlowLayout());
	
	    // Crea il campo per la risposta del server e lo aggiunge al pannello
	    serverResponseField = new JTextField(32);
	    serverResponseField.setEditable(false);
	    responsePanel.add(serverResponseField);
	
	    // Aggiunge il pannello risposta in basso
	    add(responsePanel, BorderLayout.SOUTH);
	
	    // Imposta la dimensione della finestra e la visibilita'
	    setSize(500, 300);
	    setVisible(true);
	
	    // Imposta l'operazione di chiusura di default
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	    // Aggiunge un listener per il bottone di invio
	    sendButton.addActionListener(this);
	    
	    try 
	    {
	        // Connessione al server
	        GameSocket = new Socket("127.0.0.1", 4444);
	        System.out.println("Connessione stabilita");
	
	        // Inizializza input e output
	        skIn = new Scanner(GameSocket.getInputStream());
	        skOut = new PrintWriter(GameSocket.getOutputStream(),true);

        }
        catch (ConnectException e) //Nessuna connessione al server
        {
            System.err.println("Non riesco a connettermi, controlla che il server sia attivo");
            System.exit(1);
        } 
        catch (IOException e) 	   //Errore nell'input/output
        {
            System.err.println("Errore I/O");
            System.exit(1);
        }
    }

    public void actionPerformed(ActionEvent event)
    {
        // Prende lo user input
        String userInput = userInputField.getText();
        
        // Invia lo user input al server
        	skOut.println(userInput);
        // Prende la risposta dal server
        String serverResp = skIn.nextLine();

        // Stampa la risposta del server
        serverResponseField.setText(serverResp);

        // Pulisce il campo di input
        userInputField.setText("");
    }

    public static void main(String[] args)
    {
        // Crea la GameClientGUI
        new GameClientGUI();
    }
}