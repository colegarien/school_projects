## Purpose

Implement a program to create and simulate a PDA (Push-Down Automata) based on a specially formatted input file.

## Special input file

_note that the comments are only for clarity and not valid syntax_
```
PDA for language a^n b^n	# this is a comment line
abD							# the alphabet, note D is always the last letter
8							# Number of States
1 1 1						# Transition Definition for S0
2 5 3						# Transition Definition for S1
1 1 1						# Transition Definition for S2
7 7 4						# Transition Definition for S3
4 4 4						# Transition Definition for S4
6 7 7						# Transition Definition for S5
7 5 3						# Transition Definition for S6
7 7 7						# Transition Definition for S7
0							# Start State
0 0 0						# Action Definition for S0
0 0 1						# Action Definition for S1
0 0 2						# Action Definition for S2
0 0 3						# Action Definition for S3
1 0 0						# Action Definition for S4
0 0 3						# Action Definition for S5
0 0 1						# Action Definition for S6
0 1 0						# Action Definition for S7
aaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbb # Begin Input lines
aaabbb						# - each line is seen as a tape for the PDA
aab							# - the program will tell if Accept/Reject
abb
ab
```

### Transition Definition Lines

Each number on the transition lines hold two pieces of information
  1. _position_ - The 1st number determines what happens on the input of the 1st letter of the alphabet, the 2nd number for the 2nd letter of the alphabet, etc...
  2. _value_ - The value of the number says what state to transition to, for example 1 mean state 1, 2 means state 2, etc...
  
As an example of these rules consider `1 1 1`, i.e. the first transition definition line of anbn.pda, note this describes the behavior of _state 1_.

 * At position 1 we have the value '1', this means that if the DFA is currently on S0 and it receives the first letter of the alphabet (in this case 'r') then it should transition to S1
 * At position 2 we have the value '1', this means that if the DFA is currently on S0 and it receives the second letter of the alphabet (in this case '0') then it should transition to S1
 * At position 3 we have the value '1', this means that if the DFA is currently on S0 and it receives the third letter of the alphabet (in this case 'D' which also symbolizes DELTA) then it should transition to S1

### Action Definition Lines

Each action definition line can be generalized as `is_accepting is_rejecting action`

 * `is_accepting` - 0 means false, 1 means true, if the tape head finishes on an accepting state the tape is ACCEPTed
 * `is_rejecting` - 0 means false, 1 means true, if the tage head finishes on a rejecting state the tape is REJECTed
 * `action` - this describes what action to take when transitioning to a new state, see _Available Actions_ below
 
_Available Actions_
```
0 - ACT_NONE	# Take no action
1 - ACT_READ	# Move the Tape Head forward
2 - ACT_PUSH	# Push the letter that was last read
3 - ACT_POP		# POP the stack
```
## SAMPLE RUN

```
Script started on Thu 16 Oct 2014 09:11:36 PM CDT
cole@cs:~/a03$ g++ pda.cpp -o pda
cole@cs:~/a03$ pda anbn.pda
Comment: PDA for language a^n b^n
Alphabet: abD
	aaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbb---> ACCEPT
	aaabbb---> ACCEPT
	aab---> REJECT
	abb---> REJECT
	ab---> ACCEPT
cole@cs:~/a03$ exit
exit

Script done on Thu 16 Oct 2014 09:11:53 PM CDT
```
