<h1> Sample figures </h1>

This directory contains figures created to evaluate the system's performance.
We are particurarly interested in the framework's efficiency at a scale, for this reason
I test with all vs all analysis on large datasets. 

<i>There are 3 scenarios presented:</i> 

1. A relatively small analysis of 10 genomes.
2. An average scale analysis of 30 genomes.
3. A large scale analysis of 60 genomes.

Additionally I test the profile expansion by extending the 10 organisms with the 20 rest and 
evaluating it against the 30. We then do the same thing, extending the 30 genomes into 60.
The end results were in both cases the same as expected, however the timing was quite different.

Finally, I also provide diagrams manifesting the variance in compute power of the Grid's Nodes, and its
effect in the performance from the user's point of view.
