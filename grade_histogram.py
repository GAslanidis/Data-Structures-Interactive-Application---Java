nGrades = [70, 40, 50,60, 80, 85, 78]
nCountA = 0
nCountNonA = 0
for nGrade in nGrades:
    if nGrade >= 70:
	    nCountA += 1
	else:
	    nCountNonA += 1
print(nCountA, nCountNonA)