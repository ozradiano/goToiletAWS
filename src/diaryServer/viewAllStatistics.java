package diaryServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import dataTypes.Statistics;
import enums.EStatisticType;

public class viewAllStatistics extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String FAILURE = "fail";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		parseRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		parseRequest(req, resp);
	}
	
	private void parseRequest(HttpServletRequest request,
			HttpServletResponse response) {
		
		System.out.println("Trying to view ALL statistics");
		String jsonReq = extractJsonFromRequest(request);
		JSONObject currentStat = new JSONObject(jsonReq);

		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.object();

		// first value in json is successes
		String kidId = currentStat.getString("kidId");
		int days = currentStat.getInt("daysFromToday");
		List<Statistics> statList = dbManager.getInstance()
				.getStatisticsForKidId(kidId,
						EStatisticType.successes, days);

		JSONObject responseDetailsJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < statList.size(); i++) {
			JSONObject formDetailsJson = new JSONObject();
			formDetailsJson.put("date", statList.get(i).getDate());
			formDetailsJson.put("value", statList.get(i).getValue());
			jsonArray.put(formDetailsJson);

		}
		responseDetailsJson.put("arrayValues", jsonArray);
		writer.key("successes");
		writer.value(responseDetailsJson);
		
		// now, we handle "takes" statistics type
		List<Statistics> statList2 = dbManager.getInstance()
				.getStatisticsForKidId(kidId,
						EStatisticType.takes, days);

		JSONObject responseDetailsJson2 = new JSONObject();
		JSONArray jsonArray2 = new JSONArray();

		for (int i = 0; i < statList2.size(); i++) {
			JSONObject formDetailsJson2 = new JSONObject();
			formDetailsJson2.put("date", statList2.get(i).getDate());
			formDetailsJson2.put("value", statList2.get(i).getValue());
			jsonArray2.put(formDetailsJson2);

		}
		responseDetailsJson2.put("arrayValues", jsonArray2);
		writer.key("takes");
		writer.value(responseDetailsJson2);
		
		writer.endObject();
		
	}
	
	private String extractJsonFromRequest(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = request.getReader();
		} catch (IOException e) {
			System.out.println("Hi! could not extract json from request");
			e.printStackTrace();
			return FAILURE;

		}
		String str;
		try {
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		} catch (IOException e) {
			System.out.println("could not read br - could not extract json");
			e.printStackTrace();
			return FAILURE;
		}
		return sb.toString();
	}

}
