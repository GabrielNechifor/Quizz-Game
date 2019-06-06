package gameInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JFrame;

import database.Database;


public class Main {
	
	private static int ordinea_intrebarii = 0;
	private static int exit_din_prima = 0;

	public static void main(String[] args) throws SQLException {
		JFrame frame = new JFrame ("Quizzz Game");
		LoginWindow log = new LoginWindow();
		GameWindow game = new GameWindow();
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (log);
        frame.pack();
        frame.setVisible (true);
        
        Connection con = Database.getConnection();
    	CallableStatement client = con.prepareCall("declare "
    			+ "retval BINARY_INTEGER; "
    			+ "raspuns varchar2(1000):=''; "
    			+ "begin client (?,tcp_connection.CONN,retval,raspuns); "
    			+ "end;");
    
        
	log.getLogin().addActionListener(new ActionListener() {
   	   @SuppressWarnings("deprecation")
	@Override
   	   public void actionPerformed(ActionEvent e) {
   		  String mesaj = null ;
   		  exit_din_prima = 1;
   		   try {
			client.registerOutParameter(1, Types.VARCHAR);
		    client.setString(1, "login " + log.getJcomp3().getText() + " " + log.getJcomp4().getText());
			client.execute();
		     mesaj = client.getString(1);
		     System.out.println(mesaj);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
   		   if(mesaj.equals("Logarea a fost efectuata cu succes! ")) {
   			  frame.getContentPane().remove(log);
   			  frame.getContentPane().add (game);
   	          frame.pack();
   	          frame.setVisible (true);
   	          game.getSubmit().disable();
   		   }
   		   else {
   			   log.getInfo().append(mesaj);
   		   }
   	   }
      });
	
	log.getRegister().addActionListener(new ActionListener() {
	   	   @SuppressWarnings("deprecation")
		@Override
	   	   public void actionPerformed(ActionEvent e) {
	   		String mesaj = null ;
	   		  exit_din_prima = 1;
	   		   try {
				client.registerOutParameter(1, Types.VARCHAR);
			    client.setString(1, "register " + log.getJcomp3().getText() + " " + log.getJcomp4().getText());
				client.execute();
			     mesaj = client.getString(1);
			     System.out.println(mesaj);
			   
			     log.getInfo().append(mesaj);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	   	   }
	      });
	
	log.getExit().addActionListener(new ActionListener() {
	   	   @Override
	   	   public void actionPerformed(ActionEvent e) {
	   		  if(exit_din_prima == 1) {
	   			  
	   			try {
					client.setString(1, "exit");
					client.execute();
					client.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					//System.exit(0);
		   		   frame.dispose();
	   		  }else {
		   		   frame.dispose();

	   		  }	   		
	   	   }
	      });
	
	game.getExit().addActionListener(new ActionListener() {
	   	   @Override
	   	   public void actionPerformed(ActionEvent e) {
	   		try {
				client.setString(1, "exit");
				client.execute();
				client.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
              frame.dispose();
	   	   }
	      });

	game.getRoom().addActionListener(new ActionListener() {
	   	   @Override
	   	   public void actionPerformed(ActionEvent e) {
	   		String mesaj = "" ;
	   		   try {
				client.registerOutParameter(1, Types.VARCHAR);
			    client.setString(1, "new room " + game.getJcomp3().getSelectedItem().toString());
				client.execute();
			     mesaj = client.getString(1);
			     System.out.println(mesaj);
			     game.getListModel().clear();
			     game.getListModel().add(game.getListModel().getSize(),mesaj);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	   	   }
	      });
	
	game.getStart().addActionListener(new ActionListener() {
	   	   @SuppressWarnings("deprecation")
		@Override
	   	   public void actionPerformed(ActionEvent e) {
	   		String mesaj = "";
	   		   try {
				client.registerOutParameter(1, Types.VARCHAR);
			    client.setString(1, "start");
				client.execute();
			     mesaj = client.getString(1);
			     ordinea_intrebarii = 1;
			     System.out.println(mesaj);
			     game.getListModel().clear();
			     game.getListModel().add(game.getListModel().getSize(),"<html>" + mesaj.split("_")[0] + "<br><br>" 
			                              + mesaj.split("_")[1] + "<br><br>" + mesaj.split("_")[2] + "<br><br>" + 
			    		                  mesaj.split("_")[3] + "<br><br>" + mesaj.split("_")[4] + "</html>");
			     game.getSubmit().enable();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	   	   }
	      });
	
	game.getJoin().addActionListener(new ActionListener() {
	   	   @SuppressWarnings("deprecation")
		@Override
	   	   public void actionPerformed(ActionEvent e) {
	   		String mesaj = "" ;
	   		   try {
				client.registerOutParameter(1, Types.VARCHAR);
			    client.setString(1, "join " + game.getJcomp8().getText());
				client.execute();
			     mesaj = client.getString(1);
			     ordinea_intrebarii = 1;
			     System.out.println(mesaj);
			     game.getListModel().clear();
			     game.getListModel().add(game.getListModel().getSize(),"<html>" + mesaj.split("_")[0] + "<br><br>" +
			                              mesaj.split("_")[1] + "<br><br>" + mesaj.split("_")[2] + "<br><br>" + 
		                  mesaj.split("_")[3] + "<br><br>" + mesaj.split("_")[4] + "</html>");
			     game.getSubmit().enable();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	   	   }
	      });
	
	game.getSubmit().addActionListener(new ActionListener() {
	   	   @SuppressWarnings("deprecation")
		@Override
	   	   public void actionPerformed(ActionEvent e) {
	   		String mesaj = "";
	   		String raspuns = "";
	   		if(game.getA().isSelected()) raspuns += "a ";
	   		if(game.getB().isSelected()) raspuns += "b ";
	   		if(game.getC().isSelected()) raspuns += "c ";
	   		if(game.getD().isSelected()) raspuns += "d ";

	   		   try {
				client.registerOutParameter(1, Types.VARCHAR);
			    client.setString(1, raspuns);
				client.execute();
			     mesaj = client.getString(1);
			     ordinea_intrebarii++;
			     System.out.println(mesaj);
			     game.getListModel().clear();
			     if(ordinea_intrebarii >= 2) {
			    	 game.getListModel().add(game.getListModel().getSize(), mesaj);
			    	 game.getSubmit().disable();
			     }
			     else {
			    	 game.getListModel().add(game.getListModel().getSize(),"<html>" + mesaj.split("_")[0] + "<br><br>" 
                             + mesaj.split("_")[1] + "<br><br>" + mesaj.split("_")[2] + "<br><br>" + 
                               mesaj.split("_")[3] + "<br><br>" + mesaj.split("_")[4] + "</html>");
			     }			     
			     game.getA().setSelected(false);
			     game.getB().setSelected(false);
			     game.getC().setSelected(false);
			     game.getD().setSelected(false);

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	   	   }
	      });
	
	game.getRank().addActionListener(new ActionListener() {
	   	   @Override
	   	   public void actionPerformed(ActionEvent e) {
	   		String mesaj = "" ;
	   		   try {
				client.registerOutParameter(1, Types.VARCHAR);
			    client.setString(1, "rank " + game.getJcomp3().getSelectedItem().toString());
				client.execute();
			     mesaj = client.getString(1);
			     System.out.println(mesaj);
			     game.getListModel().clear();
			     game.getListModel().add(game.getListModel().getSize(),mesaj);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	   	   }
	      });
	
	}
}
