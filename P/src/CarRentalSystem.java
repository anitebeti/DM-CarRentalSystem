import java.util.HashMap;
import java.util.Scanner;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;


public class CarRentalSystem implements Serializable{
	
	private static Scanner sc = new Scanner(System.in);
	private HashMap<String, String> rentedCars = new HashMap<String, String>(100, 0.5f);
	private HashMap<String, RentedCars> rentedCarsByOwner = new HashMap<String, RentedCars>();
	
	private static final long serialVersionUID = 3L;
	
	
	private String getPlateNo() {
		System.out.println("introduceti nr de inatriculare");
		return sc.nextLine();
	}
	
	private String getOwnerName() {
		System.out.println("introduceti numele proprietarului");
		return sc.nextLine();
	}
	
	private boolean isCarRent(String plateNo) {
		return rentedCars.containsKey(plateNo);
	}
	
	private String getCarRent(String plateNo) {
		if (rentedCars.get(plateNo) == null) {
			System.out.println("Aceasta masina nu este inchiriata!");
			return null;
		}
		return rentedCars.get(plateNo);
	}
	
	private void rentCar(String plateNo, String ownerName) {
		try {																					//NullPointerException
			
			if (this.isCarRent(plateNo)) {
				System.out.println("Aceasta masina este deja inchiriata!");
				return;
			}
			rentedCars.put(plateNo, ownerName);
			
			if (!rentedCarsByOwner.containsKey(ownerName)) {
				RentedCars owed = new RentedCars();
				owed.addCar(plateNo);
				rentedCarsByOwner.put(ownerName, owed);
			} else {
				rentedCarsByOwner.get(ownerName).addCar(plateNo);
			}
			
		} catch (NullPointerException e) {
			System.out.println("Error: Obiectul nu exista!");
		}
		
		
	}
	
	private void returnCar(String plateNo) {
		try {																					//NullPointerException
			String owner = rentedCars.get(plateNo);
			if (rentedCars.remove(plateNo) == null) {
				System.out.println("Masina nu a fost gasita in baza de date!");
			} else {
				rentedCarsByOwner.get(owner).removeCar(plateNo);
				System.out.println("Masina a fost starsa cu succes!");
			}
		} catch (NullPointerException e) {
			System.out.println("Error: Obiectul nu exista!");
		}
		
	}
	
	private int totalRented() {
		return rentedCars.size();
	}
	
	private int getCarsNo(String owner) {
		try {																					//NullPointerExcception
			return rentedCarsByOwner.get(owner).getCarNumber();
		} catch (NullPointerException e) {
			System.out.println("Error: Obiectul nu exista!");
		}
		return -1;
	}
	
	private void getCarsList(String owner) {
		try {																					//NullPointerException
			rentedCarsByOwner.get(owner).getCarsList();
		} catch (NullPointerException e) {
			System.out.println("Error: Obiectul nu exista!");
		}
	}
	
	

	private static void printCommandsList() {
	    System.out.println("help          - Afiseaza aceasta lista de comenzi");
	    System.out.println("add           - Adauga o noua pereche (masina, sofer)");
	    System.out.println("check         - Verifica daca o masina este deja luata");
	    System.out.println("remove        - Sterge o masina existenta din hashtable");
	    System.out.println("getOwner      - Afiseaza proprietarul curent al masinii");
	    System.out.println("totalRented   - Afiseaza numarul total de masini inchiriate");
	    System.out.println("carsNumber    - Afiseara numarul de masini inchiriate de catre o persoana");
	    System.out.println("carList       - Afiseaza lista de masini inchiriate de catre o persoana");
	    System.out.println("rentedByCar  - Afiseaza toate masinile inchiriate");
	    System.out.println("rentedByOwner - Afiseaza toti proprietarii impreuna cu masinile inchiriate");
	    System.out.println("quit          - Inchide aplicatia");
	    
	}

//2 metode noi - printeaza cele 2 HashMap-uri - comenzile "rentedByCar" si  "rentedByOwner"
	public void printRentedByCar() {
		System.out.println("\nSe afiseaza toate masinile inchiriate");
		Iterator<String> itCar = this.rentedCars.keySet().iterator();
		Iterator<String> itOwner = this.rentedCars.values().iterator();
		while (itCar.hasNext()) {
			System.out.println(itCar.next() + "\t-> " + itOwner.next() + " ");
		}
	}
	
