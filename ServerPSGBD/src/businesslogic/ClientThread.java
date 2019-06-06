package businesslogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import database.Database;
public class ClientThread extends Thread {
    private Socket socket = null;
    private int logat = 0;
    private String username;
    private int id_camera = -1;
    private int sef_camera = 0;
    private int punctaj_runda = 0;
	 public ClientThread (Socket socket) { this.socket = socket ; }
	 
    public void run() {
        BufferedReader in;
      
		try {

			 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 String request = in.readLine();
			 do {
				 
				 String response = "hhmmm";
					try {
						response = execute(request);
					} catch (SQLException | InterruptedException e) {
						e.printStackTrace();
					}
			        PrintWriter out = new PrintWriter(socket.getOutputStream()); //server -> client stream
			        out.println(response);
			        out.flush();

			        request = in.readLine();
			 }while(request!=null);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			 try {
				 socket.close(); 
				 } catch (IOException e) { System.err.println (e); }
				 }
    }    
    private String execute(String request) throws SQLException, InterruptedException, IOException {
			Connection con = Database.getConnection();
				
       if(request.split(" ")[0].equals("login")) {
           System.out.println("Am primit de la client comanda "+ request + " " + Thread.currentThread().getId());
    	   CallableStatement client = con.prepareCall("begin ? := login(?,?); end;");
   		client.registerOutParameter(1, Types.INTEGER);
   		client.setString(2, request.split(" ")[1]);
   		client.setString(3, request.split(" ")[2]);
   		username = request.split(" ")[1];
   		client.execute();
   		int log = client.getInt(1);
   		client.close();
   		if(log!=0) {
   			logat = 1;
   			return "Logarea a fost efectuata cu succes! ";
   		}
   		else
   			return "Logarea nu s-a reusit! ";
       }
       
       if(request.split(" ")[0].equals("register")) {
		   CallableStatement register = con.prepareCall("begin ? := user_register(?,?); end;");
		   register.registerOutParameter(1, Types.VARCHAR);
		   register.setString(2, request.split(" ")[1]);
		   register.setString(3, request.split(" ")[2]);
		   register.execute();
		   String mesaj = register.getString(1);
		   register.close();
		   return mesaj;
	   }
		
       
       if(logat==1) {
    	  System.out.println("Am primit de la " + username +" comanda "+ request + " " + Thread.currentThread().getId());
        
    	if(request.split(" ")[0].equals("new") && request.split(" ")[1].equals("room")) {
    		CallableStatement client = con.prepareCall("begin insert_camera(?,?); end;");
       		client.setString(1, username);
       		client.registerOutParameter(2, Types.INTEGER);
       		client.execute();
       	    id_camera = client.getInt(2);
       		client.close();
       		CallableStatement intrebari = con.prepareCall("begin insert_intrebari(?,?); end;");
       		intrebari.setInt(1, id_camera);
       		intrebari.setString(2, request.split(" ")[2]);
       		intrebari.execute();
       		intrebari.close();
       		sef_camera = 1;
       		return "Numarul camerei este " + id_camera + " daca esti pregatit de joc apasa pe start";
    	}
    	 
    	if(request.split(" ")[0].equals("join")) {
    		CallableStatement client = con.prepareCall("begin update_camera(?,?,?); end;");
    		id_camera = Integer.parseInt(request.split(" ")[1]);
    		client.setInt(1, id_camera);
       		client.setString(2, username);
       		client.registerOutParameter(3, Types.VARCHAR);
       		client.execute();
       		//String message = client.getString(3);
       		client.close();
       		return this.startGame();
    	}
    	
    	if(request.equals("start")) {
    		return this.startGame();
    	}
    	
    	if(request.split(" ")[0].equals("rank")) {
    		CallableStatement rank = con.prepareCall("begin ? := rank(?,?); end;");
            rank.registerOutParameter(1, Types.INTEGER);
            rank.setString(2, username);
            rank.setString(3, request.split(" ")[1]);
            rank.execute();
            int rnk = rank.getInt(1);
            rank.close();
            return rnk + "";
    	}
    	  
    	  
       return "am procesat comanda " + request + " de la " + Thread.currentThread().getId();
       }
       else
	     return "Nu esti logat! ";
   
    }

