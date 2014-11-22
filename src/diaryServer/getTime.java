package diaryServer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONWriter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

public class getTime extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logic(resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logic(resp);
	}

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
			e.printStackTrace();
		}
		TimeInfo timeInfo = null;
		try {
			timeInfo = timeClient.getTime(inetAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
		Date dateTime = new Date(returnTime);

		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.object();

		SimpleDateFormat newTimeFormat = new SimpleDateFormat("HH:MM");
		SimpleDateFormat newDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		writer.key("date");
		writer.value(newDateFormat.format(dateTime));
		writer.key("hour");
		writer.value(newTimeFormat.format(dateTime));
		writer.endObject();
	}
}
