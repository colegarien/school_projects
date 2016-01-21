# Purpose

Implement a functionality for determining if an item is in a list or if a list is a subset of another list

# SAMPLE RUN

```
Interpreting project: Prolog Programs
  Loading Extensions:  aosutils.lsx (always loaded in IDE)
  Consulting Source Files: 'program2.pro'
Type 'quit.' to end and [Ctrl]-C to stop user input.

?- subset([a], [a, b, c, d]).

yes
?- subset([a, c], [a, b, c, d]).
yes
?- subset([a, c, e], [a, b, c, d]).
no
?- subset(X, [a, b, c, d]).
X = [a] ;

X = [b] ;

X = [c] ;

X = [d] ;

X = [a, a] ;

X = [a, b] ;

X = [a, c] ;

X = [a, d] ;

X = [a, a, a] ;

X = [a, a, b] .
yes
?- quit.
```