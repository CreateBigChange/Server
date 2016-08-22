import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServerDemo01 {
	public static int sign=0;
	public static void main(String args[]) throws IOException {
		//服务器
		ServerSocket server = new ServerSocket(8999);

		while(true){
			
			//接受客户端
			Socket socket = server.accept();
			sign++;
			System.out.println(sign);
			new Thread(new SocketThread(socket)).start();
			
		}
	}
}