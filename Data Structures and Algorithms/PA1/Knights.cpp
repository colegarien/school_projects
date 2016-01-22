// Cole Garien
// Data Structures & Algorithms
// MW 5:45

#include <iostream>
#include <iomanip>
#include "Knights.h"

using namespace std;
Knights::Knights (int size, int r, int c) {
   board_size = size;
   curR = r;
   curC = c;
   count = 0;
   for (int row = 0; row < board_size; row++)
      for (int col = 0; col < board_size; col++)
         board[row][col] = 0;
   board[r][c] = 1;
   count++;
}

bool Knights::is_solved() const {
   if (count == board_size*board_size) return true;
   else return false;
}

void Knights::print() const {
   int row, col;
	
   for(row = 0; row < board_size; row++){
	   for(col = 0; col < board_size; col++){
		   cout<<setw(4)<<board[row][col]<<" ";
	   }
	   cout<< endl;
   }
}

bool Knights::canPlace(int row, int col) const {
   if(row >= 0 && row < board_size && col >= 0 && col < board_size && board[row][col] == 0)
	   return true;
   else
       return false;
}

void Knights::insert(int row, int col){
   board[row][col] = ++count;
}	


void Knights::remove(int row, int col){
   board[row][col] = 0;
   count--;
}

