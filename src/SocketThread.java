import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SocketThread implements Runnable{
	public Socket client = null;
	public Session mysession=null;
	public String sessionID=null;
	Request request=null;
	public SocketThread(Socket socket) {
		this.client = socket;
		 /**********************��װrequest �ͳ�ʼ��sesion********************************************/
			 request = new Request(client);
			//��ȡsessioID
			if(request.paranCookie.containsKey("SessionId")){
				this.sessionID= request.paranCookie.get("SessionId");
				System.out.println(this.sessionID);
				mysession=Session.getSession(sessionID);
			}
			else
			{
				mysession = new Session();
			}
		/******************************************************************/
	}
	
	@Override
	public void run() {
		
			//��װresponse
			Reponse res = new Reponse(client,this.request);
			//����session
			mysqlJDBC mysql =new mysqlJDBC("articles"); 
			
			
			ArrayList<HashMap> mysqlMaps =mysql.getHash();
			HashMap mysqlMap =null;
			for(int i =0;i<mysqlMaps.size();i++){
				mysqlMap = mysqlMaps.get(i);
				
			for (Object key : mysqlMap.keySet().toArray(null)) {
					System.out.println("�ֶ�ֵΪ:");   
					System.out.println("key= "+ key.toString() + " and value= " + mysqlMap.get(key).toString());
				}
			}
			
			System.out.println("the userId is ================"+mysession.getValue("userId"));
			
			mysession.save("userId", "123456");
				
			//����session
			mysession.sendSessionId(res);
			res.send();
	}
}
