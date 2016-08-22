import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Session implements Serializable {
	static String path="c://session/";
	String sessionId;
	Map data = new HashMap();

	public Session() {	
		 
		long temp = System.currentTimeMillis();
		sessionId = String.valueOf(temp);
		
	}
	
	// 通过序列化实现session
	public void save(String key, String value) {
		data.put(key, value);
	}
	
	public  static Session getSession(String id) {
		Session MySession = null;
		if(id==null)
			return null;
			ObjectInputStream oi=null;
			try {
				System.out.println(path + "/" + id);
				oi 	= new ObjectInputStream(new FileInputStream(new File(path + "/" + id)));
				MySession            = (Session)oi.readObject();
				
			} catch (FileNotFoundException e) {
				System.out.println(path + "/" + id);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return MySession;
	};
	public String getValue(String name){
		
		if(data.containsKey(name))
			return (String) data.get(name);
		else
			return null;
		
	}
	public void sendSessionId(Reponse response) {
		//序列化对象
		
		try {
			ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(path + "/" + sessionId)));
			oo.writeObject(this);
			oo.flush();
			oo.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.addCookie("SessionId", sessionId);
		
	};
}
