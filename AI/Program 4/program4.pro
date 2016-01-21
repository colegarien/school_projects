/* Cole Garien
 * AI, TR 4:15pm
 * Program 4
 */
 
/* Base case in which there are no
    elements in the given array */
countEm([], 0).
/* Get the number of elements in the
    tail of the list then add one to the
    returned value to account for the head
    of the list */
countEm([_|Tail], N) :- countEm(Tail, M), N is M+1.