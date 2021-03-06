package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazeluff.discord.nhlbot.utils.Utils;

@RunWith(PowerMockRunner.class)
public class UtilsTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UtilsTest.class);
	
	@Test
	public void getRandomShouldBeAbleToReturnAllValuesOfList() {
		LOGGER.info("getRandomShouldBeAbleToReturnAllValuesOfList");
		List<Integer> list = Arrays.asList(1, 2, 3);
		
		for(Integer i : list) {
			int retries = 0;
			while (i != Utils.getRandom(list) && retries < 500) {
				retries++;
			}
			if (retries == 500) {
				fail();
			}
		}
	}

	@Test
	public void sleepShouldSleepThread() {
		LOGGER.info("sleepShouldSleepThread");
		LocalDateTime start = LocalDateTime.now();
		Utils.sleep(1);
		LocalDateTime end = LocalDateTime.now();

		assertTrue(Duration.between(start, end).getNano() >= 1000);
	}

	@Test
	@PrepareForTest({ Utils.class, Thread.class })
	public void sleepShouldNotThrowExceptionWhenThreadSleepThrowsInterruptedException() throws InterruptedException {
		LOGGER.info("sleepShouldNotThrowExceptionWhenThreadSleepThrowsInterruptedException");
		mockStatic(Thread.class);
		doThrow(new InterruptedException()).when(Thread.class);
		Thread.sleep(anyLong());
		
		Utils.sleep(1);
	}

	@Test
	public void getFileNameShouldGetFileNameFromPath() {
		LOGGER.info("getFileNameShouldGetFileNameFromPath");
		assertEquals("filename.png", Utils.getFileName("a/b/c/filename.png"));
		assertEquals("filename.png", Utils.getFileName("/a/b/c/filename.png"));
	}

	@Test
	public void asSetShouldReturnOrderedSet() {
		LOGGER.info("asSetShouldReturnOrderedSet");
		
		Set<String> set = Utils.asSet("1", "2", "3");
		
		assertEquals(set.size(), 3);
		assertEquals("1", set.toArray()[0]);
		assertEquals("2", set.toArray()[1]);
		assertEquals("3", set.toArray()[2]);
	}
}
