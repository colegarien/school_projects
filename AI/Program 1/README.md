# Purpose

Implement a system for deriving the relationship among individuals

# SAMPLE RUN

```
Interpreting project: Prolog Programs
  Loading Extensions:  aosutils.lsx (always loaded in IDE)
  Consulting Source Files: 'program1.pro'
Type 'quit.' to end and [Ctrl]-C to stop user input.

?- marriedTo(al, X).

X = alice ;
no
?- marriedTo(alice, X).

X = al ;
no
?- brotherOf(bill, X).

X = bob ;

X = brenda ;

X = bob ;

X = brenda ;
no
?- sisterOf(bill, X).
no
?- sisterOf(brenda, X).
X = bill ;

X = bob ;

X = bill ;

X = bob ;
no
?- sisterOf(Y
, X).no
?- sisterOf(brenda
, X).
X = bill ;

X = bob ;

X = bill ;

X = bob ;
no

?- motherOf(X, bill).

X = alice ;
no
?- fatherOf(X, bill).

X = al ;
no
?- motherOf(al, X).
no
?- fatherOf(al, X).
X = bill ;

X = bob ;

X = brenda ;
no
?- male(al).

yes
?- male(alice).
no
?- female(al).
no

?- female(alice).
Yes

?- ancestorOf(dave, X).
no
?- ancestorOf(X
, dave).
X = carl ;

X = cathy ;

X = bill ;

X = betty ;

X = al ;

X = alice ;
no
?- cousinOf(cathy, X).

X = calvin ;

X = calvin ;

X = charles ;

X = charles ;

X = candice ;

X = candice ;
no
?- cousinOf(dave
, X).
X = calvin ;

X = calvin ;

X = charles ;

X = charles ;

X = candice ;

X = candice ;

X = calvin ;

X = calvin ;

X = charles ;

X = charles ;

X = candice ;

X = candice ;
no
?- cousinOf(X, dave
).
X = calvin ;

X = calvin ;

X = charles ;

X = charles ;

X = candice ;

X = candice ;
no
?- quit.
```