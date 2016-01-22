all: knights
knights: removal main.o Knights.o
	g++ -o knights main.o Knights.o
main.o: main.cpp
	g++ -c -g main.cpp
Knights.o: Knights.cpp
	g++ -c -g Knights.cpp
removal:
	rm -f *.o