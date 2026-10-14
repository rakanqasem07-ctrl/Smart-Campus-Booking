package smartcampus;

public class SportActivity extends Activity {
	private String coach, location;
	
    public String getCoach() {
		return coach;
	}
	public void setCoach(String coach) {
		this.coach = coach;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	SportActivity(String name, String coach, String location, int capacity) {
        super(name, capacity);
		this.coach = coach;
		this.location = location;
        
    }

    void display(int index) {
        System.out.println(index + ". [Sport] " + getName() + " (" + getBooked() + "/" + getCapacity() + ")");
    }
}