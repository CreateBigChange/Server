import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Request {
	String method = null;
	String HTTPType = null;
	String path = null;
	Map<String, String> resInfoDetail = new HashMap();
	Map<String, String> param = new HashMap();
	Map<String, String> paranCookie = new HashMap();
	String requestInfo = null;
	String[] temp = null;

	public Request(Socket client) {
		this();
		
		/**********************获取request内容**************/
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			int len = 0;
			char[] cbuf = new char[10240];
			len = br.read(cbuf);
			if(len==-1)
				return ;
			this.requestInfo = new String(cbuf, 0, len);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*************************************/
		
		temp = requestInfo.split("\r\n");
		
		String[] status;
		String[] otherInfo = new String[temp.length - 1];
		status = temp[0].split(" ");
		String[] tempSplit = null;
		
		for (int i = 0; i < temp.length - 1; i++)
			otherInfo[i] = temp[i + 1];

		for (int i = 0; i < otherInfo.length; i++) {
			tempSplit = otherInfo[i].split(": ");
			if (tempSplit.length >= 2) {
				resInfoDetail.put(tempSplit[0].toString().trim(), tempSplit[1].toString().trim());
			}
		}

		resInfoDetail.put("method", status[0].toString().trim());
		resInfoDetail.put("path", status[1].toString().trim());
		resInfoDetail.put("HTTPType", status[2].toString().trim());
		
		getParam(resInfoDetail.get("method"));
		getCookie(resInfoDetail.get("Cookie"));
		getPath(resInfoDetail.get("path"));
		
	/*
		  for (String key : resInfoDetail.keySet()) {
			   System.out.println(key + " :" + resInfoDetail.get(key));
		}
		*/
	}
	public void getPath(String pathStr){
		if(pathStr.indexOf("?")==-1){
			this.path=pathStr.substring(0, pathStr.length());
			return ;
		}
		this.path=pathStr.substring(0, pathStr.indexOf("?"));		
	}
	public void getCookie(String Cookie) {
		splitParam(Cookie, this.paranCookie, "; ");
	}
	public void getParam(String method) {
		String paramStr = null;
		if ("GET".equals(method)) {
			String str = resInfoDetail.get("path");
			if(str.indexOf("?")==-1)
				return ;
			if(str.indexOf("?")==str.length()-1)
				return ;
			paramStr = str.substring(str.indexOf("?") + 1, str.length());	
		}
		if ("POST".equals(method)) {
			paramStr = temp[temp.length - 1];
		}
		splitParam(paramStr, this.param, "&");
	}
	// 分离参数(请求参数和cookie)
	public void splitParam(String paramStr, Map param, String symbol) {
		if("".equals(paramStr)){
			param =null;
			return ;
		}
		if(paramStr==null)
			return ;
		String[] paramTemp = paramStr.split(symbol);
		
		for (int i = 0; i < paramTemp.length; i++) {
			param.put(paramTemp[i].split("=")[0], paramTemp[i].split("=")[1]);
		}
	}
	public Request() {
		requestInfo = new String();
	}
}