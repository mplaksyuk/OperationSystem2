// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

import java.io.*;
import java.util.*;

public class Scheduling {

  private static int processnum = 5;
  private static int maxqueues = 0;
  private static int meanDev = 1000;
  private static int standardDev = 100;
  private static int runtime = 1000;
  private static ArrayList<Process> processVector = new ArrayList<>();
  private static Results result = new Results("null","null",0, 0);
  private static String resultsFile = "Summary-Results";

  private static void Init(String file) {
    File f = new File(file);
    String line;
    int cputime = 0;
    int ioblocking = 0;
    int priority = 0;
    double X = 0.0;

    try {   
      //BufferedReader in = new BufferedReader(new FileReader(f));
      DataInputStream in = new DataInputStream(new FileInputStream(f));

      while ((line = in.readLine()) != null) {

        line = line.trim();

        String[] words = line.split(" ");

        switch(words[0]) {
          case "maxqueues":
            maxqueues = Common.s2i(words[1]);
          case "numprocess":
            processnum = Common.s2i(words[1]);
            break;
          case "meandev":
            meanDev = Common.s2i(words[1]);
            break;
          case "standdev":
            standardDev = Common.s2i(words[1]);
            break;
          case "runtime":
            runtime = Common.s2i(words[1]);
            break;
          case "process":
            ioblocking = Common.s2i(words[1]);

            priority = Common.s2i(words[3]);

            X = Common.R1() * standardDev;
            cputime = (int) X + meanDev;

            processVector.add(new Process(priority, cputime, ioblocking, 0, 0, 0));  
            break;
        }
      }

      in.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private static void debug() {
    

    System.out.println("processnum " + processnum);
    System.out.println("meandevm " + meanDev);
    System.out.println("standdev " + standardDev);

    int i = 0;
    for (Process process : processVector) {
      System.out.println("process " + i++ + " " + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.numblocked);
    }
    System.out.println("runtime " + runtime);
  }

  private static void WriteSummaryResults(Results result) {
    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

      out.println("Scheduling Type: "     + result.schedulingType);
      out.println("Scheduling Name: "     + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Mean: "                + meanDev);
      out.println("Standard Deviation: "  + standardDev);
      out.println("Queues count: "        + result.queuesCount);

      out.println("Process #\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked");
      
      int i = 0;
      for (Process process : processVector) {
        out.printf(" %s %10s(ms) %10s(ms) %10s(ms) %8s times\n", i++, process.cputime, process.ioblocking, process.cpudone, process.numblocked);
      }

      out.close();
    } catch(FileNotFoundException e) {
      System.out.println(e);
    }
  }

  public static void main(String[] args) {

    // String path = "/Users/mplaksyuk/src/OperationSystem/lab2/scheduling.conf";

    try {
      if ( args.length != 1 ) 
        throw new Exception("Usage: 'java Scheduling <INIT FILE>'");

      File f = new File(args[0]);
      if ( !f.exists() )
        throw new Exception("Scheduling: error, file '" + f.getName() + "' does not exist.");
      
      if ( !f.canRead() )
        throw new Exception("Scheduling: error, read of " + f.getName() + " failed.");

    } catch (Exception e) {
      System.out.println(e);
      System.exit(-1);
    }

    System.out.println("Working...");

    Init(args[0]);

    SchedulingAlgorithm.Run(runtime, processVector, result, maxqueues);    

    WriteSummaryResults(result);
        
    System.out.println("Completed.");
  }
}