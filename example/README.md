<h1> A working example </h1>

This folder contains a full working example of reasonable size for the user
to experiment with.

Run it using `./masterscript InputParams test`

This command will create a session folder named <b>test</b> and launch everything 
in a fully automated and thus transparent manner. After everything is finished
the user will be notified by an email. The expected time required varies
between 8 and 15 hours, depending on the Grid's load at the time of submitting.
The output will be made available in the <b>Phylo_output</b>  directories, 
each file contains the profiles for every sequence of the respective genome.
The user can check that those match perfectly the files inside the <b>PhyloOutputRef</b> directory. The procedure
will also return the intermediate results, namely the Blast output.


Results can be post processed to facilitate visualization using any of the
libraries provided in the javaTools/src/postProcessing package. For example 
the user can try to run the `PhyloCollapse` or the `phyloFilter` processes to 
reduce the output.  
