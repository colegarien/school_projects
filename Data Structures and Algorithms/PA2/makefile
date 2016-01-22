all: pa2-1 pa2-2
pa2-1: removal main1.o
	g++ -o pa2-1 main1.o
main1.o: main1.cpp
	g++ -c -g main1.cpp
pa2-2: removal main2.o
	g++ -o pa2-2 main2.o
main2.o: main2.cpp
	g++ -c -g main2.cpp
removal:
	rm -f *.o