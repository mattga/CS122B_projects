Optimizations:
Inserts are being ran after every parsing of a document (this isn't really an optimization)
Auto commit turned off

The best optimization we can do is just parse XML into a csv and then run LOAD DATA INFILE. I think we either need to do this
or find at least one other optimization. We have to get parsing of the big file to around 5 minutes...

Big (Non-Optimized)
5+ hrs (Never finished)

Big (Inserts & manual commits)
Still running...


Small (Non-Optimized)
118860 ms

Small (Inserts every document)
51781 ms

Small (Inserts & manual commits)
3917 msi

RUNNING THE PARSER:
Open in Eclipse, and Compile and Run from there...

