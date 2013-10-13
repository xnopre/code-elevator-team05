package utils;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class SizeLimitedArrayListTest {

	@Test
	public void verify_normal_process() {
		SizeLimitedArrayList<Integer> sizeLimitedArrayList = new SizeLimitedArrayList<Integer>(3);
		sizeLimitedArrayList.add(1);
		sizeLimitedArrayList.add(2);
		assertThat(sizeLimitedArrayList.size()).isEqualTo(2);
		assertThat(sizeLimitedArrayList.toArray()).containsOnly(1, 2);
	}

	@Test
	public void verify_size_is_limited_to3_if_add_4_or_more_elements() {
		SizeLimitedArrayList<Integer> sizeLimitedArrayList = new SizeLimitedArrayList<Integer>(3);
		sizeLimitedArrayList.add(1);
		sizeLimitedArrayList.add(2);
		sizeLimitedArrayList.add(3);
		sizeLimitedArrayList.add(4);
		assertThat(sizeLimitedArrayList.size()).isEqualTo(3);
		assertThat(sizeLimitedArrayList.toArray()).containsOnly(2, 3, 4);
	}

}