	public void printRentedByOwner() {
		System.out.println("\nSe afiseaza toti proprietarii impreuna cu masinile inchiriate");
		Iterator<String> itOwner = this.rentedCarsByOwner.keySet().iterator();
		Iterator<RentedCars> itCars = this.rentedCarsByOwner.values().iterator();
		while (itOwner.hasNext()) {
			System.out.println(itOwner.next() + "\t-> " + itCars.next() + " ");
		}
	}
	
//Save and restore 2 HashMaps from the binary file - Serialization
	public void saveRentedByCar() throws IOException{
		try (ObjectOutputStream binaryFileOut = new ObjectOutputStream (new BufferedOutputStream (new FileOutputStream ("rentedData\\rentedByCar.dat")))) {
			binaryFileOut.writeObject(this.rentedCars);
		} 
	}
	
	public void restoreRentedByCar() throws IOException {
		try (ObjectInputStream binaryFileIn = new ObjectInputStream (new BufferedInputStream (new FileInputStream ("rentedData\\rentedByCar.dat")))) {
			this.rentedCars = (HashMap<String, String>) binaryFileIn.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Fisierul nu a fost gasit: " + e.getMessage());
		}
	}
	
	public void saveRentedByOwner() throws IOException{
		try (ObjectOutputStream binaryFileOut = new ObjectOutputStream (new BufferedOutputStream (new FileOutputStream ("rentedData\\rentedByOwner.dat")))) {
			binaryFileOut.writeObject(this.rentedCarsByOwner);
		}
	}
	
	public void restoreRentedByOwner() throws IOException {
		try (ObjectInputStream binaryFileIn = new ObjectInputStream (new BufferedInputStream (new FileInputStream ("rentedData\\rentedByOwner.dat")))) {
			this.rentedCarsByOwner = (HashMap<String, RentedCars>) binaryFileIn.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Fisierul nu a fost gasit: " + e.getMessage());
		}
	}
	public void run() {
		boolean quit = false;
		
		try {
			this.restoreRentedByCar();
			this.restoreRentedByOwner();
		} catch (IOException e) {
			System.out.println("IOException thrown when trying to restore data: " + e.getMessage());
		}
		
		while (!quit) {
			System.out.println("Asteapta comanda: (help - Afiseaza lista de comenzi)");
			String command = sc.nextLine();
			switch(command) {
				case "help":
					printCommandsList();
					break;
				case "add":
					rentCar(getPlateNo(), getOwnerName());
					break;
				case "check":
					System.out.println(isCarRent(getPlateNo()));
					break;
				case "remove":
					returnCar(getPlateNo());
					break;
				case "getOwner":
					System.out.println(getCarRent(getPlateNo()));
					break;
				case "totalRented": 
					System.out.println(totalRented());
					break;
				case "carsNumber":
					System.out.println(getCarsNo(getOwnerName()));
					break;
				case "carList":
					getCarsList(getOwnerName());
					break;
				case "rentedByCar":
					printRentedByCar();
					break;
				case "rentedByOwner":
					printRentedByOwner();
					break;
				case "quit":
					System.out.println("Aplicatia se inchide...");
					quit = true;
					break;
				default:
					System.out.println("Unknown command. Chose from:");
					printCommandsList();
			}
		}
		
		try {
			this.saveRentedByCar();
			this.saveRentedByOwner();
		} catch (IOException e) {
			System.out.println("IOException thrown when trying to save data: " + e.getMessage());
		}
	}
	
	
	
	public static void main(String[] args) {
		
		new CarRentalSystem().run();
		
		
		

	}

}
