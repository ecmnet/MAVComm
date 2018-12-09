package com.comino.parallel.function;

/**
 * An interface for defining an operation that can be applied to an object.
 *
 * @author Jonathon Hare (jsh2@ecs.soton.ac.uk)
 *
 * @param <T>
 *            the type of objects processed by the operation.
 */
public interface Operation<T> {
	/**
	 * Perform the operation on the given object.
	 *
	 * @param object
	 *            the object.
	 */
	public void perform(T object);
}