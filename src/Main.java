import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print(" Number of judges: ");
        int numJudges = input.nextInt();
        System.out.print("Cost: ");
        int changeCost = input.nextInt();
        List<String> tasks = new ArrayList<>();
        readTasks(tasks); // Read tasks from a file

        // Create a 2D array to store judge assignments
        String[][] judgeAssignments = new String[numJudges][tasks.size()];

        // Track the number of tasks assigned to each judge
        int[] assignmentCounts = new int[numJudges];

        // Calculate total cost and assign tasks to judges
        int totalCost = assignTasks(tasks, changeCost, judgeAssignments, numJudges, assignmentCounts);
        totalCost = totalCost + (numJudges * changeCost); // Add change cost for each judge
        System.out.println("Total cost: " + totalCost);

        // Display the assignments of tasks to judges
        displayJudgeAssignments(numJudges, judgeAssignments, assignmentCounts);
    }

    // Read tasks from a file
    private static void readTasks(List<String> tasks) throws FileNotFoundException {
        File file = new File("input2.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                if (lineScanner.hasNextInt()) {
                    tasks.add(String.valueOf(lineScanner.nextInt()));
                } else {
                    lineScanner.next(); // Skip non-integer tokens
                }
            }
            lineScanner.close(); // Close the line scanner after processing the line
        }
        scanner.close(); // Close the main scanner after reading the file
    }

    // Assign tasks to judges based on cost
    private static int assignTasks(List<String> tasks, int changeCost, String[][] judgeAssignments, int numJudges, int[] assignmentCounts) {
        String[] lastAssignedTask = new String[numJudges];
        Map<String, Integer> lastJudgeForTask = new HashMap<>(); // Tracks the last judge that handled each task

        int totalCost = 0;

        // Process each task as it comes
        for (String task : tasks) {
            int judgeToAssign = -1;

            // Check if this task has a preferred judge (one who handled it last time)
            if (lastJudgeForTask.containsKey(task) && lastAssignedTask[lastJudgeForTask.get(task)] != null && lastAssignedTask[lastJudgeForTask.get(task)].equals(task)) {
                judgeToAssign = lastJudgeForTask.get(task);
            } else {
                // Find the judge who can take this task with the least additional cost
                int minCost = Integer.MAX_VALUE;
                for (int j = 0; j < numJudges; j++) {
                    int cost = (lastAssignedTask[j] != null && !lastAssignedTask[j].equals(task)) ? changeCost : 0;
                    if (cost < minCost) {
                        minCost = cost;
                        judgeToAssign = j;
                    }
                }
            }

            // Assign the task to the chosen judge
            if (lastAssignedTask[judgeToAssign] == null || !lastAssignedTask[judgeToAssign].equals(task)) {
                if (lastAssignedTask[judgeToAssign] != null && !lastAssignedTask[judgeToAssign].isEmpty()) {
                    totalCost += changeCost;
                }
                lastAssignedTask[judgeToAssign] = task;
            }

            // Update the assignment matrix and record the judge as the last to handle this task
            judgeAssignments[judgeToAssign][assignmentCounts[judgeToAssign]++] = task;
            lastJudgeForTask.put(task, judgeToAssign);
        }

        return totalCost;
    }

    // Display assignments of tasks to judges
    private static void displayJudgeAssignments(int numJudges, String[][] judgeAssignments, int[] assignmentCounts) {
        for (int i = 0; i < numJudges; i++) {
            System.out.print("Judge " + (i + 1) + ": ");
            for (int j = 0; j < assignmentCounts[i]; j++) {
                System.out.print(judgeAssignments[i][j] + (j < assignmentCounts[i] - 1 ? ", " : ""));
            }
            System.out.println();
        }
    }
}


 /*
        readTasks method time complexity of this method is O(n), where n is the total number of integers in the input file
        assignTasks method time complexity of this method is O(n * m).
        displayJudgeAssignments method  time complexity of this method is O(m) wich m is the number of judges.
         time complexity of the entire program is O(n * m), where n is the number of tasks and m is the number of judges.
 */
