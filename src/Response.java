import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

class Reponse {
	String CRLF = "\r\n";
	String BLANK = " ";
	byte[] content = null;
	StringBuilder responseText = null;
	int length = 0;
	String cookie = "";
	String ContentType = null;
	String contentPath = null;
	String type = null;
	String path=null;
	Socket client;
	Request request =null;
	
	public Reponse(Socket client,Request request) {
		this();
		this.client = client;
		
		this.request = request;
		
		try {
			BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.contentPath = "c://session"+request.resInfoDetail.get("path");

		setContentType();

		setContent();

	}

	public Reponse() {
		responseText = new StringBuilder();
		responseText.append("HTTP/1.1").append(BLANK).append("200").append(BLANK).append("OK").append(CRLF);
		responseText.append("Date:").append(new Date()).append(CRLF);
		responseText.append("Expires:").append(new Date()).append(CRLF);
		
	}

	public void setContent() {
		BufferedInputStream bis;
		byte[] b = new byte[102400];
		int len = 0;
		try {

			bis = new BufferedInputStream(new FileInputStream(new File(contentPath)));
			len = bis.read(b);
			this.content = b;
			this.length = len;
			responseText.append("Content-Length:").append(length).append(CRLF);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setContentType() {
		if (contentPath.endsWith("png"))
			this.ContentType = "Content-Type: image/png";
		if(contentPath.endsWith("html"))
			this.ContentType = "Content-Type: text/html; charset=UTF-8";
		else
			this.ContentType = "Content-Type: application/octet-stream";
		
		responseText.append(ContentType).append(CRLF);
	}
	
	public void addCookie(String key, String value) {
		this.cookie += "" + key + "=" + value;
		
	}

	public void setCookie() {
		responseText.append("Set-Cookie: ").append(cookie).append(CRLF);
	}

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
       

    	byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
        
    	System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
        return byte_3;  
    }
    
	public void send() {
		BufferedOutputStream bos=null;;
		try {
			bos = new BufferedOutputStream(client.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			setCookie();
			responseText.append(CRLF);
			
			System.out.println(responseText);
			
			byte[] byte_1 = responseText.toString().getBytes();
			byte[] byte_2 = content;
			
			byte[] Testcontent =byteMerger( byte_1,byte_2);
		
			bos.write(Testcontent, 0, Testcontent.length);
			
			bos.flush();
			
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}