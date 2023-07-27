package Final;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
	Connection con = null;
	Statement stmt = null;
	String url = "jdbc:mysql://localhost:3306/mydb";
	String user = "root";
	String passwd = "1234"; // MySQL에 저장한 root 계정의 비밀번호를 적어주면 된다.

	public static void main(String[] args) {
		Database db = new Database();

		/* 데이터베이스 관련 코드는 try-catch문으로 예외 처리를 꼭 해주어야 한다. */
		try {
			// 데이터베이스 연결
			Class.forName("com.mysql.cj.jdbc.Driver");
			db.con = DriverManager.getConnection(db.url, db.user, db.passwd);
			db.stmt = db.con.createStatement();

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				db.stmt.close();
				db.con.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	
	void login(String email)
	{
		String[] mail=email.split("\\^");
		
		String mail_2=null;
		try
		{
			String viewStr1 = "SELECT * FROM mydb.user where ID = '" + mail[0]+"';";
			System.out.println(viewStr1);
			ResultSet result1 = stmt.executeQuery(viewStr1);
			while (result1.next()) {
				mail_2 = result1.getString("ID");
			}
			System.out.println("접속 ID : "+ mail_2);
			if (mail_2 == null)
			{
				try {
					String insertStr = "INSERT INTO mydb.user VALUES('" + mail[0] + "','" + mail[1] + "','" + mail[2] + "',+'0');";
					System.out.println(insertStr);
					stmt.executeUpdate(insertStr);
					System.out.println("데이터 추가 성공!");
				} catch (Exception e) {
					System.out.println("데이터 추가 실패 이유 : " + e.toString());
				}
			}
			
		} catch (Exception e) {
			System.out.println("데이터 추가 실패 이유 : " + e.toString());
		}
	}
	// 등록
	void Registration(String cloth) {
		String[] cloth_2 = cloth.split("\\^");

		try {
			String insertStr = "INSERT INTO mydb.cloth VALUES('" + cloth_2[0] + "','" + cloth_2[1] + "','" + cloth_2[2] + "','" + cloth_2[3] + "','" + cloth_2[4] + "');";		
			stmt.executeUpdate(insertStr);
			System.out.println("데이터 추가 성공!");
		} catch (Exception e) {
			System.out.println("데이터 추가 실패 이유 : " + e.toString());
		}
	}

	void delete(String ID, String clothName) {
		try {
			String removeStr = "DELETE FROM mydb.cloth where uID = '"+ID+"' AND clothName = '"+clothName+"';";
			stmt.executeUpdate(removeStr);
			System.out.println("데이터 삭제 성공!");
		} catch (Exception e) {
			System.out.println("데이터 삭제 실패 이유 : " + e.toString());
		}
	}

	/*
	void changeData() {
		try {
			String changeStr = "UPDATE user SET name='가나다'";
			stmt.executeUpdate(changeStr);
			System.out.println("데이터 변경 성공!");
		} catch (Exception e) {
			System.out.println("데이터 변경 실패 이유 : " + e.toString());
		}
	}*/

	// 조회
	public String viewData(String nx, String ny) {
		String location = "";
		try {
			String viewStr1 = "SELECT * FROM mydb.location WHERE (Grid_X = " + nx + " AND Grid_Y = " + ny + ");";
			// System.out.println(viewStr1);
			ResultSet result1 = stmt.executeQuery(viewStr1);
			int cnt1 = 0;
			/*
			 * while(result1.next()) { 
			 * System.out.print(result1.getString("Step_1") + " " + result1.getString("Step_2") + " " + result1.getString("Step_3") + " ");
			 */
			while (result1.next()) {
				location = result1.getString("Step_1") + " " + result1.getString("Step_2") + " "
						+ result1.getString("Step_3");
				cnt1++;
			}
		} catch (Exception e) {
			System.out.println("데이터 조회 실패 이유 : " + e.toString());
		}
		return location;
	}

	//옷장
	public String Closet(String x,String ID) 
	{
		String y="", result="";
		try {
			String viewStr1=null;
			
			viewStr1 = "SELECT * FROM mydb.cloth WHERE uID = '"+ID+"' AND "+ "typeBig = '"+ x +"';";
		
			System.out.println(viewStr1);
			ResultSet result1 = stmt.executeQuery(viewStr1);
			int cnt1 = 0;
			/*while(result1.next()) { 
				  System.out.print(result1.getString("uID") + "|" + result1.getString("bi	tmap") + "|" + result1.getString("clothName") +result1.getString("typeBig")+"|"+ result1.getString("typeSmall")+"\n");
			}*/
			while (result1.next()) {
				y = result1.getString("bitmap") + "|" + result1.getString("clothName") + "|"+ result1.getString("typeBig")+"|"+ result1.getString("typeSmall")+"^";				
				result+=y;	
			}	 
		} catch (Exception e) {
			System.out.println("데이터 조회 실패 이유 : " + e.toString());
		}				
		return result;
	}
	
	void feedback(String ID, String result)
	{
		System.out.println(result);
		try {
			String changeStr = null;
			if (result.equals("cold"))
				changeStr = "UPDATE mydb.user SET feedback = feedback + '1' where ID = '"+ID+"';";
			else if (result.equals("hot"))
				changeStr = "UPDATE mydb.user SET feedback = feedback + '-1' where ID = '"+ID+"';";
				
			stmt.executeUpdate(changeStr);	
			System.out.println("데이터 변경 성공!");
		} catch (Exception e) {
			System.out.println("데이터 변경 실패 이유 : " + e.toString());
		}
	}
	
	public String Recommendation(String temperature, String ID) 
	{
		int temper = Integer.parseInt(temperature);
		int level=0;
		int fb;
		if (temper >= 28 ) {level = 1;}	
		else if (temper >=23 && temper <=27) {level=2;}
		else if (temper >=20 && temper <=22) {level=3;}
		else if (temper >=17 && temper <=19) {level=4;}
		else if (temper >=12 && temper <=16) {level=5;}
		else if (temper >=9 && temper <=11) {level=6;}
		else if (temper >=5 && temper <=8) {level=7;}
		else {level = 8;}
			
		
		try {
		String viewStr1 = "SELECT * FROM mydb.user where ID = '" +ID+"';";
		
		ResultSet result1 = stmt.executeQuery(viewStr1);
		while (result1.next()) {
			fb = result1.getInt("feedback");	
			level += fb;
		//int feedback = Integer.parseInt(fb) + 1;
			}
		}catch (Exception e) {
			System.out.println("데이터 조회 실패 이유 : " + e.toString());
		}
		
		if (level<1)
			level=1;
		else if (level>8)
			level=8;
		
		System.out.println("레벨:"+level);
		String result="";
		try {
			String viewStr1=null, y=null;
						
		
				switch (level) {
				case 1:
					viewStr1 = "SELECT * FROM mydb.cloth WHERE uID = '"+ID+"' AND (typeBig = '상의' AND (typeSmall = '반팔' OR typeSmall = '민소매' OR typeSmall = '린넨옷')) OR (typeBig = '하의' AND (typeSmall ='반바지'or typeSmall='짧은치마'));";
					ResultSet result1 = stmt.executeQuery(viewStr1);
					while (result1.next()) {
						y = result1.getString("bitmap") + "|" + result1.getString("clothName") + "|"+ result1.getString("typeBig")+"|"+ result1.getString("typeSmall")+"^";				
						result+=y;}		
					break;
				
				case 2:
					viewStr1 = "SELECT * FROM mydb.cloth WHERE uID = '"+ID+"' AND (typeBig = '상의' AND (typeSmall = '반팔' OR typeSmall = '얇은셔츠')) OR (typeBig = '하의' AND (typeSmall ='반바지'or typeSmall='면바지'));";
					ResultSet result2 = stmt.executeQuery(viewStr1);
					while (result2.next()) {
						y = result2.getString("bitmap") + "|" + result2.getString("clothName") + "|"+ result2.getString("typeBig")+"|"+ result2.getString("typeSmall")+"^";				
						result+=y;}		
					break;
					
				case 3:
					viewStr1 = "SELECT * FROM mydb.cloth WHERE uID = '"+ID+"' AND (typeBig = '상의' AND (typeSmall = '블라우스' OR typeSmall = '긴팔티'OR typeSmall = '셔츠' )) OR (typeBig = '하의' AND (typeSmall ='면바지'or typeSmall='슬랙스'or typeSmall='긴바지'));";
					ResultSet result3 = stmt.executeQuery(viewStr1);
					while (result3.next()) {
						y = result3.getString("bitmap") + "|" + result3.getString("clothName") + "|"+ result3.getString("typeBig")+"|"+ result3.getString("typeSmall")+"^";				
						result+=y;}		
					break;
					
				case 4:
					viewStr1 = "SELECT * FROM mydb.cloth WHERE uID = '"+ID+"' AND (typeBig = '상의' AND (typeSmall = '니트' OR typeSmall = '맨투맨' OR typeSmall = '후드' OR typeSmall = '셔츠' )) OR (typeBig = '하의' AND (typeSmall ='면바지'or typeSmall='슬랙스'or typeSmall='긴바지')) OR (typeBig = '아우터' AND (typeSmall = '얇은가디건'))";
					ResultSet result4 = stmt.executeQuery(viewStr1);
					while (result4.next()) {
						y = result4.getString("bitmap") + "|" + result4.getString("clothName") + "|"+ result4.getString("typeBig")+"|"+ result4.getString("typeSmall")+"^";				
						result+=y;}		
					break;
					
				case 5:
					viewStr1 = "SELECT * FROM mydb.cloth WHERE uID = '"+ID+"' AND (typeBig = '상의' AND (typeSmall = '니트' OR typeSmall = '맨투맨' OR typeSmall = '후드' OR typeSmall = '셔츠' )) OR (typeBig = '하의' AND (typeSmall ='면바지'or typeSmall='슬랙스'or typeSmall='긴바지' or typeSmall ='청바지')) OR (typeBig = '아우터' AND (typeSmall = '가디건' or typeSmall = '자켓'))";		
					ResultSet result5 = stmt.executeQuery(viewStr1);
					while (result5.next()) {
						y = result5.getString("bitmap") + "|" + result5.getString("clothName") + "|"+ result5.getString("typeBig")+"|"+ result5.getString("typeSmall")+"^";				
						result+=y;}		
					
					break;
					
				case 6:
					viewStr1 = "SELECT * FROM mydb.cloth WHERE uID = '"+ID+"' AND (typeBig = '상의' AND (typeSmall = '니트' OR typeSmall = '맨투맨' OR typeSmall = '후드'OR typeSmall = '셔츠' )) OR (typeBig = '하의' AND (typeSmall ='면바지'or typeSmall='슬랙스'or typeSmall='긴바지' or typeSmall ='청바지'or typeSmall ='기모바지')) OR (typeBig = '아우터' AND (typeSmall = '트렌치코트' or typeSmall = '야상'or typeSmall = '점퍼'))";
					ResultSet result6 = stmt.executeQuery(viewStr1);
					while (result6.next()) {
						y = result6.getString("bitmap") + "|" + result6.getString("clothName") + "|"+ result6.getString("typeBig")+"|"+ result6.getString("typeSmall")+"^";				
						result+=y;}		
					break;
					
				case 7:
					viewStr1 = "SELECT * FROM mydb.cloth WHERE uID = '"+ID+"' AND (typeBig = '상의' AND (typeSmall = '니트' OR typeSmall = '맨투맨' OR typeSmall = '후드'OR typeSmall = '셔츠' )) OR (typeBig = '하의' AND (typeSmall ='면바지'or typeSmall='슬랙스'or typeSmall='긴바지' or typeSmall ='청바지'or typeSmall ='기모바지')) OR (typeBig = '아우터' AND (typeSmall = '울코트' or typeSmall = '가죽옷'))";
					ResultSet result7 = stmt.executeQuery(viewStr1);
					while (result7.next()) {
						y = result7.getString("bitmap") + "|" + result7.getString("clothName") + "|"+ result7.getString("typeBig")+"|"+ result7.getString("typeSmall")+"^";				
						result+=y;}	
					break;
					
				case 8:
					viewStr1 = "SELECT * FROM mydb.cloth WHERE uID = '"+ID+"' AND (typeBig = '상의' AND (typeSmall = '니트' OR typeSmall = '맨투맨' OR typeSmall = '후드'OR typeSmall = '셔츠' )) OR (typeBig = '하의' AND (typeSmall ='면바지'or typeSmall='슬랙스'or typeSmall='긴바지' or typeSmall ='청바지'or typeSmall ='기모바지')) OR (typeBig = '아우터' AND (typeSmall = '두꺼운코트' or typeSmall = '패딩' or typeSmall = '누빔'))";
					ResultSet result8 = stmt.executeQuery(viewStr1);
					while (result8.next()) {
						y = result8.getString("bitmap") + "|" + result8.getString("clothName") + "|"+ result8.getString("typeBig")+"|"+ result8.getString("typeSmall")+"^";				
						result+=y;}	
					break;
				}
			
			}catch (Exception e) {
				System.out.println("데이터 조회 실패 이유 : " + e.toString());
		}
		
		return result;
	}
}