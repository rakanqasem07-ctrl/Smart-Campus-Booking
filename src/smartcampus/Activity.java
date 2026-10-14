package smartcampus;
import java.util.ArrayList;

public class Activity {

    private String name;
	private int capacity,booked;
    private ArrayList<String> students;
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getBooked() {
		return booked;
	}
	public void setBooked(int booked) {
		this.booked = booked;
	}
	public ArrayList<String> getStudents() {
		return students;
	}
	public void setStudents(ArrayList<String> students) {
		this.students = students;
	}

    Activity(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.booked = 0;
        this.students = new ArrayList<>();
    }


    boolean register(String student) {
        if (booked >= capacity) {
        	return false;
        }
        students.add(student);
        booked++;
        return true;
    }
    boolean unregister(String student) {
        if (!students.remove(student)) return false;
        booked--;
        return true;
    }
    void displayStudents() {
        if (booked == 0) {
            System.out.println("No Students Registered");
        } else {
        	for (int i = 0; i < students.size(); i++) {
        	    System.out.println(students.get(i));
        	}
        }
    }
    void display(int index) {
        System.out.println(index + ". " + name + " (" + booked + "/" + capacity + ")");
    }
}