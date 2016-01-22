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
		a[i] = rand()%100000;  // random value from 0 - 99999
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

int partitionD(int a[], int low, int high){
	int pivot = (high - low)/2 + low;	// pick the middle
	int i = low-1;		// marks last ele in low set
	int temp;			// for swapping

	for(int k = low; k <= high; k++){
		if(a[k] < a[pivot]){	// if value goes in lower half
			i+=1;

			// if gonna shift pivot keep track of it
			if (i == pivot)
				pivot = k;

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

void quickDeter(int a[],int low, int high){
	if(low < high){ // if is valid input
		int pivot = partitionD(a,low,high); // put lows on left of pivot and highs on right
		quickDeter(a,low,pivot-1); // sort lower half
		quickDeter(a,pivot+1,high); // sort upper half
	}
}

int partitionR(int a[], int low, int high){
	int pivot = rand()%(high-low) + low;	// pick rand from low to high
	int i = low-1;		// marks last ele in low set
	int temp;			// for swapping

	for(int k = low; k <= high; k++){
		if(a[k] < a[pivot]){	// if value goes in lower half
			i+=1;

			// if gonna shift pivot keep track of it
			if (i == pivot)
				pivot = k;

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

void quickRand(int a[],int low, int high){
	if(low < high){ // if is valid input
		int pivot = partitionR(a,low,high); // put lows on left of pivot and highs on right
		quickRand(a,low,pivot-1); // sort lower half
		quickRand(a,pivot+1,high); // sort upper half
	}
}



int main(){
	srand(123456);
	clock_t timer;
	const int n = 50000;	// Max size
	const int trials = 100;	// times to run numbers
	int a[n];
	int b[n];

	cout<<"Number of trials per experiment: " << trials << endl<<endl;

	// run the trials
	for(int i = 10000; i <= n; i+= 5000){ // is the size of the data set
		float avgTimeDeter = 0;	// for getting avg times
		float avgTimeRand = 0;		// for gettign avg times
		for(int k = 0; k < trials; k++){ // number of times to run experiment
			fillRand(a,i);
			copy(a,a+i, b); // so both arrays have same values;

			// Quick DETERMINED
			//print(a,n);
			timer = clock(); // start timer;
			quickDeter(a,0,i-1);
			timer = clock()-timer; // end timer
			avgTimeDeter += (float)timer/CLOCKS_PER_SEC; // add to time
			//cout<< (float)timer/CLOCKS_PER_SEC<<endl; //print time
			//print(a,n);

			// Quick RANDOM
			//print(b,n);
			timer = clock(); // start timer;
			quickRand(b,0,i-1);
			timer = clock()-timer; // end timer
			avgTimeRand +=(float)timer/CLOCKS_PER_SEC; // add to time
			//cout<< (float)timer/CLOCKS_PER_SEC<<endl; //print time
			//print(b,n);
		}
		avgTimeDeter=avgTimeDeter/trials;
		avgTimeRand=avgTimeRand/trials;
		cout<<"Array filled with " << i << " random elements: " << endl;
		cout<<"-----------------------------------------------------------------------"<<endl;
		cout<<"Quick determined sort average time: " << (avgTimeDeter) << " seconds." << endl;
		cout<<"Quick random sort average time:     " << (avgTimeRand) << " seconds." << endl << endl;
	}

}