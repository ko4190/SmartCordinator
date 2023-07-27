package Final;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class test {
	public static void main(String[] args) {
		final int SERVER_PORT = 10000;

		ServerSocket server = null;

		int connectCount = 0;

		try {
			server = new ServerSocket(SERVER_PORT);

			System.out.println("****************************************");
			System.out.println("[server] binding! \n" + "port:" + SERVER_PORT);
			System.out.println("Thread를 이용한 다중 접속 서버 작동됨...");
			System.out.println("****************************************");

			while (true) {
				// 클라이언트가 접속해 오기를 기다립니다.
				Socket connectedClientSocket = server.accept();

				InetAddress ia = connectedClientSocket.getInetAddress();
				int port = connectedClientSocket.getLocalPort();// 접속에 사용된 서버측 PORT
				String ip = ia.getHostAddress(); // 접속된 원격 Client IP

				++connectCount; // 접속자수 카운트
				System.out.print(connectCount);
				System.out.print(" 접속-Local Port: " + port);
				System.out.println(" Client IP: " + ip);
				////////////////////////////////////////////////////

				// -------------------------------------------
				// 스레드 관련 부분
				// -------------------------------------------
				// Handler 클래스로 client 소켓 전송
				ThreadServerHandler handler = new ThreadServerHandler(connectedClientSocket);
				// 스레드 시작, run()호출
				handler.start(); // start() --> run() 호출
				// -------------------------------------------

			}
		} catch (IOException ioe) {
			System.err.println("Exception generated...");
		} finally {
			try {
				server.close();
			} catch (IOException ignored) {
			}
		}
	}
}

//클라이언트로 데이터를 전송할 스레드 클래스
class ThreadServerHandler extends Thread {

	// 멤버변수
	private Socket connectedClientSocket;

	// 생성자
	public ThreadServerHandler(Socket connectedClientSocket) {
		// Client와 통신할 객체를 초기값으로 받아 할당합니다.
		this.connectedClientSocket = connectedClientSocket;
	}

