package utils;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

public class FloorBoundariesTest {

	private final FloorBoundaries boundaries = new FloorBoundaries(2, 8);

	@Test(expected = IllegalArgumentException.class)
	public void test_first_floor_with_a_floor_out_of_boundary() {
		boundaries.isAtFirstFloor(99);
	}

	@Test
	public void test_first_floor_with_a_floor_inside_boundary() {
		assertFalse(boundaries.isAtFirstFloor(3));
	}

	@Test
	public void test_first_floor_at_first_floor() {
		Assert.assertTrue(boundaries.isAtFirstFloor(2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_last_floor_with_a_floor_out_of_boundary() {
		boundaries.isAtLastFloor(99);
	}

	@Test
	public void test_last_floor_with_a_floor_inside_boundary() {
		assertFalse(boundaries.isAtLastFloor(3));
	}

	@Test
	public void test_current_floor_is_the_last_floor() {
		assertTrue(boundaries.isAtLastFloor(8));
	}

	@Test
	public void calculateFloorsNumber_must_return_good_floor_number() {
		assertEquals(7, boundaries.calculateFloorsNumber());
	}

	@Test(expected = IllegalArgumentException.class)
	public void ensure_need_at_least_two_floors() {
		new FloorBoundaries(1, 1);
	}

	@Test
	public void test_toString() {
		assertEquals("FloorBoundaries[floorRange=[1‥9]]", new FloorBoundaries(1, 9).toString());
	}

	@Test
	public void test_equals() {
		final FloorBoundaries floorBoundaries = new FloorBoundaries(1, 9);
		assertThat(floorBoundaries).isEqualTo(floorBoundaries);
		assertThat(new FloorBoundaries(1, 9)).isEqualTo(new FloorBoundaries(1, 9));
		assertThat(new FloorBoundaries(1, 9)).isNotEqualTo(new FloorBoundaries(0, 9));
		assertThat(new FloorBoundaries(1, 9)).isNotEqualTo(new FloorBoundaries(1, 8));
	}

}
