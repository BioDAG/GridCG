
#This script takes a non binary phylogenetic file, and makes it binary.
def binarize_line(line):
	"""Transform every non 0 dimension of the vector into 1"""
	return 0
	
def is_number(s):
    try:
        return float(s)
    except ValueError:
        return s
        	
r = open('phylo.out','r')
w = open('phylo_bin.out','w')
#skip header
for i in range(4):
	next(r)

for line in r:
	protein,vector,org_scores = line.split('\t')
	print("protein is: " + protein + "vector is: " + vector)
	binary_vector = vector.split()
	binary_vector = map(is_number,binary_vector)
	w.write(protein + '\t[' + ' '.join(vector) + ']\t' + str(org_scores) + '\n')
    
    


r.close()
w.close()
