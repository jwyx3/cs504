# cs504-hw-3

# steps:

1. go to workspace
2. run: mvn clean install
3. run: java -jar <workspace>/target/weighted-random-sampling-1.0-SNAPSHOT-jar-with-dependencies.jar --weights=1,2,3 --samples=5

   sample output
```
weights: 1,2,3, samples: 5
CDF: [0.16666666666666666, 0.5, 1.0]
Result: [0, 2, 2, 0, 2]
Probabilities:
0: 40.00%
1: 0.00%
2: 60.00%
```