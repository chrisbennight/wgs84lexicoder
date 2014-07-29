#WGS 84 Lexicoder test mockup
This is a quick mockup of a SFC based lexicoder for accumulo.  Intent here is just to feel out an interface, and run some quick performance tests.


##Stats
Some quick and dirty tests in the main method of the LexicoderWGS84 class.

The first line contains the number of bits per dimension, the time it took to decompose the majority of the SFC fully (no extra cells), and the number of resulting ranges.

The second line contains the average of the sum of all the latitude errors after running through the encoding:
``` 
Point -> encode -> Decode -> SumLatitudeErrors+= Point.Latitude - Decode.latitude
SumLatitude / # points
````

The third line contains the absolute error for each longitude set: 
``` 
Point -> encode -> Decode -> SumLongitudeErrors+= Math.abs(Point.longitude - Decode.longitude)
SumLongitudeErrors / #points
````

These errors are expected, and are just the result of storing a number with less bits than it takes to fully represent it.


As far as time, between 18 and 19 bits we pass the 1 second to decompose mark.  

```
----------------------------------------------------
2 bits took 0.030 seconds for  1 ranges
Actual lattitude error:         30.7720588
Worst case avg longitude error: 60.3973510
----------------------------------------------------
3 bits took 0.003 seconds for  3 ranges
Actual lattitude error:         12.8098739
Worst case avg longitude error: 25.8845790
----------------------------------------------------
4 bits took 0.002 seconds for  6 ranges
Actual lattitude error:         5.3602941
Worst case avg longitude error: 10.8079470
----------------------------------------------------
5 bits took 0.003 seconds for  13 ranges
Actual lattitude error:         2.8071632
Worst case avg longitude error: 5.6910916
----------------------------------------------------
6 bits took 0.012 seconds for  46 ranges
Actual lattitude error:         1.3392857
Worst case avg longitude error: 2.7625355
----------------------------------------------------
7 bits took 0.025 seconds for  73 ranges
Actual lattitude error:         0.6852131
Worst case avg longitude error: 1.3516191
----------------------------------------------------
8 bits took 0.074 seconds for  313 ranges
Actual lattitude error:         0.3153114
Worst case avg longitude error: 0.6264122
----------------------------------------------------
9 bits took 0.037 seconds for  419 ranges
Actual lattitude error:         0.1702976
Worst case avg longitude error: 0.3452521
----------------------------------------------------
10 bits took 0.040 seconds for  910 ranges
Actual lattitude error:         0.0850656
Worst case avg longitude error: 0.1724573
----------------------------------------------------
11 bits took 0.040 seconds for  1906 ranges
Actual lattitude error:         0.0438051
Worst case avg longitude error: 0.0861865
----------------------------------------------------
12 bits took 0.035 seconds for  6335 ranges
Actual lattitude error:         0.0144635
Worst case avg longitude error: 0.0343498
----------------------------------------------------
13 bits took 0.034 seconds for  7957 ranges
Actual lattitude error:         0.0090083
Worst case avg longitude error: 0.0215387
----------------------------------------------------
14 bits took 0.070 seconds for  18518 ranges
Actual lattitude error:         0.0056349
Worst case avg longitude error: 0.0117874
----------------------------------------------------
15 bits took 0.132 seconds for  33670 ranges
Actual lattitude error:         0.0027366
Worst case avg longitude error: 0.0055297
----------------------------------------------------
16 bits took 0.333 seconds for  83506 ranges
Actual lattitude error:         0.0012269
Worst case avg longitude error: 0.0024738
----------------------------------------------------
17 bits took 0.651 seconds for  167546 ranges
Actual lattitude error:         0.0006639
Worst case avg longitude error: 0.0013460
----------------------------------------------------
18 bits took 0.773 seconds for  198597 ranges
Actual lattitude error:         0.0003219
Worst case avg longitude error: 0.0006639
----------------------------------------------------
19 bits took 1.329 seconds for  335470 ranges
Actual lattitude error:         0.0001660
Worst case avg longitude error: 0.0003274
----------------------------------------------------
20 bits took 5.268 seconds for  1049209 ranges
Actual lattitude error:         0.0000767
Worst case avg longitude error: 0.0001523
----------------------------------------------------
21 bits took 6.735 seconds for  1433559 ranges
Actual lattitude error:         0.0000415
Worst case avg longitude error: 0.0000841
----------------------------------------------------
22 bits took 29.073 seconds for  5371949 ranges
Actual lattitude error:         0.0000207
Worst case avg longitude error: 0.0000421
----------------------------------------------------
23 bits took 44.222 seconds for  7681223 ranges
Actual lattitude error:         0.0000107
Worst case avg longitude error: 0.0000210
----------------------------------------------------
24 bits took 72.172 seconds for  11490434 ranges
Actual lattitude error:         0.0000035
Worst case avg longitude error: 0.0000084
----------------------------------------------------
```