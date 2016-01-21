/* Cole Garien
 * AI, TR 4:15pm
 * Program 5
 */
 
/* Base case in which the first list is empty
    so the result of appending would simply be
    the second list, i.e. List2 */
append([], List2, List2).
/* The first list is split into its head->H
    and its tail->T
   TheRest is the result of append T to the
    front of List2
   The returned list is the TheRest with H
    appended to the front 
   In Short: the result as follows:
     H+T+List2 where T+List2 is done 
     using another append rule */
append([H|T], List2, [H|TheRest]):- append(T, List2, TheRest).