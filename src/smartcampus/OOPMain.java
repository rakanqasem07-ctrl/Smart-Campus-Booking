package smartcampus;
import java.util.ArrayList;
import java.util.Scanner;

public class OOPMain {

    public static void main(String[] args) {

        AcademicActivity AIWorkshop = new AcademicActivity("AI Workshop", "Dr. Ahmad", 101, 20);
        SportActivity Football = new SportActivity("Football", "Coach Samir", "Stadium", 30);
        AcademicActivity CareerDay = new AcademicActivity("Career Day", "Dr. Sara", 202, 50);
        AcademicActivity RoboticsClub = new AcademicActivity("Robotics Club", "Dr. Khalid", 303, 15);

        ArrayList<Activity> activities = new ArrayList<>();
        activities.add(AIWorkshop);
        activities.add(Football);
        activities.add(CareerDay);
        activities.add(RoboticsClub);

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
            } catch (Exception e) {
                System.out.println("Invalid Input");
                sc.nextLine();
                choice = -1;
            }

            if (choice == 1) {
                System.out.println("\nActivities:");
                for (int i = 0; i < activities.size(); i++)
                    activities.get(i).display(i + 1);

            } else if (choice == 2) {
                System.out.print("Student Name: ");
                String name = sc.nextLine();
                System.out.print("Activity Number: ");

                try {
                    int index = sc.nextInt() - 1;
                    sc.nextLine();

                    if (index < 0 || index >= activities.size()) {
                        System.out.println("Invalid Activity");
                    } else if (!activities.get(index).register(name)) {
                        System.out.println("Activity Full");
                    } else {
                        System.out.println("Registered Successfully");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    sc.nextLine();
                }

            } else if (choice == 3) {
                System.out.print("Student Name: ");
                String name = sc.nextLine();
                System.out.print("Activity Number: ");

                try {
                    int index = sc.nextInt() - 1;
                    sc.nextLine();

                    if (index < 0 || index >= activities.size()) {
                        System.out.println("Invalid Activity");
                    } else if (!activities.get(index).unregister(name)) {
                        System.out.println("Student Not Found");
                    } else {
                        System.out.println("Unregistered Successfully");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    sc.nextLine();
                }

            } else if (choice == 4) {
                System.out.print("Activity Number: ");

                try {
                    int index = sc.nextInt() - 1;
                    sc.nextLine();

                    if (index < 0 || index >= activities.size()) {
                        System.out.println("Invalid Activity");
                    } else {
                        System.out.println("\nStudents:");
                        activities.get(index).displayStudents();
                    }
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    sc.nextLine();
                }

            } else if (choice == 5) {
                int n = activities.size();
                for (int i = 0; i < n - 1; i++) {
                    int max = i;
                    for (int j = i + 1; j < n; j++) {
                        if (activities.get(j).getBooked() > activities.get(max).getBooked())
                            max = j;
                    }
                    Activity tmp = activities.get(i);
                    activities.set(i, activities.get(max));
                    activities.set(max, tmp);
                }
                System.out.println("Activities Sorted.");
                System.out.println("\nActivities:");
                for (int i = 0; i < activities.size(); i++)
                    activities.get(i).display(i + 1);

            } else if (choice == 6) {
                System.out.println("\nActivity Report:");
                for (Activity a : activities)
                    System.out.println(a.getName() + ": " + a.getBooked() + " students");
            }

        } while (choice != 0);

        sc.close();
    }
}