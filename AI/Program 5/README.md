# Purpose

This program (and it comments) illustrate the creation of a function for appending two lists in Prolog

# SAMPLE RUN

```
Interpreting project: Prolog Programs
  Loading Extensions:  aosutils.lsx (always loaded in IDE)
  Consulting Source Files: 'program5.pro'
Type 'quit.' to end and [Ctrl]-C to stop user input.

?- append([a,b,c],[d],Result).

Result = [a, b, c, d] ;
no
?- append([a,b,c],X,[a,b,c,d,e]).
X = [d, e] ;
no
?- append(X,[d,e],[a,b,c,d,e]).
X = [a, b, c] ;
no
?- quit.
```