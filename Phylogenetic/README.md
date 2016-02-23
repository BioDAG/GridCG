 # Phylogenetic

This folder provides scripts needed to produce simple phylogenetic profiles,
i.e not based on the best bidirectional hit strategy.

Using this approach, every time there is blast match between the query sequence A and
a db sequence of an organism B, the respective element of A if gets incremented 
<b> without checking whether the inverse matching actually exists! </b>

Those profiles can be either boolean (each element is 1 if there is at least 1 match, 0 otherwise)
or numerical (each element is the number of matches found).
