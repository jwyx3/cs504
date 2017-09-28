#!/usr/bin/env python

import os
import json
import random

random.seed()
query_file = os.path.join(os.path.dirname(os.path.realpath(__file__)), '..', 'data', 'rawQuery3.txt')
budget_file = os.path.join(os.path.dirname(os.path.realpath(__file__)), '..', 'data', 'budget.txt')
with open(query_file, 'r') as ifh, open(budget_file, 'w') as ofh:
    for line in ifh.readlines():
        parts = line.strip().split(',')
        if len(parts) == 4:
            campaign = {'campaignId': int(parts[2].strip()), 'budget': float(parts[1].strip()) * random.randint(10, 10000)}
            ofh.write("%s\n" % json.dumps(campaign))

        
        
