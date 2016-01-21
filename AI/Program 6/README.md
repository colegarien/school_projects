# Purpose

This program illustrates nesting in Prolog

# SAMPLE RUN

```
Interpreting project: Prolog Programs
  Loading Extensions:  aosutils.lsx (always loaded in IDE)
  Consulting Source Files: 'program6.pro'
Type 'quit.' to end and [Ctrl]-C to stop user input.

?- owns(Who, motorcycle(honda,_,_));owns(Who, car(honda,_,_));owns(Who,truck(honda,_,_)).

Who = henry ;

Who = george ;
no
?- owns(Who, car(_,_,_)).

Who = bill ;

Who = sue ;

Who = george ;
no
?- owns(henry, What).

What = motorcycle(honda, goldwing, 2010) ;
no
?- owns(Who, car(_,_,_));owns(Who, truck(_,_,_)).

Who = bill ;

Who = sue ;

Who = george ;

Who = betty ;
no
?- quit.
```