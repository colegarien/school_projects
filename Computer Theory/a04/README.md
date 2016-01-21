## Purpose

Implement a program to create and simulate a TM (Turing Machine) based on a specially formatted input file.

## Special input file

_note that the comments are only for clarity and not valid syntax_
```
TM for second letter b	# this line is a comment
abD						# The Alphabet, note 'D' is always on the end
4						# Number of States
0 1 aar					# Transition Definition
0 1 bbr					# Transition Definition
1 2 bbr					# Transition Definition
2 2 aar					# Transition Definition
2 2 bbr					# Transition Definition
2 3 DDr					# Transition Definition
-1						# END of Definition (always -1)
0						# Start State
1 3						# Number_End_States End_State
aba						# Input tapes for TM
babba					# - each line is a tape to proces
aaabbb					# - the program will say ACCEPT/REJECT
abaaba
```

### Transition Definition Lines

The transition definition line can be generalized as follows `current_state next_state actions`

 * `current_state` - defines which state this definition is for
 * `next_state` - defines what state to transition to
 * `action` - the action can be broken into three pieces (one for each character) `read_char write_char direction`
   * `read_char` - the character that must be read to make the transition
   * 'write_char' - the character to write on the tape in-place of the `read_char`
   * `direction` - the direction to move the tape head (either `r - right` or `l - left`)

## SAMPLE RUN

```
Script started on Fri 07 Nov 2014 03:27:21 PM CST
cole@cs:~/a04$ g++ tm.cpp -o tm
cole@cs:~/a04$ tm tm1.tm
Comment: TM for second letter b
Alphabet: abD
Start State: 0
End State: 1
	aba---> ACCEPT
	babba---> REJECT (crash, no leaving edge)
	aaabbb---> REJECT (crash, no leaving edge)
	abaaba---> ACCEPT
cole@cs:~/a04$ exit
exit

Script done on Fri 07 Nov 2014 03:27:36 PM CST
```