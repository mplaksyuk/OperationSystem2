// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

public class SchedulingAlgorithm {

  private static int comptime = 0;

  private static int quantTime = 20; // ms
  private static int quanttimeResidue = 0;

  private static ArrayList<Queue<Integer>> PriorityQueues = new ArrayList<>();

  private static int queueScheduling(PrintStream out, int runtime, int quanttime, ArrayList<Process> processVector, int queueIndex) {

    int processIndex = PriorityQueues.get(queueIndex).poll();

    Process process = (Process) processVector.get(processIndex);
    
    out.println("Process: " + processIndex + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");

    int i = 0;
    for (i = process.cpudone % process.ioblocking; 

        comptime < runtime &&
        process.cpudone != process.cputime &&
        i < process.ioblocking &&
        quanttime != 0;

        i++, quanttime--, process.cpudone++, comptime++ );
    
    if (process.cpudone != process.cputime) {
      if (quanttime > 0)
        PriorityQueues.get(Math.min(queueIndex + 1, PriorityQueues.size() - 1)).add(processIndex);
      
      else
        PriorityQueues.get(queueIndex).add(processIndex);
    }
      
    else
      out.println("Process: " + processIndex + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");

    if (i == process.ioblocking) {
      process.numblocked++;
      out.println("Process: " + processIndex + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
    }
    if (comptime == runtime)
      out.println("\n TIMEOUT \n");

    return quanttime;
  }

  private static Boolean queuesIsEmpty() {
    return PriorityQueues.get(0).peek() == null && 
           PriorityQueues.get(1).peek() == null &&
           PriorityQueues.get(2).peek() == null &&
           PriorityQueues.get(3).peek() == null;
  }

  public static void Run(int runtime, ArrayList<Process> processVector, Results result, int queuesCount) {

    String resultsFile = "Summary-Processes";

    result.schedulingType = "Batch (Nonpreemptive)";
    result.schedulingName = "Multiple Queues";
    result.queuesCount = queuesCount;

    for (int i = 0; i < queuesCount; ++i) {
      PriorityQueues.add(new LinkedList<Integer>());
    }

    for(int processNum = 0; processNum < processVector.size() ; processNum++ ) {
      Process process = (Process) processVector.get(processNum);
        int index = (process.priority + (process.priority % 2 * 2)) % queuesCount;
        
        PriorityQueues.get(index).add(processNum);
    }

    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

      
      for (int i = 0; comptime < runtime && !queuesIsEmpty(); ) {
        quanttimeResidue = queueScheduling(out, runtime, quanttimeResidue != 0 ? quanttimeResidue : (int) Math.pow(2, i) * quantTime, processVector, i);
        if (PriorityQueues.get(i).peek() == null) {
          i++;
        }
      }
      
      out.close();

    } catch (Exception e) {
      System.out.println(e);
    }

    result.compuTime = comptime;
  }
}
