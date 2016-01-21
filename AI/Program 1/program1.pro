/* Cole Garien
 * AI, TR 4:15pm
 * Program 1
 */

/* Setting up parents
   X parentOf Y ==> parentOf(X, Y)*/
parentOf(al, bill).
parentOf(al, bob).
parentOf(al, brenda).
parentOf(alice, bill).
parentOf(alice, bob).
parentOf(alice, brenda).
parentOf(bill, cathy).
parentOf(betty, cathy).
parentOf(bob, calvin).
parentOf(becky, calvin).
parentOf(bart, charles).
parentOf(bart, candice).
parentOf(brenda, charles).
parentOf(brenda, candice).
parentOf(carl, dave).
parentOf(carl, debbie).
parentOf(cathy, dave).
parentOf(cathy, debbie).

/* setting up males*/
male(al).
male(bill).
male(bob).
male(bart).
male(carl).
male(calvin).
male(charles).
male(dave).

/* females are those that arent male*/
female(X) :- not male(X).

/* define wedded pairs of people*/
wed(al, alice).
wed(bill, betty).
wed(bob, becky).
wed(bart, brenda).
wed(carl, cathy).
wed(calvin, cindy).
/* X is wed to Y or vice versa*/
marriedTo(X, Y) :- wed(X,Y). 
marriedTo(X, Y) :- wed(Y,X).

/*X is male sibling to Y*/
brotherOf(X, Y) :- male(X),
                   parentOf(Z,X),
                   parentOf(Z,Y),
                   X\=Y.

/*X is female sibling to Y*/
sisterOf(X, Y) :- female(X),
                  parentOf(Z,X),
                  parentOf(Z,Y),
                  X\=Y.

/*X is female parent of Y*/
motherOf(X, Y) :- parentOf(X, Y),
                  female(X).
                  
/*X is male parent of Y*/
fatherOf(X, Y) :- parentOf(X, Y),
                  male(X).

/*X is parent or parent's parent of Y*/
ancestorOf(X, Y) :- parentOf(X, Y).
ancestorOf(X, Y) :- parentOf(Z,Y),
                    ancestorOf(X, Z).

/*Cousins via Uncle's, Great Uncle's, etc */
cousinOf(X, Y) :- ancestorOf(Z,X),
                  ancestorOf(W,Y),
                  brotherOf(Z, W).
/*Cousins via Aunt's, Great Aunt's, etc */
cousinOf(X, Y) :- ancestorOf(Z,X),
                  ancestorOf(W,Y),
                  sisterOf(Z, W).
/*Removed Cousins, ie Parent Z's lst cousin
  is X's lst cousin once removed*/
cousinOf(X, Y) :- ancestorOf(Z, X),
                  cousinOf(Z, Y).