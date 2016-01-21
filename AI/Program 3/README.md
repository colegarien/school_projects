# Purpose

Implement, with detail comments, a simple graph traveral algorithm

# SAMPLE RUN

```
Interpreting project: Prolog Programs
  Loading Extensions:  aosutils.lsx (always loaded in IDE)
  Consulting Source Files: 'program3.pro'
Type 'quit.' to end and [Ctrl]-C to stop user input.

?- findPath(a, g).

{ a -> b -> c -> d -> g }

yes
?- findPath(a, b).

{ a -> b }

yes
?- findPath(a, a).

{ a -> b -> a }

yes
?- findPath(e, g).

{ e -> b -> c -> d -> g }

yes
?- quit.
```