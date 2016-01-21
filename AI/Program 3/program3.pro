/* Cole Garien
 * AI, TR 4:15pm
 * Program 3
 */

/*Is item a member of a list*/ 
member(Item, [Item | _]).
member(Item, [_|Others]) :- member(Item, Others).
/*append list 1 and 2 so result=list1,list2*/
append([], List2, List2).
append([H|T], List2, [H|TheRest]):- append(T, List2, TheRest).
/* print a supplied array */
writeList(List) :- nl, write('{ '), writeIt(List).
writeIt([Node | []]) :- write(Node), write(' }'), nl.
writeIt([Node | Others]):- write(Node), write(' -> '), writeIt(Others).
 
/* edges of graph 
  edge(a,b) ==> edge from a to b */
edge(a,b).
edge(b,a).
edge(b,c).
edge(b,e).
edge(c,b).
edge(c,d).
edge(c,e).
edge(c,f).
edge(d,c).
edge(d,f).
edge(d,g).
edge(e,b).
edge(e,c).
edge(e,f).
edge(f,c).
edge(f,d).
edge(f,e).
edge(g,d).

/* use path predicate to get an array (ThePath) of nodes that
    starts with Start and gives a path to Finish 
    then ThePath is displayed */
findPath(Start, Finish) :- path(Start, Finish, [Start], ThePath),
                           writeList(ThePath).
/* this is the base case inwhich there is an edge directly
    between the Start and Finish node*/
path(Start, Finish, SoFar, FinalPath) :- edge(Start, Finish),
                                         append(SoFar, [Finish], FinalPath).
/* this is when Start and Finish are not directly adjacent and
    intermidiate nodes must be traveled over.
   An edge is selected to a node adjacent to Start that has not been
     traveled to yet, this node Next is then append to the list of nodes
     traveled (SoFarAndNext)
   Then a path is found from Next to the Finish */
path(Start, Finish, SoFar, FinalPath) :- edge(Start, Next),
                                         not member(Next, SoFar),
                                        append(SoFar, [Next], SoFarAndNext),
                                       path(Next, Finish, SoFarAndNext, FinalPath).