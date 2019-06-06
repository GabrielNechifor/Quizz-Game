package businesslogic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SocialNetworkServer {
    private static final int PORT = 8110;
    private ServerSocket serverSocket;
    private boolean running = false;

    public static void main(String[] args)  {
        SocialNetworkServer server = new SocialNetworkServer();
        server.init();
        server.waitForClients();
    }
    
    
     private void waitForClients() {
    	 Socket socket;
		try {
			while(running) {
			socket = serverSocket.accept();
	    	 new ClientThread(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void init()  {
    	 try {
			serverSocket = new ServerSocket(PORT);
			running=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	 
    	 
		
	}


    public void stop() throws IOException {
        this.running = false;
        serverSocket.close();
    }
    
}