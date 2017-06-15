# mergesort-group12
A Java implementation for MergeSort in external memory

This project implements multiway merge sort algorithm. Its src folder includes these following packages:

./src/generator: contains DataTestGenerator and IntegerGenerator to generate random numbers and data test.

./src/inoutstream: contains 4 ReadStream classes and 4 WriteStream classes to support reading/writing from/to files

./src/sort: contatins MultiwayMergeSort that implement the multiway merge sort algorithm.

./src/main: contains MainTest to test ReadStream/WriteStream/MultiwayMergeSort

./src/test: contains TestCorrectness where we show that the algorithm is correctly implemented.

./src/testdata: In case user run TestCorrectness to test the algorithm with different parameter N, M, d: the generated files: input file, splitted files, merged files will be stored here.

---------------------------------TestCorrectness---------------------------------

To use TestCorrectness, run it with different parameter N, M, d. Please note that because it tests the correctness by reading the result form the result file and compare that with the one that is sorted by an internal sorting algorithm, so N should be small enough. It is recommended that value of N is smaller than 20 mil.


---------------------------------Projec description and FAQ---------------------------------
Project intro and FAQ: http://cs.ulb.ac.be/public/teaching/infoh417/project
Project full description: http://cs.ulb.ac.be/public/_media/teaching/infoh417/mergesort-assignment.pdf

