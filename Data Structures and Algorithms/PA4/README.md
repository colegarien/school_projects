## Purpose

Create a table based on _input.txt_ using Double Hashing and Quadratic Probeing

## Compiling and Running

```
make
pa4 table_size r_value c1_value c2_value
```

note that _input.txt_ is a required file in same folder as the program (it is simply a list of values)

### Parameter Information

 * `table_size` - 
 * `r_value` - A Prime number smaller than the size of the table, used in Double Hashing
 * `c1_value` - used in calculating the jump size, used in Quadratic Probing
 * `c2_value` - used in calculating the jump size, used in Quadratic Probing
 * _note_ the jump size calculation for Quadratic Probing is as follows `(c1*i)+(c2*i*i)` where `i` is the initial insertion attempt position
 