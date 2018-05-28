package com.doimicuuti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.*;

/**
 * Servlet implementation class Test
 */
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void startRunning(){
		try{
			server = new ServerSocket(6789, 100); //6789 is a dummy port for testing, this can be changed. The 100 is the maximum people waiting to connect.
			while(true){
				try{
					//Trying to connect and have conversation
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException){
					showMessage("\n Server ended the connection! ");
				} finally{
					closeConnection(); //Changed the name to something more appropriate
				}
			}
		} catch (IOException ioException){
			ioException.printStackTrace();
		}
	}
    
  //wait for connection, then display connection information
  	private void waitForConnection() throws IOException{
  		showMessage(" Waiting for someone to connect... \n");
  		connection = server.accept();
  		showMessage(" Now connected to " + connection.getInetAddress().getHostName());
  	}
  	
  	//get stream to send and receive data
  	private void setupStreams() throws IOException{
  		output = new ObjectOutputStream(connection.getOutputStream());
  		output.flush();
  		
  		input = new ObjectInputStream(connection.getInputStream());
  		
  		showMessage("\n Streams are now setup \n");
  	}
  	
  	//during the chat conversation
  	private void whileChatting() throws IOException{
  		String message = " You are now connected! ";
  		sendMessage(message);
  		ableToType(true);
  		do{
  			try{
  				message = (String) input.readObject();
  				showMessage("\n" + message);
  			}catch(ClassNotFoundException classNotFoundException){
  				showMessage("The user has sent an unknown object!");
  			}
  		}while(!message.equals("CLIENT - END"));
  	}
  	
  	public void closeConnection(){
  		showMessage("\n Closing Connections... \n");
  		ableToType(false);
  		try{
  			output.close(); //Closes the output path to the client
  			input.close(); //Closes the input path to the server, from the client.
  			connection.close(); //Closes the connection between you can the client
  		}catch(IOException ioException){
  			ioException.printStackTrace();
  		}
  	}
  	
  	//Send a mesage to the client
  	private void sendMessage(String message){
  		try{
  			output.writeObject("SERVER - " + message);
  			output.flush();
  			showMessage("\nSERVER -" + message);
  		}catch(IOException ioException){
  			//chatWindow.append("\n ERROR: CANNOT SEND MESSAGE, PLEASE RETRY");
  		}
  	}
  	
  	//update chatWindow
  	private void showMessage(final String text){
//  		SwingUtilities.invokeLater(
//  			new Runnable(){
//  				public void run(){
//  					//chatWindow.append(text);
//  				}
//  			}
//  		);
  	}
  	
  	private void ableToType(final boolean tof){
//  		SwingUtilities.invokeLater(
//  			new Runnable(){
//  				public void run(){
//  					//userText.setEditable(tof);
//  				}
//  			}
//  		);
  	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
