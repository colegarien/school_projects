// Cole Garien
// Data Structures & Algorithms
// MW 5:45

#include <iostream>
#include <cstdlib>
#include <ctime>

using namespace std;

void print(int a[], int n){
	cout << "{";
	for(int i = 0; i < n; i++){
		cout << a[i];
		if(i != n-1) cout<<", ";
	}
	cout<<"}"<<endl;
}

void fillRand(int a[], int n){
	for(int i = 0; i < n; i++){
		a[i] = rand()%100 + 1;
	}
}

void insertionSort(int a[],int n){
	int temp;
	int loc;
	for(int i = 1; i < n; i ++){
		if(a[i] < a[i-1]){
			temp = a[i]; // number to reinsert
			loc = i;     // where it was
			bool done = false;
			while(loc >= 0 && !done){ // shift everything to right and look for place to put temp
				if(temp < a[loc-1]){ // if temp needs to be place more left
					a[loc] = a[loc-1];
					loc-=1;
				}else{ // found a place for temp
					a[loc] = temp;
					done = true;
				}
			}
		}
	}
}

int partition(int a[], int low, int high){
	int pivot = high;	// pick the last
	int i = low-1;		// marks last ele in low set
	int temp;			// for swapping 
	for(int k = low; k < high; k++){
		if(a[k] < a[pivot]){	// if value goes in lower half
			i+=1;
			// swap
			temp = a[i];
			a[i] = a[k];
			a[k] = temp;
		}
	}
	// now put pivot at end of low list
	temp = a[i+1];
	a[i+1] = a[pivot];
	a[pivot] = temp;
	
	return i+1; // where pivot is now
}

void quickSort(int a[],int low, int high){
	if(low < high){ // if is valid input
		int pivot = partition(a,low,high); // put lows on left of pivot and highs on right
		quickSort(a,low,pivot-1); // sort lower half
		quickSort(a,pivot+1,high); // sort upper half
	}
}

int main(){
	srand(123456);
	clock_t timer;
	const int n = 50000;	// Max size
	const int trials = 10;	// times to run numbers
	int a[n];
	int b[n];
	
	cout<<"Number of trials per experiment: " << trials << endl<<endl;

	for(int i = 10000; i <= n; i+= 5000){ // is the size of the data set
		float avgTimeInsertion = 0;	// for getting avg times
		float avgTimeQuick = 0;		// for gettign avg times
		for(int k = 0; k < trials; k++){ // is the number of times to run experiment
			fillRand(a,i);
			copy(a,a+n, b); // so both arrays have same values;

			// SORTING WITH INSERTION
			//print(a,i);
			timer = clock(); // start timer;
			insertionSort(a,i);
			timer = clock()-timer; // end timer
			avgTimeInsertion += (float)timer/CLOCKS_PER_SEC; // add to time
			//cout<< (float)timer/CLOCKS_PER_SEC<<endl; //print time
			//print(a,i);

			// SORTING WITH QUICKSORT
			//print(b,i);
			timer = clock(); // start timer;
			quickSort(b,0,i-1);
			timer = clock()-timer; // end timer
			avgTimeQuick +=(float)timer/CLOCKS_PER_SEC; // add to time
			//cout<< (float)timer/CLOCKS_PER_SEC<<endl; //print time
			//print(b,i);
		}
		avgTimeInsertion=avgTimeInsertion/trials;
		avgTimeQuick=avgTimeQuick/trials;
		cout<<"Array filled with " << i << " random elements: " << endl;
		cout<<"-----------------------------------------------------------------------"<<endl;
		cout<<"Insertion sort average time: " << (avgTimeInsertion) << " seconds." << endl;
		cout<<"Quick sort average time:     " << (avgTimeQuick) << " seconds." << endl << endl;
	}

}