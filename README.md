Interactive data analysis framework for sequence analysis on large compute systems

Development of a large-scale data analysis tool, aiming towards similarity detection between
protein sequences, employing the computational resources offered by EGI.


Eventually, the functionality provided by the tool is expected to address:

 -Optimization of data management (optimal distribution of data load across nodes).
 -Automatic submission of multiple jobs to the Grid worker nodes.
 -Automatic detection of job failures and resubmission to another node.
 -Visualization of end results (optional)
 
Each subdirectory contains source and documentation for a specific usage scenario.
For example Phylogenetic creates plain phylogenetic profiles while createBBH uses the
best bidirectional hit approach. Details about each scenario can be found in the 
respective README of each sub-directory.
