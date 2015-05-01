## DISCLAIMER: THIS IS NOT MY CODE.
# Borrowed from Piazza

import string
import random

ofile = 'testAlphaLarge1'
N = 40000
cands = string.ascii_uppercase + string.ascii_lowercase + string.digits

def rs(l):
    return ''.join(random.choice(cands) for _ in range(l))

w2 = set()
for l in range(4,10):
    w2 |= set([rs(l) for _ in range(N)])

with open(ofile, 'w') as f:
    candarr = list(cands)
    random.shuffle(candarr)
    f.write(''.join(candarr) + '\n')
    for ln in w2:
        f.write(ln + '\n')