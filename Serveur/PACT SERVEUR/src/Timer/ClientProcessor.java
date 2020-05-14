package Timer;

import java.sql.*;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ClientProcessor implements Runnable {
	public static final String sep = "!@@!!";
	private Socket sock;
	private DataOutputStream writer = null;
	private BufferedInputStream reader = null;
	private String code = "0000";
	final String server = "localhost:3306/";
	final String db_name = "green_guy";
	final String userName = "root";
	final String password = "";
	Statement stmt = null;
	Statement statement = null;
	ArrayList<String> itemList = null;
	Connection myConn;
	ResultSet rs;
	String EOF="[eNdEnD]";
	byte[] buffer = new byte[4096];
	int timeout=1000;
	long time=0;
	long starttime=0;
	public ClientProcessor(Socket pSock) {
		sock = pSock;
		time =System.currentTimeMillis();
		starttime=time;
	}
	public void run() {
		System.err.println("Lancement du traitement de la connexion cliente");
		boolean closeConnexion = false;
		while (!sock.isClosed()) {
			time=System.currentTimeMillis();
			try {
				writer = new DataOutputStream(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());
				String response = read();
				InetSocketAddress remote = (InetSocketAddress) sock.getRemoteSocketAddress();
				String debug = "";
				debug = "Thread : " + Thread.currentThread().getName() + ". ";
				debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() + ".";
				debug += " Sur le port : " + remote.getPort() + ".\n";
				if(response.length()<300) debug += "\t -> Commande reçue : " + response + "\n"; else debug += "\t -> Commande reçue : il y a une image \n";
				System.err.println("\n" + debug);
				String toSend = "";
				String[] responseList = response.split(sep, -1);
				System.out.println("number of intem: " + responseList.length);
				for (String str : responseList) {
					if(str.length()<300) System.out.println(str); else System.out.println("Sans doute une image");
				}
				if (responseList.length != 0) {
					code = responseList[0];
				}
				itemList =new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(responseList, 1, responseList.length)));
						;
				switch (code) {
				case "0001":
					toSend = nouveauProfile();
					break;
				case "0002":
					toSend = login();
					break;
				case "0003":
					toSend= addFriend();
					break;
				case "0004":
					toSend= getAllInfo();
					break;
				case "0005":
					toSend= updateUserPicture();
					break;
				case "0006":
					toSend= getFriendsId();
					break;
				case "0007":
					toSend= getFriendsName();
					break;
				case "0008":
					toSend= getFriendsPic();
					break;
				case "0009":
					toSend= getAllConvId();
					break;
				case "0010":
					toSend= getAllConvName();
					break;
				case "0011":
					toSend= getAllConPic();
					break;
				case "0012":
					toSend= checkNewConv();
					break;
				case "0013":
					toSend= idToPic();
					break;
				case "0014":
					toSend= idToName();
					break;
				case "0015":
					toSend= getAllConvDates();
					break;
				case "0016":
					toSend= getAllConvMsg();
					break;
				case "0017":
					toSend= storeMsg();
					break;
					
				case "0018":
					toSend= newGeneralEvent();
					break;
				case "0019":
					toSend= getAllMLocations();
					break;
				case "0020":
					toSend= getAllMTypes();
					break;
				case "0021":
					toSend= getAllMTitles();
					break;
				case "0022":
					toSend= getAllMID();
					break;
				case "0000":
					closeConnexion = true;
					break;
				default:
					System.out.println("Commande inconnu !");
					closeConnexion = true;
					break;
				}
				 closeSQL();
				toSend += EOF;
				if(toSend.length()<300) System.err.println("\n \t Sending : " + toSend); else System.err.println("\n \t Sending : Sans doute une image" );
				InputStream in = new ByteArrayInputStream(toSend.getBytes(StandardCharsets.UTF_8));
				int count;
				while ((count = in.read(buffer)) > 0) {
					writer.write(buffer, 0, count);
					writer.flush();
				}

				if (closeConnexion || time-starttime>timeout) {
					System.err.println("///////////////////////////FINI////////////////////////// ! "+String.valueOf(time-starttime));
					writer = null;
					reader = null;
					if (myConn!=null)myConn.close();
					sock.close();
					break;
				}

			} catch (SocketException e) {
				System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			if (closeConnexion || time-starttime>timeout) {
				System.err.println("///////////////////////////FINI////////////////////////// ! "+String.valueOf(time-starttime));
				writer = null;
				reader = null;
				try {
					if (sock!=null)sock.close();
					if (myConn!=null)myConn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

		}
	}


	private String getAllMID() {
		try {
			ask("SELECT id FROM events");
			ArrayList<String> ids=new ArrayList<String>();
			
			while (rs.next()) {
				ids.add(rs.getString("id"));
			}
			return format(ids);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getAllMTitles() {
		try {
			ask("SELECT titre FROM events");
			ArrayList<String> titre=new ArrayList<String>();
			
			while (rs.next()) {
				titre.add(rs.getString("titre"));
			}
			return format(titre);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getAllMTypes() {
		try {
			ask("SELECT type FROM events");
			ArrayList<String> type=new ArrayList<String>();
			
			while (rs.next()) {
				type.add(rs.getString("type"));
			}
			return format(type);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getAllMLocations() {
		try {
			ask("SELECT lieu FROM events");
			ArrayList<String> lieu=new ArrayList<String>();
			
			while (rs.next()) {
				lieu.add(rs.getString("lieu"));
			}
			return format(lieu);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String newGeneralEvent() {
		try {
			ask("SELECT MAX(id) FROM events");
			rs.next();
			int id=rs.getInt(1)+1;	
			itemList.add(0, String.valueOf(id));
			System.out.println("id : "+id);
			String[] champs= {"id","type","titre","descr","lieu","deb","fin","creator_id"};
			if (add("events",champs,itemList)) {
				closeSQL();
				String query ="CREATE TABLE `"+"event_"+String.valueOf(id)+"` (\r\n" + 
						" `comment` longtext NOT NULL,\r\n" + 
						" `date` bigint(20) NOT NULL,\r\n" + 
						" `user_id` int(11) NOT NULL\r\n" + 
						") ";
				createTable(query);
				return "true";
			}
		else {
			closeSQL();
			return "champs invalides";
		}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return null;
	}
	private String nouveauProfile() {
		try {
			if (itemList.size()>1) {
				ask("SELECT * FROM users WHERE mail = "+itemList.get(1)); // check if mail used
			}
			else {
				return "champs invalides";
			}
			
			if (rs.next()) {
				System.out.println("mail already used");
				closeSQL();
				return "mail already used";
			}
			else {
				ask("SELECT MAX(id) FROM users");
				rs.next();
				int id=rs.getInt(1)+1;					//get new id and add user
				itemList.add(0, String.valueOf(id));
				String[] champs= {"id","name","mail","pwd","age"};
				if (add("users",champs,itemList)) {
					closeSQL();
					String query ="CREATE TABLE `"+"user_"+String.valueOf(id)+"` (`friend_id` int(11) NOT NULL,\r\n" + 
							" `conv` tinyint(1) NOT NULL DEFAULT 0\r\n" + 
							")  ";
					createTable(query);
					return "true";
				}
				else {
					closeSQL();
					return "champs invalides";
				}
				
				
		  
			}

			

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "true";
	}
	
	private String storeMsg() {
		String[] champs= {"msg","type","date"};
		try {
			itemList.set(4,"'"+itemList.get(4)+"'");
			itemList.set(2,"'"+itemList.get(2).replace("'","''")+"'");
			add("CONV_"+itemList.get(0)+"_"+itemList.get(1),champs,new ArrayList<String>(itemList.subList(2,itemList.size())));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getAllConvMsg() {
		try {
			ArrayList<String> dates=new ArrayList<String>();
			ask("SELECT msg from CONV_"+itemList.get(0)+"_"+itemList.get(1));
			while (rs.next()) {
				dates.add(rs.getString("msg"));
			}
			return format(dates);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	private String getAllConvDates() {
		try {
			ArrayList<String> dates=new ArrayList<String>();
			ask("SELECT date from CONV_"+itemList.get(0)+"_"+itemList.get(1));
			while (rs.next()) {
				dates.add(rs.getString("date"));
			}
			return format(dates);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	private String idToName() {
		try {
			ask("SELECT name from users WHERE id=" + itemList.get(0));
			rs.next();
			return rs.getString("name");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "null";
	}
	private String idToPic() {
		try {
			ask("SELECT photo from users WHERE id=" + itemList.get(0));
			rs.next();
			return rs.getString("photo");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "null";
	}
	
	private String checkNewConv() {
		String id1=itemList.get(0);
		String id2=itemList.get(1);
		try {
			if (exists("CONV_"+id1+"_"+id2)) {
				//rien à faire
			}
			else {
				update("UPDATE user_"+id1+" SET conv='"+1+"' WHERE friend_id = "+id2);
				update("UPDATE user_"+id2+" SET conv='"+1+"' WHERE friend_id = "+id1);
				String query ="CREATE TABLE `CONV_"+id1+"_"+id2+"` (\r\n" + 
						" `msg` longtext NOT NULL,\r\n" + 
						" `date` longtext NOT NULL,\r\n" + 
						" `type` int(11) NOT NULL\r\n" + 
						")";
				createTable(query);
			}
			return null;
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
		return "";
	}
	private String getAllConPic() {
		ArrayList<String> names = new ArrayList<String>();
		for (String id : itemList) {
			try {
				System.out.println("SELECT photo from users WHERE id=" + id);
				ask("SELECT photo from users WHERE id=" + id);
				rs.next();
				names.add(rs.getString("photo"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return format(names);
	}

	private String getAllConvName() {
		ArrayList<String> names = new ArrayList<String>();
		for (String id : itemList) {
			try {
				System.out.println("SELECT name from users WHERE id=" + id);
				ask("SELECT name from users WHERE id=" + id);
				rs.next();
				names.add(rs.getString("name"));
				
				System.out.println("new name");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return format(names);
	}
	private String getAllConvId() {
		try {
			ask("SELECT * from user_" + itemList.get(0));
			ArrayList<String> ids=new ArrayList<String>();
			while(rs.next()) {
				if(String.valueOf(rs.getBoolean("conv")).toLowerCase().equals("true"))
				ids.add(String.valueOf(rs.getInt("friend_id")));
			}
			return format(ids);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getFriendsPic() {
		try {
			ask("SELECT friend_id from user_" + itemList.get(0));
			ArrayList<String> ids=new ArrayList<String>();
			ArrayList<String> pics=new ArrayList<String>();
			while(rs.next()) {
				ids.add(String.valueOf(rs.getInt("friend_id")));
			}
			for (String i :ids) {
				ask("SELECT photo from users WHERE id=" + i);
				rs.next();
				pics.add(rs.getString("photo"));
			}
			return format(pics);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "null";//no friend or error
		
	}
	private String getFriendsName() {
		try {
			ask("SELECT friend_id from user_" + itemList.get(0));
			ArrayList<String> ids=new ArrayList<String>();
			ArrayList<String> pics=new ArrayList<String>();
			while(rs.next()) {
				ids.add(String.valueOf(rs.getInt("friend_id")));
			}
			for (String i :ids) {
				ask("SELECT name from users WHERE id=" + i);
				rs.next();
				pics.add(rs.getString("name"));
			}
			return format(pics);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "null";//no friend or error
		
	}
	private String getFriendsId() {
		try {
			ask("SELECT friend_id from user_" + itemList.get(0));
			ArrayList<String> ids=new ArrayList<String>();
			while(rs.next()) {
				ids.add(String.valueOf(rs.getInt("friend_id")));
			}
			return format(ids);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "null";//no friend or error
	}
	private String updateUserPicture() {
		String id=itemList.get(0);
		String image64=itemList.get(1);
		try {
			update("UPDATE users SET photo='"+image64+"' WHERE id = "+id);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//retrieve the same image
		try {
			ask("SELECT photo from users where id="+id);
			rs.next();
			return rs.getString("photo");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
		
	}
	
	private String getAllInfo() {
		try {
			ask("SELECT * FROM users WHERE id = "+itemList.get(0));
			rs.next();
			ArrayList<String> liste=new ArrayList<String>();
			liste.add(rs.getString("name"));
			liste.add(String.valueOf(rs.getInt("age")));
			liste.add(rs.getString("lieu"));
			liste.add(rs.getString("photo"));
			
			return format(liste);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String addFriend() {
		try {
			System.out.println("SELECT * FROM user_"+itemList.get(0)+" WHERE friend_id = "+itemList.get(1));
			ask("SELECT * FROM user_"+itemList.get(0)+" WHERE friend_id = "+itemList.get(1)); // check if mail used
			
			if (rs.next()) {
				System.out.println("already friend");
				closeSQL();
				return "already friend";
			}
			else {
				String[] champs= {"friend_id"};
				add("user_"+itemList.get(0),champs,new ArrayList<String>(itemList.subList(1,2)));
				add("user_"+itemList.get(1),champs,new ArrayList<String>(itemList.subList(0,1)));
				return "true";
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String login() {
		try {
			ask("SELECT * FROM users WHERE mail = "+itemList.get(0));// take all account info
			if (!rs.next()) {
				return "err";
			}
			else {
				if(itemList.get(1).equals(rs.getString("pwd"))){
					String id_str=String.valueOf(rs.getInt("id"));
					System.out.println("Login id : "+id_str);
					return id_str+sep+rs.getString("name");
				}
				else {
					System.out.print("entered: "+itemList.get(1)+" pws: "+"'"+rs.getString("pwd")+"'");
					return "false";
				}
				
			}
		} catch (ClassNotFoundException e) {
			return "err";
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return "err";
	}

	private void closeSQL() {
		if (stmt != null) {
			try {
				stmt.close();
				if (rs != null) {
					rs.close();

				}
				if (myConn != null) {
					myConn.close();

				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	// "SELECT * FROM table_test" updates rs how gets results
	private void createTable(String request) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection("jdbc:mysql://" + server + db_name, userName, password);
			try {
				stmt = myConn.createStatement();
				stmt.executeUpdate(request);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void ask(String request) throws ClassNotFoundException {

		try {

			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection("jdbc:mysql://" + server + db_name, userName, password);

			try {
				stmt = myConn.createStatement();
				rs = stmt.executeQuery(request);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private boolean exists(String request) throws ClassNotFoundException {

		try {

			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection("jdbc:mysql://" + server + db_name, userName, password);

			try {
				DatabaseMetaData dbm = myConn.getMetaData();
				ResultSet tables = dbm.getTables(null, null, request, null);
				if (tables.next()) {
					System.out.println("table "+request+" exists");
				  return true;
				}
				else {
					System.out.println("table "+request+"D OES NOT exists");
					return false;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;

	}
	private void update(String request) throws ClassNotFoundException {

		try {

			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection("jdbc:mysql://" + server + db_name, userName, password);

			try {
				stmt = myConn.createStatement();
				stmt.executeUpdate(request);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
//[int,int,'str','str']
	private boolean add(String table,String[] champs,ArrayList<String> request) throws ClassNotFoundException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection("jdbc:mysql://" + server + db_name, userName, password);
			statement = myConn.createStatement();

			String send = "";
			int len = request.size();
			for (int i = 0; i < len - 1; i++) {
				send += request.get(i) + ",";
			}
			send += request.get(len-1);
			String sendcommand="INSERT INTO "+table+" ("+String.join(",", champs).toUpperCase()+ ") VALUES (" + send + ")";
			System.out.println(sendcommand);
			statement.executeUpdate(sendcommand);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SQL error CANNOT ADD !");
			return false;

		}

	}

	private String read() throws IOException {
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		
		while ((stream = reader.read(b)) > 0) {
	
			response += new String(b, 0, stream);
			
			if (response.endsWith(EOF)) {
				break;
			}
			
			
		}
		
		return response.substring(0, response.length() - EOF.length());
	}
	public String format(ArrayList<String> liste) {
		String ret="";
		for (String s:liste) {
			ret+=s+sep;
		}
		if (ret.length() - sep.length()>=0)
			return ret.substring(0, ret.length() - sep.length());
		else
			return"";
	}

}