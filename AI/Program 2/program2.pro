/* Cole Garien
 * AI, TR 4:15pm
 * Program 2
 */

/* is item a member of a given list */
member(Item, [Item | _]).
member(Item, [_ | Tail]) :- member(Item, Tail).

/* first list has only 1 item and it is in FullList*/
subset([Head|[]], FullList) :- member(Head, FullList).
/* head is a member and tail is also a subset of FullList*/
subset([Head|Tail], FullList) :- member(Head, FullList),
                                 subset(Tail, FullList).