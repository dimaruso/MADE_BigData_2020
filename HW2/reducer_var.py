import sys
import numpy as np

vars = []

for line in sys.stdin:
    try:
        res = line.split("\t")[1]
        if not (res == 'price'):
            vars.append(np.int(res))
    except:
        pass

sys.stdout.write(f'mean = {np.var(vars)}')