	// start() 메소드 호출 시 실행됩니다.
	public void run() {

		try {
			String nx_2 = "";
			String ny_2 = "";
			Database db = new Database();
			try {
				// 데이터베이스 연결
				Class.forName("com.mysql.cj.jdbc.Driver");
				db.con = DriverManager.getConnection(db.url, db.user, db.passwd);
				db.stmt = db.con.createStatement();

				try {
					// client data 수신
					BufferedReader in = new BufferedReader( new InputStreamReader(connectedClientSocket.getInputStream()));
					String Coordinates = in.readLine();
					
					// str = new String(str.getBytes("MS949"),"UTF-8");
					// System.out.println("Received: '" + Coordinates + "'");
					String[] xy = Coordinates.split("\\|");
					ny_2 = xy[0];
					nx_2 = xy[1];

					db.login(xy[2]);

					} catch (Exception e) {
					System.out.println("Error");
					e.printStackTrace();
					}

				// 클라이언트로 내용을 출력 할 객체 생성
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(connectedClientSocket.getOutputStream()));
				LocalDate now = LocalDate.now();
				//LocalTime now_2 = LocalTime.now();
				DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("yyyyMMdd");
				String formatedNow_1 = now.format(formatter_1);
				//DateTimeFormatter formatter_2 = DateTimeFormatter.ofPattern("HHmm");

				// 위경도 좌표 -> 그리드 좌표
				GpsTransfer transfer = new GpsTransfer();
				Map<String, String> dd2 = transfer.dfs_xy_conv("toXY", nx_2, ny_2);

				System.out.println("사용자의 위도 : " + dd2.get("lat"));
				System.out.println("사용자의 경도 : " + dd2.get("lng"));
				System.out.println("그리드 좌표로 변환한 X : " + dd2.get("x"));
				System.out.println("그리드 좌표로 변환한 Y : " + dd2.get("y"));

				String[] nxy = dd2.get("x").split("\\.");
				String nx = nxy[0];
				String[] nxy2 = dd2.get("y").split("\\.");
				String ny = nxy2[0];

				String baseDate = formatedNow_1; // 자신이 조회하고싶은 날짜를 입력해주세요
				String baseTime = "0500"; // 자신이 조회하고싶은 시간대를 입력해주세요
				// 서비스 인증키입니다. 공공데이터포털에서 제공해준 인증키를 넣어주시면 됩니다.
				String serviceKey = "SU4HdR%2F5jMBd3lAaBvV%2Bk6QewPgUmJTXNjxn3tsH6GVt%2F1hQT26elvtXUZVODLSX%2BwIGZYZlM95biX%2BgcRpGiQ%3D%3D";

				// 정보를 모아서 URL정보를 만들면됩니다. 맨 마지막 "&_type=json"에 따라 반환 데이터의 형태가 정해집니다.
				String urlStr = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?"
						+ "serviceKey=" + serviceKey + "&PageNo=1&numOfRows=302&dataType=json&base_date=" + baseDate
						+ "&base_time=" + baseTime + "&nx=" + nx + "&ny=" + ny;
				// 조회 기준 시간 -3시간 부터 302개 출력 하면 다음날까지 24시간 나옴
				URL url = new URL(urlStr); // 위 urlStr을 이용해서 URL 객체를 만들어줍니다.

				/* GET방식으로 전송해서 파라미터 받아오기 */
				// 어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
				System.out.println("기상청 오픈 api 링크 : " + url);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-type", "application/json");
				// System.out.println("Response code: " + conn.getResponseCode());
				BufferedReader rd;
				if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				} else {
					rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				}
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				conn.disconnect();
				String result = sb.toString();
				// System.out.println(result);
				result = new String(result.getBytes("MS949"), "UTF-8");
				// System.out.println("result : " + result);

				// Json parser를 만들어 만들어진 문자열 데이터를 객체화
				JSONParser parser = new JSONParser();
				JSONObject obj = null;
				try {
					obj = (JSONObject) parser.parse(result);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// response 키를 가지고 데이터를 파싱
				JSONObject parse_response = (JSONObject) obj.get("response");
				// response 로 부터 body 찾기
				JSONObject parse_body = (JSONObject) parse_response.get("body");
				// body 로 부터 items 찾기
				JSONObject parse_items = (JSONObject) parse_body.get("items");

				// items로 부터 itemlist 를 받기
				JSONArray parse_item = (JSONArray) parse_items.get("item");
				String category;
				JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용
				// 카테고리와 값만 받아오기
				String day = "";
				String time = "";
				String information = "";

				information += db.viewData(nx, ny);

				for (int i = 0; i < 302; i++) {
					weather = (JSONObject) parse_item.get(i);
					Object fcstValue = weather.get("fcstValue");
					Object fcstDate = weather.get("fcstDate");
					Object fcstTime = weather.get("fcstTime");

					// double형으로 받고싶으면 아래내용 주석 해제
					// double fcstValue = Double.parseDouble(weather.get("fcstValue").toString());
					category = (String) weather.get("category");
					// 출력
					if (!day.equals(fcstDate.toString())) {
						day = fcstDate.toString();
					}
					if (!time.equals(fcstTime.toString())) {
						time = fcstTime.toString();
						information += "|" + day + " " + time;
					}
					if (category.equals("TMP")) {
						information += "^온도 : " + fcstValue + " 도";
					}
					if (category.equals("WSD")) {
						information += "^풍속 : " + fcstValue + " m/s";
					}
					if (category.equals("SKY")) {
						if (fcstValue.equals("1")) {
							information += "^하늘 상태 : 맑음";
						}
						if (fcstValue.equals("3")) {
							information += "^하늘 상태 : 구름 많음";
						}
						if (fcstValue.equals("4")) {
							information += "^하늘 상태 : 흐림";
						}
					}
					if (category.equals("PTY")) {
						if (fcstValue.equals("0")) {
							information += "^강수 형태 : 없음";
						}
						if (fcstValue.equals("1")) {
							information += "^강수 형태 : 비";
						}
						if (fcstValue.equals("2")) {
							information += "^강수 형태 : 비/눈";
						}
						if (fcstValue.equals("3")) {
							information += "^강수 형태 : 눈";
						}
						if (fcstValue.equals("4")) {
							information += "^강수 형태 : 소나기";
						}
					}
					if (category.equals("POP")) {
						information += "^강수 확률 : " + fcstValue + "%";
					}
					if (category.equals("REH")) {
						information += "^습도 : " + fcstValue + "%";
					}
				}

				PrintWriter out = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(connectedClientSocket.getOutputStream(), "utf-8")), true);
				System.out.println(information);
				out.println(information);

