package com.example.simpleflink;

import java.util.Arrays;
import java.util.List;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/***
 * 
 * @author dyfro
 * 
 * The simple application using stream methods: map, filter, keyBy, reduce
 * 
 * Method: applyMapAndFilterOnSocketStream
 * 
 * This method gets values from Integer stream and creates new KeyedStream that partitions 
 * states of positive and negative numbers and performs reduce on each partition.     
 * 
 * 
 * Method: applyKeyByAndReduceOnIntegerDataStreamSource
 * 
 * This method gets socketTextStream and performs map method to replace minus sign 
 * of negative numbers in input string to text "(negative)".
 * Input strings, that contains substring "00" are filtered. 
 * 
 */


public class SimpleFlinkApplication {

	private static final String TO_REPLACE = "-" ;
	private static final String REPLACEMENT = "(negative) " ;
	private static final String FILTER_SUBSTRING = "00" ;
	private static List<Integer> integerList;
	static {
		integerList = Arrays.asList(1,2,3,-3,4,-7);
	}

	
	public static void main(String[] args) {
		
		 StreamExecutionEnvironment env =
	                StreamExecutionEnvironment.getExecutionEnvironment();

		 applyKeyByAndReduceOnIntegerDataStreamSource(env, integerList);
		 
		 Thread socketWriterThread = new Thread( new SocketWriter() );

		 applyMapAndFilterOnSocketStream(env, socketWriterThread);

	}
	
	
	/***
	 * 
	 * @param env
	 * @param socketWriterThread
	 * 
	 */
	private static void applyMapAndFilterOnSocketStream(StreamExecutionEnvironment env, Thread socketWriterThread) {
	 	System.out.println("-------- apply map and filter to socket stream ------------- ");
		socketWriterThread.start();
		DataStreamSource<String> stringStreamSource = env.socketTextStream(SocketWriter.URL, SocketWriter.PORT, "\n");
	        stringStreamSource
		        .map(s -> s != null && s.startsWith(TO_REPLACE) ? s.replaceFirst(TO_REPLACE, REPLACEMENT) : s )
		        .filter(s -> s!= null && !s.contains(FILTER_SUBSTRING))
		        .print();
        try {
			env.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/***
	 * 
	 * @param env
	 * @param integerList
	 * 
	 */
	private static void applyKeyByAndReduceOnIntegerDataStreamSource(StreamExecutionEnvironment env,
			List<Integer> integerList) {

		System.out.println("------- apply reducing function to negative and positive numbers in integer data stream ------- ");
	        DataStreamSource<Integer> integerStream = env.fromCollection(integerList);
	        integerStream
	        	.keyBy( e -> e > 0 )
	        	.reduce( (v1, v2) -> v1 + v2)
	        	.print();
	        try {
				env.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
