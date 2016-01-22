// Cole Garien
// Data Structures & Algorithms
// MW 5:45

const int max_board = 30;
class Knights{
	public:
		Knights(int size, int row, int col);
		bool is_solved() const;
		void print() const;
		bool canPlace(int row, int col) const;
		void insert(int row, int col);
		void remove(int row, int col);
		int board_size; // dimension of board
		int curR;
		int curC;
	private:
		int count; // number of placed knights
		int board[max_board][max_board];
};