				/////////////////////////////////////////////////////////////////////////////////////////////
				while (true) {
					String[] select = null;
					String selection = null;

					try {
						// client data 수신
						BufferedReader in = new BufferedReader(
								new InputStreamReader(connectedClientSocket.getInputStream()));
						selection = in.readLine();

						if (selection == null)
							continue;
						//System.out.println("Received: '" + selection + "'");
						select = selection.split("\\|");
					} catch (Exception e) {
						System.out.println("Error");
						e.printStackTrace();
					}
					System.out.println(select[0]);
					
					switch (select[0]) {
					case "옷장":
						String closet_information;
						closet_information = db.Closet(select[1], select[2]);

						PrintWriter out_2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connectedClientSocket.getOutputStream(), "utf-8")), true);
						out_2.println(closet_information);
						break;

					case "추천":			
						String recommend;
						recommend = db.Recommendation(select[1],select[2]);
						
						PrintWriter out_3 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connectedClientSocket.getOutputStream(), "utf-8")), true);
						out_3.println(recommend);
						break;

					case "등록":
						db.Registration(select[1]);
						break;
					
					case "피드백":
						db.feedback(select[1],select[2]);
						break;
						
					case "삭제":
						db.delete(select[1], select[2]);
						break;
						
					case "종료":
						try {
							connectedClientSocket.close(); // 클라이언트 접속 종료
						} catch (IOException ignored) {
						}
					}
				}
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
		} finally {
			try {
				connectedClientSocket.close(); // 클라이언트 접속 종료
			} catch (IOException ignored) {
			}
		}
	}
}

/*
 * 코드 구현해야할 것 
 * - 사용자 IP를 이용해서 위치 탐색 후 좌표 설정 o 
 * - 데이터 베이스 연동해서 위치 정보 저장 o 
 * - 최대 최소 온도, 일교차, 날씨 뽑아내기 o 
 * - 기온별 옷차림 추천 알고리즘 구현 o
 * - 클라이언트의가 옷차림 선택 하면 데이터베이스에 저장된 사용자의 옷 정보 전송 o 
 * - 여유 되면 색 조합까지
 */

// 옷장 비어있으면 팅김. (옷장에서 넘긴 데이터 중에 비트맵을 그림으로 변환하는 과정에서 비트맵이 비어있어서 팅기는거같음 클라이언트에서 예외처리 해야할듯)
// 추천받기 누르면 서버에서는 그 날씨에 맞는 옷 데이터를 전부 다 보내니 랜덤으로 추천 시키려면 클라이언트에서 데이터 빼낸것 중에 랜덤으로 뽑아내서 데이터 넣어줘야 할 듯
// 다른지역에서 실행해봤는데 위치값이 계속 똑같은거 보니깐 지금 고정 gps 주소 넘어오는듯
// 추천 창에 왼쪽 부분에는 랜덤 상 하의 보여주고 오른쪽에는 별명으로 상 하의 목록 보여주고 목록에서 클릭하면 왼쪽 추천을 클릭한걸로 바꿔주는 방식으로 코드 짜는거 쫌 좋아보임 