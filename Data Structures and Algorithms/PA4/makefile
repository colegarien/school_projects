all: pa4
pa4: removal main.o Hash.o
	g++ -o pa4 main.o Hash.o
main.o: main.cpp Hash.h
	g++ -c -g main.cpp
Hash.o: Hash.cpp
	g++ -c -g Hash.cpp
removal:
	rm -f *.o
