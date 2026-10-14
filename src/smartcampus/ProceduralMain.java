package smartcampus;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProceduralMain {

    public static void main(String[] args) {
        String[] activities = {"AI Workshop", "Football", "Career Day", "Robotics Club"};
        int[] capacity = {20, 30, 50, 15};
        int[] booked = {0, 0, 0, 0};
        ArrayList<ArrayList<String>> students = new ArrayList<>();
        for (int i = 0; i < activities.length; i++) {
            students.add(new ArrayList<>());
        }
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n1. Display Activities");
            System.out.println("2. Register Student");
            System.out.println("3. Unregister Student");
            System.out.println("4. View Students");
            System.out.println("5. Sort By Popularity");
            System.out.println("6. Activity Report");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
                sc.nextLine();
                choice = -1;
            }

            if (choice == 1) {
                System.out.println("\nActivities:");
                for (int i = 0; i < activities.length; i++)
                    System.out.println((i + 1) + ". " + activities[i] + " (" + booked[i] + "/" + capacity[i] + ")");

            } else if (choice == 2) {
                System.out.print("Student Name: ");
                String name = sc.nextLine();
                System.out.print("Activity Number: ");
                try {
                    int activity = sc.nextInt() - 1;
                    sc.nextLine();

                    if (activity < 0 || activity >= activities.length) {
                        System.out.println("Invalid Activity");
                    } else if (booked[activity] >= capacity[activity]) {
                        System.out.println("Activity Full");
                    } else {
                        students.get(activity).add(name);
                        booked[activity]++;
                        System.out.println("Registered Successfully");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input");
                    sc.nextLine();
                }

            } else if (choice == 3) {
                System.out.print("Student Name: ");
                String name = sc.nextLine();
                System.out.print("Activity Number: ");

                try {
                    int activity = sc.nextInt() - 1;
                    sc.nextLine();

                    if (activity < 0 || activity >= activities.length) {
                        System.out.println("Invalid Activity");
                    } else if (!students.get(activity).remove(name)) {
                        System.out.println("Student Not Found");
                    } else {
                        booked[activity]--;
                        System.out.println("Unregistered Successfully");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input");
                    sc.nextLine();
                }

            } else if (choice == 4) {
                System.out.print("Activity Number: ");

                try {
                    int activity = sc.nextInt() - 1;
                    sc.nextLine();

                    if (activity < 0 || activity >= activities.length) {
                        System.out.println("Invalid Activity");
                    } else {
                        System.out.println("\nStudents:");
                        if (booked[activity] == 0) {
                            System.out.println("No Students Registered");
                        } else {
                            for (int i = 0; i < booked[activity]; i++) {
                                System.out.println(students.get(activity).get(i));
                                }
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input");
                    sc.nextLine();
                }

            } else if (choice == 5) {
                for (int i = 0; i < (activities.length) - 1; i++) {
                    int max = i;
                    for (int j = i + 1; j < (activities.length); j++) {
                        if (booked[j] > booked[max]) {
                            max = j;
                        }
                    }

                    int tmpCap = capacity[i];
                    capacity[i] = capacity[max];
                    capacity[max] = tmpCap;

                    int tmpBooked = booked[i];
                    booked[i] = booked[max];
                    booked[max] = tmpBooked;

                    String tmpActivity = activities[i];
                    activities[i] = activities[max];
                    activities[max] = tmpActivity;

                    ArrayList<String> tmpStudents = students.get(i);
                    students.set(i, students.get(max));
                    students.set(max, tmpStudents);
                }
                System.out.println("Activities Sorted.");
                System.out.println("\nActivities:");
                for (int i = 0; i < activities.length; i++)
                    System.out.println((i + 1) + ". " + activities[i] + " (" + booked[i] + "/" + capacity[i] + ")");

            } else if (choice == 6) {
                System.out.println("\nActivity Report:");
                for (int i = 0; i < activities.length; i++)
                    System.out.println(activities[i] + ": " + booked[i] + " students");
            }

        } while (choice != 0);

        sc.close();
    }
}