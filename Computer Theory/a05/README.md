## Purpose

Implement a program to create and simulate a UTM (Universal Turing Machine) based on a specially formatted input file.

## Special input file

_note that the comments are only for clarity and not valid syntax_
```
anbn.utm -- UTM file for strings a^n b^n	# Comment line
1 6						# Start_State End_State
abaabaaabaab			# Code Word Segment
aabaabaaaaaab			# Code Word Segment 
aabaabbabbabb			# Code Word Segment
aabaaabaabbaba			# Code Word Segment
aaabaaabbabbaba			# Code Word Segment
aaabaaaabaaaaaaa		# Code Word Segment
aaabaaaaabbaabaab		# Code Word Segment
aaaabaaaabaaaaaaa		# Code Word Segment
aaaababbaabaab			# Code Word Segment
aaaaabaaaaabbabbabb		# Code Word Segment
aaaaabaaaaaababaabab#	# Final Segment (ends with '#')
aabb					# input tape lines
aaabbb					# - program will tell if Accept/Reject
aaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbb
aaabbbb
abaaba
ab
```

### Decoding Code Word Segments

Each Code Word Segment gets converted into a Transition Definition via the following process
 1. The number of a's till the first b is `current_state`
 2. The number of a's from the first b to the second b is the `next_state`
 3. The last 7 characters of the segment get decoded as following `[3 letter][3 letter][1 direction]` => `read_char write_char direction`
   * the [1 direction] gets mapped as suchs `'a' -> L` and `'b' -> R`
   * the [3 letter] segements get decoded as such:
    ```
    "aaa" => 'a'
    "aab" => 'b'
	"aba" => 'D'
	"abb" => '#'
	"baa" => 'A'
	"bab" => 'B'
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
Script started on Sat 22 Nov 2014 12:25:56 AM CST

cole@cs:~/a05$ g++ utm.cpp -o utm
cole@cs:~/a05$ utm anbn.utm
Comment: anbn.utm -- UTM file for strings a^n b^n
Start State: 0
End State: 5
# State's: 6
	abaabaaabaabaabaabaaaaaabaabaabbabbabbaabaaabaabbabaaaabaaabbabbabaaaabaaaabaaaaaaaaaabaaaaabbaabaabaaaabaaaabaaaaaaaaaaababbaabaabaaaaabaaaaabbabbabbaaaaabaaaaaababaabab---> REJECT (crash, no leaving edge)
	aabb---> ACCEPT
	aaabbb---> ACCEPT
	aaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbb---> ACCEPT
	aaabbbb---> REJECT (crash, no leaving edge)
	abaaba---> REJECT (crash, no leaving edge)
	ab---> ACCEPT
cole@cs:~/a05$ exit
exit

Script done on Sat 22 Nov 2014 12:26:15 AM CST
```