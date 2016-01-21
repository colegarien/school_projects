# Computer Organization II

Project 1 was created to simulate two 8-bit registers made of 8 simulated D flip flops.

`i01.dat` is a specially formated input file
`o01.dat` is the formatted output file

to use the simulated registers, the input file uses the following op-codes
```
000 - XOR		// R0 XOR R1
001 - OR		// R0 OR R1
010 - XNOR		// R0 XNOR R1
011 - AND		// R0 AND R1
100 - LOAD_R0	// note this op-code requires 8-bit input
101 - LOAD_R1	// note this op-code requires 8-bit input
110 - PRINT_R0	// contents of R0 is displayed
111 - PRINT_R1  // contents of R1 is displayed
```

### Compiling and Running
```
make -f p01make
p01 i01.dat o01.dat
```
