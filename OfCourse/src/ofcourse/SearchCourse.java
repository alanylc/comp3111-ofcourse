package ofcourse;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

/**
 * This abstract class is the main interface for one course 
 * search function, and itself represents the result of the search 
 * by inheriting from Collection<Course>. Each additional search 
 * criterion can be "nested", as in decorator pattern, by using 
 * the constructor. Any class inheriting from this can include 
 * other parameters in their constructors, but must ask for the 
 * other Collection<Course> as the base of this new search, and call 
 * the super class constructor. This means other CourseSearch instance 
 * can be provided in terms of polymorphism.
 * Any class inheriting from this must also implement 
 * <tt>boolean checkCriteria(Course)</tt>, meaning that the new class 
 * is a new type of criteria.
 * @author Bob Lee
 *
 */
public abstract class SearchCourse implements Collection<Course> {
	
	public Collection<Course> prevPipe = null;
	
	public SearchCourse(Collection<Course> prevPipe) {
		this.prevPipe = prevPipe;
	}

	//public abstract void setCritieria();
	
	public abstract boolean checkCriteria(Course course);

	public Course get(int index) {
		if (index < 0) return null;
		int count = 0;
		Iterator<Course> i = this.iterator();
		while(i.hasNext()) {
			Course next = i.next();
			if (count == index) return next;
			count++;
		}
		return null;
	}
	
	@Override
	public boolean add(Course e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Course> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		Iterator<Course> i = this.iterator();
		while(i.hasNext()) {
			Object next = i.next();
			if (next == o) return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		Iterator<?> i = c.iterator();
		while(i.hasNext()) {
			Object next = i.next();
			if (!contains(next)) return false;
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		Iterator<Course> i = this.iterator();
		return !i.hasNext();
	}
	
	@Override
	public Iterator<Course> iterator() {
		return new CourseSearchIterator();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		int result = 0;
		Iterator<Course> i = this.iterator();
		while(i.hasNext()) {
			i.next();
			result++;
		}
		return result;
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size()];
		int pos = 0;
		Iterator<Course> i = this.iterator();
		while(i.hasNext()) {
			result[pos] = i.next();
			pos++;
		}
		return null;
	}

	@Override
	@Deprecated 
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("Use toArray(Course[]) instead.");
	}
	
	public Course[] toArray(Course[] a) {
		Course[] result = new Course[size()];
		int pos = 0;
		Iterator<Course> i = this.iterator();
		while(i.hasNext()) {
			result[pos] = i.next();
			pos++;
		}
		return null;
	}
	
	@Override
	public abstract String toString();
	
	/**
	 * This iterator implements peeking, by using NoSuchElementException.
	 * This is necessary because if not, it is unknown whether the result 
	 * of the previous search contains any more element that satisfies 
	 * current search criteria.
	 * 
	 * @author Bob Lee
	 *
	 */
	private class CourseSearchIterator implements Iterator<Course> {
		Iterator<Course> prev = null;
		private Course next = null;
		
		/**
		 * Initialize a new CourseSearchIterator. It must be based on the
		 * result of the previous/outer "pipe".
		 */
		public CourseSearchIterator() {
			if(prevPipe != null) {
				prev = prevPipe.iterator();
				//Error based peeking is easier to implement
				try {
					do {
						next = prev.next();
					} while (!checkCriteria(next));
				}
				catch (NoSuchElementException e) {
					next = null;
				}
				
			}
		}
		
		@Override
		public boolean hasNext() {
			return (next != null);
		}

		@Override
		public Course next() {
			Course toReturn = next;
			//Error based peeking is easier to implement
			try {
				do {
					next = prev.next();
				} while (!checkCriteria(next));
			}
			catch (NoSuchElementException e) {
				next = null;
			}
			if(toReturn != null) return toReturn;
			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
