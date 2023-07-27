package Final;

import java.util.HashMap;
import java.util.Map;

public class GpsTransfer {

	static double RE = 6371.00877; // 지구 반경(km)
	static double GRID = 5.0; // 격자 간격(km)
	static double SLAT1 = 30.0; // 투영 위도1(degree)
	static double SLAT2 = 60.0; // 투영 위도2(degree)
	static double OLON = 126.0; // 기준점 경도(degree)
	static double OLAT = 38.0; // 기준점 위도(degree)
	static double XO = 43; // 기준점 X좌표(GRID)
	static double YO = 136; // 기1준점 Y좌표(GRID)

	public static void main(String args[]) {
		// 그리드 좌표 -> 위경도
		Map<String, String> dd = dfs_xy_conv("toLL", "60", "127");
		System.out.println(dd.get("x"));
		System.out.println(dd.get("y"));
		System.out.println(dd.get("lat"));
		System.out.println(dd.get("lng"));
		// 위경도 좌표 -> 그리드 좌표
		Map<String, String> dd2 = dfs_xy_conv("toXY", "35.91374479444874", "128.82146781716762");
		System.out.println(dd2.get("x"));
		System.out.println(dd2.get("y"));
		System.out.println(dd2.get("lat"));
		System.out.println(dd2.get("lng"));

	}

	public static Map<String, String> dfs_xy_conv(String mode, String sv1, String sv2) {
		double v1 = Double.parseDouble(sv1);
		double v2 = Double.parseDouble(sv2);
		double DEGRAD = Math.PI / 180.0;
		double RADDEG = 180.0 / Math.PI;

		double re = RE / GRID;
		double slat1 = SLAT1 * DEGRAD;
		double slat2 = SLAT2 * DEGRAD;
		double olon = OLON * DEGRAD;
		double olat = OLAT * DEGRAD;

		double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
		sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
		double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
		sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
		double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
		ro = re * sf / Math.pow(ro, sn);
		Map<String, String> rs = new HashMap<String, String>();
		if (mode == "toXY") {
			rs.put("lat", v1 + "");
			rs.put("lng", v2 + "");
			double ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5);
			ra = re * sf / Math.pow(ra, sn);
			double theta = v2 * DEGRAD - olon;
			if (theta > Math.PI)
				theta -= 2.0 * Math.PI;
			if (theta < -Math.PI)
				theta += 2.0 * Math.PI;
			theta *= sn;
			double x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
			double y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
			rs.put("x", x + "");
			rs.put("y", y + "");
		} else {
			double theta = 0.0;
			rs.put("x", v1 + "");
			rs.put("y", v2 + "");
			double xn = v1 - XO;
			double yn = ro - v2 + YO;
			double ra = Math.sqrt(xn * xn + yn * yn);
			// if (sn < 0.0) - ra;
			ra = Math.abs(ra);
			double alat = Math.pow((re * sf / ra), (1.0 / sn));
			alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

			if (Math.abs(xn) <= 0.0) {
				theta = 0.0;
			} else {
				if (Math.abs(yn) <= 0.0) {
					theta = Math.PI * 0.5;
					// if (xn < 0.0) - theta;
					theta = Math.abs(theta);
				} else
					theta = Math.atan2(xn, yn);
			}
			double alon = theta / sn + olon;
			double lat = alat * RADDEG;
			double lng = alon * RADDEG;
			rs.put("lat", lat + "");
			rs.put("lng", lng + "");
		}
		return rs;
	}
}
