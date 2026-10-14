package smartcampus;

public class AcademicActivity extends Activity {
	private String instructor;
	private int roomNumber;
	
    public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	AcademicActivity(String name, String instructor, int roomNumber, int capacity) {
        super(name, capacity);
		this.instructor = instructor;
		this.roomNumber = roomNumber;
    }

    void display(int index) {
        System.out.println(index + ". [Academic] " + getName() + " (" + getBooked() + "/" + getCapacity() + ")");
    }
}