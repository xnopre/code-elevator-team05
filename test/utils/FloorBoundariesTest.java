package utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

public class FloorBoundariesTest {

	private final FloorBoundaries boundaries = new FloorBoundaries(0, 6);

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
		Assert.assertTrue(boundaries.isAtFirstFloor(0));
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
		assertTrue(boundaries.isAtLastFloor(6));
	}

	@Test
	public void isAtMiddleFloor_must_return_false() {
		assertFalse(boundaries.isAtMiddelFloor(0));
		assertFalse(boundaries.isAtMiddelFloor(1));
		assertFalse(boundaries.isAtMiddelFloor(2));
		assertFalse(boundaries.isAtMiddelFloor(4));
		assertFalse(boundaries.isAtMiddelFloor(5));
		assertFalse(boundaries.isAtMiddelFloor(6));
	}

	@Test
	public void isAtMiddleFloor_must_return_true() {
		assertTrue(boundaries.isAtMiddelFloor(3));
	}

	@Test(expected = IllegalArgumentException.class)
	public void ensure_need_at_least_two_floors() {
		new FloorBoundaries(1, 1);
	}
}
