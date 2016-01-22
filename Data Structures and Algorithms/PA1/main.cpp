// Cole Garien
// Data Structures & Algorithms
// MW 5:45

#include <iostream>
#include "Knights.h"

using namespace std;

bool solve_from(Knights &configuration) {
   if (configuration.is_solved()) {
	   // found a solution now show it
      configuration.print();
      return true;
   } else {
	   // store the current knight we are working on
	   int curR = configuration.curR;
	   int curC = configuration.curC;
	   //Half the L moves
      for (int row = curR-1; row <= curR+1; row += 2)
		  for(int col = curC-2; col <=curC+ 2; col+=4)
			  // if a knight is not there
			 if (configuration.canPlace(row, col)) {
				 // put a knight there
				configuration.insert(row, col);
				// prepare to work on the next knight
				configuration.curC = col;
				configuration.curR = row;
				// work on the next knight
				bool success = solve_from(configuration);
				// done with that knight
				configuration.remove(row, col);
				// if we found a solution
				if (success) return true;
			 }
		//Other Half
		for (int row = curR-2; row <= curR + 2; row += 4)
		  for(int col = curC-1; col <= curC+1; col+=2)
			 // if a knight is not there
			 if (configuration.canPlace(row, col)) {
				 // put a knight there
				configuration.insert(row, col);
				// prepare to work on the next knight
				configuration.curC = col;
				configuration.curR = row;
				// work on the next knight
				bool success = solve_from(configuration);
				// done with that knight
				configuration.remove(row, col);
				// if we found a solution
				if (success) return true;
			 }
			 
      
      return false;
   }
}

int main() {
   int board_size;
   int row;
   int col;
   cout << "What is the size of the board? " << endl;
   cin >> board_size;
   cout << "What is starting row?" << endl;
   cin >> row;
   cout << "What is starting column?" << endl;
   cin >> col;
   if (board_size <= 0 || board_size > max_board)
      cout << "The size must be between 0 and " << max_board << endl;
   else if(row < 0 || col >= max_board){
	   cout << "The column must be between 0 and " << max_board << endl;
   }else if(col < 0 || col >= max_board){
	   cout << "The row must be between 0 and " << max_board << endl;
   }else{
	   // make board and place the first knight
      Knights configuration(board_size,row,col);
	  // solve it
      bool success = solve_from(configuration);
	  // can't solve it then say so
      if (!success) cout << "No solution found." << endl;
   }

	system("pause");

}
