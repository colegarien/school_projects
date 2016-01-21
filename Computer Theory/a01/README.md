## Purpose

Implement a program to create and simulate a DFA (Deterministic Finite Automata) based on a specially formatted input file.

## Special input file

_note that the comments are only for clarity and not valid syntax_
```
M5 is a DFA sample	# First line is a comment
r012				# Alphabet for DFA
3					# Number of States
1 1 2 3				# Transitions for State 1
1 2 3 1				# Transitions for State 2
1 3 1 2				# Transitions for State 3
1					# Start State, in this case S1
1 1					# Number_End_States End_State
11r1011				# Starting here are just inputs to simulate
101101				# - inputs are fed left to right to DFA
10r22r012			# - program will tell if string valid or not
```

### Notes on Transition Lines

Each number on the transition lines hold two pieces of information
  1. _position_ - The 1st number determines what happens on the input of the 1st letter of the alphabet, the 2nd number for the 2nd letter of the alphabet, etc...
  2. _value_ - The value of the number says what state to transition to, for example 1 mean state 1, 2 means state 2, etc...
  
As an example of these rules consider `1 1 2 3`, i.e. the first transition definition line of m5.dat, note this describes the behavior of _state 1_.

 * At position 1 we have the value '1', this means that if the DFA is currently on state 1 and it receives the first letter of the alphabet (in this case 'r') then it should transition to state 1
 * At position 2 we have the value '1', this means that if the DFA is currently on state 1 and it receives the second letter of the alphabet (in this case '0') then it should transition to state 1
 * At position 3 we have the value '2', this means that if the DFA is currently on state 1 and it receives the third letter of the alphabet (in this case '1') then it should transition to state 2
 * At position 4 we have the value '3', this means that if the DFA is currently on state 1 and it receives the forth letter of the alphabet (in this case '2') then it should transition to state 3

## SAMPLE RUN

```
Script started on Thu 04 Sep 2014 09:14:29 PM CDT

cole@cs:~/a01$ g++ dfa.cpp -o dfa
cole@cs:~/a01$ dfa m5.dat
comment: M5 is a DFA sample
alphabet: r012
start state: 1
end state(s): 1 

Delta Table
  | r 0 1 2
===========
1 | 1 1 2 3 
2 | 1 2 3 1 
3 | 1 3 1 2 

	11r1011---> ACCEPT
	101101---> REJECT
	10r22r012---> ACCEPT
cole@cs:~/a01$ exit
exit

Script done on Thu 04 Sep 2014 09:14:56 PM CDT
```