	public String startGame() throws SQLException, InterruptedException, IOException {
		
		Connection con = Database.getConnection();

		 System.out.println("Asteapta-ti oponentul!");
		 ///// Sef camera asteapta startul
		 CallableStatement asteptare = con.prepareCall("begin ? := verif_juc(?); end;");
      		asteptare.registerOutParameter(1, Types.INTEGER);
      		asteptare.setInt(2,id_camera);
      	    asteptare.execute();
      	    int astp = asteptare.getInt(1);
      	    while(astp==0) {
      	    	//System.out.println(asteptare.getInt(1) + "  " + Thread.currentThread().getId());
      	    	Thread.sleep(1000);
      	    	asteptare.execute();
      	    	astp = asteptare.getInt(1);
      	    }
      		asteptare.close();
		 
      		//////Incepe jocul
            BufferedReader in;

      		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 String request;
      		String response = null;
			 int id_intrebare;
			 int id_domeniu = 0;
			 for(int i = 1; i<=1; i++) {
				 
				 CallableStatement client = con.prepareCall("begin select_o_intrebare(?,?); end;");
		       		client.setInt(1, id_camera);
		       		client.registerOutParameter(2, Types.INTEGER);
		       		client.execute();
		       		id_intrebare = client.getInt(2);
		       		client.close();	
				
		       		Statement st = con.createStatement();
		       	    ResultSet rs = st.executeQuery("select * from intrebari where id="+id_intrebare);
		       	    while(rs.next()) {
		       	    response = rs.getString(2) + "_" + "a. " + rs.getString(3) + "_" +
		       	               "b. " + rs.getString(4) + "_" + "c. " + rs.getString(5) +
		       	               "_" + "d. " + rs.getString(6);
		       	    id_domeniu = rs.getInt(11);
		       	    }
		       	 PrintWriter out = new PrintWriter(socket.getOutputStream()); //server -> client stream
			        out.println(response);
			        out.flush();
			        ////citim raspunsul si calculam punctajul
			        request = in.readLine();
			        
			        CallableStatement punctaj = con.prepareCall("begin ? := punctaj_intrebare(?,?,?,?,?); end;");
                    punctaj.registerOutParameter(1, Types.INTEGER);
                    punctaj.setInt(2, id_intrebare);
                    if(request.contains("a")) punctaj.setInt(3, 1); else punctaj.setInt(3, 0);
                    if(request.contains("b")) punctaj.setInt(4, 1); else punctaj.setInt(4, 0);
                    if(request.contains("c")) punctaj.setInt(5, 1); else punctaj.setInt(5, 0);
                    if(request.contains("d")) punctaj.setInt(6, 1); else punctaj.setInt(6, 0);
                    punctaj.execute();
                    punctaj_runda += punctaj.getInt(1);
                    punctaj.close();
			        System.out.println(request + " " + punctaj_runda + " " + Thread.currentThread().getId());
			       //jucatorul a raspuns la intrebare 			        
			        CallableStatement raspunde = con.prepareCall("begin raspunde_la_intrebare(?,?); end;");
		       		raspunde.setInt(1, id_camera);
		       		raspunde.setInt(2, id_intrebare);
		       		raspunde.execute();
		       		raspunde.close();	
			        System.out.println(i);
			        // asteapta sa raspunda celalalt
			        CallableStatement asteapta = con.prepareCall("begin ? := au_raspuns_ambii(?,?); end;");
		      		asteapta.registerOutParameter(1, Types.INTEGER);
		      		asteapta.setInt(2,id_camera);
		      		asteapta.setInt(3,id_intrebare);
		      	    asteapta.execute();
		      	    int astpt = asteapta.getInt(1);
		      	    while(astpt==0) {
		      	    	//System.out.println(asteptare.getInt(1) + "  " + Thread.currentThread().getId());
		      	    	Thread.sleep(1000);
		      	    	asteapta.execute();
		      	    	astpt = asteapta.getInt(1);
		      	    }
		      		asteapta.close();
		      		
		      	/////stergem intrebarea
					 CallableStatement sterge = con.prepareCall("begin sterge_o_intrebare(?,?); end;");
			       		sterge.setInt(1, id_camera);
			       		sterge.setInt(2, id_intrebare);
			       		sterge.execute();
			       		sterge.close();	
			        
			}
			 //Sfarsit joc
			 CallableStatement add_pct = con.prepareCall("begin add_pct(?,?,?); end;");
			 add_pct.setInt(1, id_camera);
             add_pct.setInt(2, punctaj_runda);
             add_pct.setInt(3, sef_camera);
             add_pct.execute(); 
             add_pct.close();
             
             CallableStatement verif_pct = con.prepareCall("begin ?:=verif_pct(?); end;");
             verif_pct.registerOutParameter(1, Types.INTEGER);
             verif_pct.setInt(2, id_camera);
             verif_pct.execute();
             int vrf = verif_pct.getInt(1);
             while(vrf == 0) {
            	 verif_pct.execute();
            	 vrf = verif_pct.getInt(1);
             }
             verif_pct.close();
             
			 CallableStatement castigator = con.prepareCall("begin ? := mesaj_final(?,?,?); end;");             
             castigator.registerOutParameter(1, Types.VARCHAR);
             castigator.setInt(2, id_camera);
             castigator.setString(3, username);
             castigator.setInt(4, id_domeniu);
             castigator.execute();
             String mesaj = castigator.getString(1);
             castigator.close();
             punctaj_runda = 0;
             return mesaj;
	}
}

