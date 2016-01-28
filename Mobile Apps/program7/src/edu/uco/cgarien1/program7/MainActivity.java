// Cole Garien

package edu.uco.cgarien1.program7;

import edu.uco.cgarien1.program7.Contact;
import edu.uco.cgarien1.program7.NamesFragment.ListSelectionListener;
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity implements ListSelectionListener{
	
	// list programmed in random order
	public static Contact[] contacts = {
			new Contact("Elizabeth", "Jones","ejones@spaceman.com","(155)642-0008","http://en.wikipedia.org"),
			new Contact("Charles", "Martin","cmartin@hotmail.com","(525)735-0015","http://www.google.com"),
			new Contact("David", "Clark","dclark@louis.com","(555)654-0003","http://www.uco.edu"),
			new Contact("Paul", "Jackson","pjackson@yahoo.com","(955)735-0007","http://www.uco.edu"),
			new Contact("Sandra", "Moore","smoore@alphabet.org","(955)532-0001","http://www.space.com"),
			new Contact("Susan", "Davis","sdavis@website.com","(755)432-0013","http://www.google.com"),
			new Contact("Joseph", "Harris","jharris@msn.com","(558)642-0005","http://www.uco.edu"),
			new Contact("Richard", "Thompson","rthompson@internet.org","(553)213-0031","http://www.nasa.gov"),
			new Contact("Mary", "Smith","msmith@gmail.com","(575)513-0012","http://www.cplusplus.com"),
			new Contact("Deborah", "Taylor","dtaylor@furniture.net","(555)846-0002","http://www.nasa.gov"),
			new Contact("Betty", "Miller","bmiller@gmail.com","(556)846-0004","http://www.google.com"),
			new Contact("Daniel", "White","dwhite@yahoo.com","(575)634-0021","http://www.space.com"),
			new Contact("Barbara", "Williams","bwilliams@gmail.com","(755)412-0017","http://www.nasa.gov"),
			new Contact("Mark", "Anderson","manderson@gmail.com","(535)123-0014","http://www.space.com"),
			new Contact("Patricia", "Johnson","pjohnson@gmail.com","(535)846-0010","http://en.wikipedia.org"),
			new Contact("Texas", "Ranger","cnorris@texas.gov","(515)735-0011","http://www.tv.com/shows/walker-texas-ranger/"),
			new Contact("Thomas", "Thomas","tthomas@llamas.co.uk","(355)432-0009","http://www.cplusplus.com"),
			new Contact("Helen", "Wilson","hwilson@libary.edu","(553)634-0018","http://www.weather.com"),
			new Contact("John", "Robinson","jrobinson@gmail.com","(505)846-0016","http://www.weather.com"),
			new Contact("Maria", "Brown","mbrown@gmail.com","(155)432-0006","http://www.google.com")
			};
	
	private DetailsFragment detailsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// listed sorted in alphabetical order
		
		detailsFragment = (DetailsFragment)getFragmentManager().findFragmentById(R.id.details);
	}

	@Override
	public void onListSelection(int index){ 
		if (detailsFragment.getShownIndex() != index) {
			detailsFragment.showDetailsAtIndex(index);
		}
	}
}
