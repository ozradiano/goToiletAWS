package dataTypes;
import java.util.ArrayList;
import java.util.List;

public class EventData {

	private String dateTime;
	private String insertingUserId;
	private String kidId;
	private List<IndependenceStages> createdIndependenceStages;
	private boolean kidIsInitiator;
	private String comments;
	private boolean isKaki, isPipi;
	private String successResult;

	public EventData() {
		createdIndependenceStages = new ArrayList<IndependenceStages>();
	}

	public EventData(String dateTime, String insertingUserId, String kidId, List<IndependenceStages> createdIndependenceStages, boolean kidIsInitiator,
			String comments, boolean isKaki, boolean isPipi, String successResult) {
		this.dateTime = dateTime;
		this.insertingUserId = insertingUserId;
		this.kidId = kidId;
		this.createdIndependenceStages = createdIndependenceStages;
		this.kidIsInitiator = kidIsInitiator;
		this.comments = comments;
		this.isKaki = isKaki;
		this.isPipi = isPipi;
		this.setSuccessResult(successResult);
	}

	@Override
	public String toString() {
		StringBuilder independeceStages = new StringBuilder();
		for (IndependenceStages s : createdIndependenceStages) {
			independeceStages.append(s.toString());
		}
		return String.format("date time: %s, inserting user: %s, kidId: %s, independence stages: %s, kid is the initiator: %s, comments: %s", dateTime,
				insertingUserId, kidId, independeceStages.toString(), kidIsInitiator, comments);
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getInsertingUserId() {
		return insertingUserId;
	}

	public void setInsertingUserId(String insertingUserId) {
		this.insertingUserId = insertingUserId;
	}

	public String getKidId() {
		return kidId;
	}

	public void setKidId(String kidId) {
		this.kidId = kidId;
	}

	public List<IndependenceStages> getCreatedIndependenceStages() {
		return createdIndependenceStages;
	}

	public void setCreatedIndependenceStages(List<IndependenceStages> createdIndependenceStages) {
		this.createdIndependenceStages = createdIndependenceStages;
	}

	public boolean isKidIsInitiator() {
		return kidIsInitiator;
	}

	public void setKidIsInitiator(boolean kidIsInitiator) {
		this.kidIsInitiator = kidIsInitiator;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean getIsPipi() {
		return isPipi;
	}

	public void setIsPipi(boolean isPipi) {
		this.isPipi = isPipi;
	}

	public boolean getIsKaki() {
		return isKaki;
	}

	public void setIsKaki(boolean isKaki) {
		this.isKaki = isKaki;
	}

	public String getCreatedIndependenceStagesSerialized() {
		StringBuilder retVal = new StringBuilder();

		for (IndependenceStages currentIndependenceStage : createdIndependenceStages) {
			retVal.append(String.format("%s,%s;", currentIndependenceStage.getIndependenceStage(), currentIndependenceStage.getAssistantLevel()));
		}

		return retVal.toString();
	}

	public String getSuccessResult() {
		return successResult;
	}

	public void setSuccessResult(String successResult) {
		this.successResult = successResult;
	}
}
