<h1> A working example </h1>

This folder contains a full working example of reasonable size for the user
to experiment with.

Run it using `./masterscript InputParams test`

This command will create a session folder named <b>test</b> and launch everything 
in a fully automated and thus transparent manner. After everything is finished
the user will be notified by an email. The expected time required varies
between 8 and 15 hours, depending on the Grid's load at the time of submitting
The output will be made available in the <b>Blast_output</b> and <b>Phylo_output</b> 
directories. The user can check that those match perfectly the 
<b>BlastOutputRef</b> and <b>PhyloOutputRef</b> directories respectively.


Results can be post processed to facilitate visualization using any of the
libraries provided in the javaTools/src/postProcessing package. For example 
the user can try to run the `collapse` or the `phyloFilter` processes to 
reduce the output.  
