import sys

for line in sys.stdin:
    try:
        res = line.split(",")[-7]
        sys.stdout.write('%s\t%s\n'%('res', res))
    except:
        pass
