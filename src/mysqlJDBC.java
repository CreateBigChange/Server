import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mysqlJDBC {
	  Connection conn = null;
      String sql;
      String url = "jdbc:mysql://127.0.0.1:3306/laravel?"+ "user=root&password=123456&useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC";
      Statement stmt=null;
      public mysqlJDBC(String table) {
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			sql = "select * from "+table;
		
			this.conn = DriverManager.getConnection(url);
			
			this.stmt = conn.createStatement();
			
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}// 动态加载mysql驱动
		catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public ArrayList<HashMap> getHash(){
		
		//Map的队列
		ArrayList<HashMap> list = new ArrayList<HashMap>(); 
		
		//临时的myMap
		HashMap myMap=null;
		
	     try {
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData data = rs.getMetaData();

		
			//字段总数目
			int num=data.getColumnCount();
			
			String [] name=new String[num];
			
			//获取相关的列名
			
			for(int i =1;i<=data.getColumnCount();i++){
				System.out.println(i+"---------"+data.getColumnName(i));;
			}
			
			while(rs.next()){
				myMap = new HashMap();
				for(int i=1;i<=num;i++)
				{
					//System.out.println(myMap.put(name[i], rs.getString(i)));
				}
				list.add(myMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}// executeQuery会返回结果的集合，否则返回空值
	     return list;
	}
}
