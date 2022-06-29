import java.util.ArrayList;
import java.io.Serializable;

public class RentedCars implements Serializable{
	
	private ArrayList<String> rentedCars;
	
	private static final long serialVersionUID = 3L;
	
	public RentedCars() {
		this.rentedCars = new ArrayList<String>();
	}
	
	public void addCar(String plateNo) {
		this.rentedCars.add(plateNo);
	}
	
	public void removeCar(String plateNo) {
		this.rentedCars.remove(plateNo);
	}
	
	public int getCarNumber() {
		return this.rentedCars.size();
	}
	
	public void getCarsList() {																//IndexOutOfBoundsException
		try {
			for (int i = 0; i < this.rentedCars.size(); i++) {
				System.out.print(this.rentedCars.get(i) + "\t");
			}
			System.out.println();
			
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Error: a fost accesat o pozitie inexistenta.");
		}
	}
	
	@Override
	public String toString() {
		String resp = "Rented ";
		for (String car : this.rentedCars) {
			resp += " - " + car;
		}
		return resp;
	}

}
