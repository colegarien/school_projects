all: pa3
pa3: removal main.o Graph.o
	g++ -o pa3 main.o Graph.o
main.o: main.cpp Graph.h
	g++ -c -g main.cpp
Graph.o: Graph.cpp
	g++ -c -g Graph.cpp
removal:
	rm -f *.o
