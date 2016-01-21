## Purpose

Implement a program to create and simulate a TG (Transition Graph) based on a specially formatted input file.

## Special input file

_note that the comments are only for clarity and not valid syntax_
```
TG for language (aa)*bbb	# This line is a comment
ab							# The Alphabet for the TG
2							# Number of States
2							# Number of Edges
1 1 aa						# Transition Definition (details below)
1 2 bbb						# Transition Definition (details below)
1							# Start State
1 2							# Number_End_States End_State
bbb							# Start Feeding Input into the TG
aaaabbb						# - input fed from left to right
ababab						# - program will tell if accept/reject
aaabbb						# - if Reject, input left-over provided
aabbb						#
aabbbb						#
aaaaaaaaaaaabbb				#
```

### Transition Definition Lines

The Transition defintion can be generalized as follows `current_state output_state required_input`

For example, consider the line `1 1 aa`
 * This rule of for State 1
 * This transition results in the state changing to State 1
 * This transition occurs when the string 'aa' is received

## SAMPLE RUN

```
Script started on Mon 15 Sep 2014 01:18:46 PM CDT
cole@cs:~/a02$ g++ tg.cpp -o tg
cole@cs:~/a02$ tg ea3b.tg
Comment: TG for language (aa)*bbb
	bbb---> ACCEPT
	aaaabbb---> ACCEPT
	ababab---> REJECT (Crash, input left: ababab)
	aaabbb---> REJECT (Crash, input left: abbb)
	aabbb---> ACCEPT
	aabbbb---> REJECT (Crash, input left: b)
	aaaaaaaaaaaabbb---> ACCEPT
cole@cs:~/a02$ exit
exit

Script done on Mon 15 Sep 2014 01:19:11 PM CDT
```