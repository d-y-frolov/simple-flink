# simple-flink

 * 
 * The simple application using stream methods: map, filter, keyBy, reduce
 * 
 * 
 * The first method gets values from Integer stream and creates new KeyedStream that partitions 
 * states of positive and negative numbers and performs reduce on each partition.     
 * 
 * 
 * The second method gets socketTextStream and performs map method to replace minus sign 
 * of negative numbers in input string to text "(negative)".
 * Input strings, that contains substring "00" are filtered. 
 * 
