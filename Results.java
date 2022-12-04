public class Results {
  public String schedulingType;
  public String schedulingName;
  public int compuTime;
  public int queuesCount;

  public Results (String schedulingType, String schedulingName, int compuTime, int queuesCount) {
    this.schedulingType = schedulingType;
    this.schedulingName = schedulingName;
    this.compuTime = compuTime;
    this.queuesCount = queuesCount;
  } 	
}
