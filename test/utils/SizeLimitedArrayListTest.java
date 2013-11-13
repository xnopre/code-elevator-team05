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

	@Test
	public void setContent_must_replace_current_content() {
		SizeLimitedArrayList<Integer> sizeLimitedArrayList = new SizeLimitedArrayList<Integer>(3);
		sizeLimitedArrayList.add(1);
		sizeLimitedArrayList.add(2);
		sizeLimitedArrayList.add(3);

		SizeLimitedArrayList<Integer> sizeLimitedArrayListOther = new SizeLimitedArrayList<Integer>(3);
		sizeLimitedArrayListOther.add(7);
		sizeLimitedArrayListOther.add(8);
		sizeLimitedArrayListOther.add(9);

		sizeLimitedArrayList.setContent(sizeLimitedArrayListOther);

		assertThat(sizeLimitedArrayList.toArray()).containsOnly(7, 8, 9);

	}

	@Test
	public void test_toString() {
		final SizeLimitedArrayList<String> list = new SizeLimitedArrayList<String>(9);
		list.add("a");
		list.add("b");
		assertThat(list.toString()).isEqualTo("SizeLimitedArrayList[maxSize=9,[a, b]]");
	}
}
