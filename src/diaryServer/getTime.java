package diaryServer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONWriter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.time.TimeTCPClient;

public class getTime extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logic(resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logic(resp);
	}

	@SuppressWarnings("deprecation")
	private void logic(HttpServletResponse response) {
		System.out.println("hi");
		/**
		 * 
		 
		//String[] hosts = new String[] { "ntp02.oal.ul.pt", "ntp04.oal.ul.pt",
			//	"ntp.xs4all.nl" };
				String[] hosts = new String[] { "ntp02.oal.ul.pt"};

		NTPUDPClient client = new NTPUDPClient();
		// We want to timeout if a response takes longer than 5 seconds
		client.setDefaultTimeout(5000);
		Date date;
		for (String host : hosts) {

			try {
				InetAddress hostAddr = InetAddress.getByName(host);
				System.out.println("> " + hostAddr.getHostName() + "/"
						+ hostAddr.getHostAddress());
				TimeInfo info = client.getTime(hostAddr);
				date = new Date(info.getReturnTime());
				System.out.println(date);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

		client.close();
		
		*/
		
		String TIME_SERVER = "time-a.nist.gov";   
		NTPUDPClient timeClient = new NTPUDPClient();
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(TIME_SERVER);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TimeInfo timeInfo = null;
		try {
			timeInfo = timeClient.getTime(inetAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
		Date time = new Date(returnTime);


		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.object();

		writer.key("time");
		writer.value(time.toGMTString());
		writer.endObject();
	}

	private String convertDate(String hour, String minute, String secs,
			String day, String month, String year) {
		StringBuilder sb = new StringBuilder();

		sb.append(day);
		sb.append(".");
		sb.append(month);
		sb.append(".");
		sb.append(year);
		sb.append(" ");
		sb.append(hour);
		sb.append(":");
		sb.append(minute);
		sb.append(" ");
		String res = sb.toString();
		return res;
	}
}
