package diaryServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import dataTypes.EventData;
import dataTypes.IndependenceStages;
import enums.EAssistantLevel;
import enums.EIndependenceStages;
import enums.ELogLevel;

/**
 * REQUEST STRUCTUR FOR NEW EVENT IS DONE WHEN ASKING FOR /newEvent (url page)
 * STRCUTRE: (key=value) "KidID" = "12345"(string) "comments" = "bla"(string)
 * "dateTime" = "11.11.11"(string) "isKaki" = True/False (boolean) "isPipi" =
 * True/false(boolean)
 * 
 * @author ilaisit
 *
 */
public class newEvent extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String FAILURE = "fail";
	final String KID_EVENTS = "data";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		parseRequest(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		parseRequest(req, resp);
	}

	private void parseRequest(HttpServletRequest request,
			HttpServletResponse response) {
		Logger.getInstance().Log(ELogLevel.debug, "newEvent", "parseRequest", "Trying to Create new event");
		String jsonReq = extractJsonFromRequest(request);
		JSONObject currentEventJson = new JSONObject(jsonReq);

		JSONWriter writer = null;
		try {
			writer = new JSONWriter(response.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// independence stages is not handled meanwhile! maybe next version
		EventData newEvent = new EventData();
		newEvent.setKidId(currentEventJson.getString("kidID"));
		newEvent.setComments(currentEventJson.getString("comments"));
		newEvent.setDateTime(currentEventJson.getString("dateTime"));
		newEvent.setSuccessResult(currentEventJson.getString("successResult"));
		newEvent.setInsertingUserId(currentEventJson
				.getString("insertingUserId"));
		newEvent.setKidIsInitiator(currentEventJson.getBoolean("kidIsInitiator"));
		JSONArray independenceJsonArray = currentEventJson.getJSONArray("createdIndependenceStages");
		List<IndependenceStages> independenceStagesList = new ArrayList<>();
		
		for (int i = 0; i < independenceJsonArray.length(); i++) {
		    JSONObject c = independenceJsonArray.getJSONObject(i);
		   
		    independenceStagesList.add(new IndependenceStages((EIndependenceStages)
		    		Enum.valueOf(EIndependenceStages.class, 
		    				c.getString("independenceStage")),
		    				(EAssistantLevel)Enum.valueOf(EAssistantLevel.class, 
				    				c.getString("assistantLevel"))));
		    
		}
		
//		independenceStages.add(new IndependenceStages(EIndependenceStages.cleanAss, EAssistantLevel.fullHelp));
//		independenceStages.add(new IndependenceStages(EIndependenceStages.doorClose, EAssistantLevel.noHelp));
		newEvent.setCreatedIndependenceStages(independenceStagesList);
		newEvent.setIsKaki(currentEventJson.getBoolean("isKaki"));
		newEvent.setIsPipi(currentEventJson.getBoolean("isPipi"));

		// temp is never used, we don't use the 'entry id'
		int temp = dbManager.getInstance().insertNewEvent(newEvent);
		Logger.getInstance().Log(ELogLevel.debug, "newEvent", "insertNewEvent", "Insertion Status from DB is: "+ temp);
		// pulls events from last 24 for the kid
		List<EventData> kidEvents = dbManager.getInstance().getEventsForKid(
				newEvent.getKidId(), 1);
		JSONObject outputList = new JSONObject();

		// write back the answer to ido (atifa in json)
		for (EventData eventData : kidEvents) {
			outputList.put("dateTime", eventData.getDateTime());
			outputList.put("insertingUserId", eventData.getInsertingUserId());
			outputList.put("kidId", eventData.getKidId());
			// *Note: meanwhile, no createdIndependenceStages are sent
			// outputList.put("createdIndependenceStages",
			// eventData.getCreatedIndependenceStages());
			outputList.put("kidIsInitiator", eventData.isKidIsInitiator());
			outputList.put("comments", eventData.getComments());
			outputList.put("isKaki", eventData.getIsKaki());
			outputList.put("isPipi", eventData.getIsPipi());
		}

		// the key is KID_EVENTS_LIST,
		// and the value is the list of the events of the kid
		writer.object();
		writer.key(KID_EVENTS);
		writer.value(outputList);
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
