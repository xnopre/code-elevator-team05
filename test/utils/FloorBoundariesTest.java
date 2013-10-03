package utils;

import org.junit.Assert;
import org.junit.Test;

public class FloorBoundariesTest {

	private final FloorBoundaries boundaries = new FloorBoundaries(0, 6);

	@Test(expected = IllegalArgumentException.class)
	public void test_first_floor_with_a_floor_out_of_boundary() {
		boundaries.atFirstFloor(99);
	}

	@Test
	public void test_first_floor_with_a_floor_inside_boundary() {
		Assert.assertFalse(boundaries.atFirstFloor(3));
	}

	@Test
	public void test_first_floor_at_first_floor() {
		Assert.assertTrue(boundaries.atFirstFloor(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_last_floor_with_a_floor_out_of_boundary() {
		boundaries.atLastFloor(99);
	}

	@Test
	public void test_last_floor_with_a_floor_inside_boundary() {
		Assert.assertFalse(boundaries.atLastFloor(3));
	}

	@Test
	public void test_current_floor_is_the_last_floor() {
		Assert.assertTrue(boundaries.atLastFloor(6));
	}

	@Test(expected = IllegalArgumentException.class)
	public void ensure_need_at_least_two_floors() {
		new FloorBoundaries(1, 1);
	}
}
