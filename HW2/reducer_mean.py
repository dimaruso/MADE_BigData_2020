import sys
import numpy as np

means = []

for line in sys.stdin:
    try:
        res = line.split("\t")[1]
        if not (res == 'price'):
            means.append(np.int(res))
    except:
        pass

sys.stdout.write(f'mean = {np.mean(means)}')
