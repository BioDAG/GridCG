<h1> Grid CG </h1>
<i>A scalable and modular <b>Grid</b> computing framework for <b>C</b>omparative <b>G</b>enomics  </i>


This projects contains a large-scale data analysis tool, aiming towards similarity detection between
protein sequences, employing the computational resources offered by [EGI](https://www.egi.eu/).

The functionality provided by the tool is currently addressing:

1. Optimization of data management (optimal distribution of data load across nodes).
2. Automatic submission of multiple jobs to the Grid worker nodes.
3. Automatic detection of job failures and resubmission to another node.
4. Visualization of the end results through extensible post processing libraries.
 
The similarity between a given pair of sequences is infered either by a high similarity score 
created by the [BLAST](http://blast.ncbi.nlm.nih.gov/Blast.cgi) tool, and/or by similar phylogenetic profiles constructed using a novel algorithm designed and implemented by the author.

The example folder provides a working scenario for the user to experiment with.
Details about this analysis can be found in the folder's specific README
