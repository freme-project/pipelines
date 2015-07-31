package eu.freme.eservices.pipelines;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * <p>Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public class GsonTester {
	private final Gson gson = new Gson();

	private class AClass {
		private String aString;
		private int anInt;
		private List<Integer> anIntList;

		public AClass() {
			Random r = new Random();
			long l = r.nextLong();
			aString = Long.toBinaryString(l);
			anInt = r.nextInt();
			anIntList = new ArrayList<>();

			for (int i = 0; i < 10; i++) {
				anIntList.add(r.nextInt());
			}
		}

		public String getaString() {
			return aString;
		}

		public int getAnInt() {
			return anInt;
		}

		public List<Integer> getAnIntList() {
			return anIntList;
		}
	}

	@Test
	public void testMultiThreading() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(1000);

		for (int i = 0; i < 1000000; i++) {
			executor.submit(new Runnable() {
				@Override
				public void run() {
					AClass aClass = new AClass();
					int anInt = aClass.getAnInt();
					String aString = aClass.getaString();
					List<Integer> anIntList = aClass.getAnIntList();

					// now serialize and deserialize
					String json = gson.toJson(aClass);
					AClass bClass = gson.fromJson(json, AClass.class);

					assertEquals(anInt, bClass.getAnInt());
					assertEquals(aString, bClass.getaString());
					assertEquals(anIntList, bClass.getAnIntList());
				}
			});
		}
		executor.shutdown();
		executor.awaitTermination(5, TimeUnit.MINUTES);
	}
}
