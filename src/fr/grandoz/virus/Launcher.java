package fr.grandoz.virus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Launcher {

	private static List<Citizen> citizens = new ArrayList<>();

	private static List<Infected> canInfect = new ArrayList<>();

	public static List<Infected> cannotInfect = new ArrayList<>();

	public static int stage = 0;

	public static void main(String[] args) {

		int[] d2 = {3 , 4};

		start(Arrays.asList(d2) ,40);


	}

	public static void start(List<int[]> patients0, int n){

		if(!(patients0.size() > 0))return;
				//setup all citizens and patients
		for(int i = 0 ; i < n ; i++) {

			for(int o = 0 ; o < n ; o++) {

				for(int[] loc : patients0) {
					
					if(!(loc[0] > n || loc[1] > n)) {
						
						if(loc.length ==2 && loc[0] == i && loc[1] == o) {

							Infected patient = new Infected(i, o);
							canInfect.add(patient);

						}else {

							Citizen guy = new Citizen(i, o);
							citizens.add(guy);

						}
					}
				}

			}

		}

		if(citizens.size() == 0 || canInfect.size() == 0) return;
				//start main thread
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				step();

				System.out.println(getInformationsMessage());
				System.out.println("-------------------------------");

				if(citizens.size() == 0) {

					cancel();
					System.out.println("Tout le monde est mort !");
				}

			}
		}, 0 , 500);


	}

	public static void step() {
			//thread step function
		stage++;
		List<Infected> ptoRemove = new ArrayList<>();
		List<Infected> ptoAdd = new ArrayList<>();

			//Infect citizens behind
		for(Infected patient : canInfect) {
			
			boolean hasInfected = false;
			List<Citizen> gtoRemove = new ArrayList<>();
			
			for(Citizen guy : citizens) {

				if((guy.getX()+1 == patient.getX() && guy.getY() == patient.getY()) ||
						(guy.getX() == patient.getX() && (guy.getY()-1 == patient.getY())) ||
						(guy.getX()-1 == patient.getX() && guy.getY() == patient.getY())||
						(guy.getX() == patient.getX() && guy.getY()+1 == patient.getY())){

					ptoAdd.add(new Infected(guy.getX(), guy.getY()));
					gtoRemove.add(guy);
					hasInfected = true;

				}

			}

			citizens.removeAll(gtoRemove);
					
			//check if he can no longer Infect
			if(!hasInfected) {

				ptoRemove.add(patient);
				cannotInfect.add(patient);
			}


		}
		
		
		canInfect.addAll(ptoAdd);
		canInfect.removeAll(ptoRemove);
	}


	public static int getInfectionSize() {
		return canInfect.size()+cannotInfect.size();
	}

	
	public static String getInformationsMessage() {
		return "Jour " + stage + "\n" + "Il reste " + citizens.size() + " habitants et il y'a " + getInfectionSize() + " infectés";
	} 
	

}


class Citizen{
	

	private int x;
	
	private int y;
	

	
	public Citizen(int x , int y) {
		this.x = x;
		this.y = y;
	
	}
	
	

	public int getY() {
		return y;
	}


	public int getX() {
		return x;
	}

	
}

class Infected {
	
	private int x;
	private int y;
	
	public Infected(int x, int y) {
		this.x = x;
		this.y = y;
		
	}

	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}



}
