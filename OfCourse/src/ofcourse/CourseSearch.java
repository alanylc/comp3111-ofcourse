package ofcourse;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public abstract class CourseSearch implements Collection<Course> {
	
	public Collection<Course> prevPipe = null;
	
	public CourseSearch(Collection<Course> prevPipe) {
		this.prevPipe = prevPipe;
	}

	//public abstract void setCritieria();
	
	public abstract boolean checkCriteria(Course course);

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
	

	private class CourseSearchIterator implements Iterator<Course> {
		private int pos = -1;
		Iterator<Course> prev = null;
		
		public CourseSearchIterator() {
			if(prevPipe != null) {
				prev = prevPipe.iterator();
			}
		}
		
		@Override
		public boolean hasNext() {
			if (prev != null && prev.hasNext()) {
				return true;
			}
			return false;
		}

		@Override
		public Course next() {
			while (prev != null && prev.hasNext()) {
				Course next = prev.next();
				if (checkCriteria(next)) {
					pos += 1;
					return next;
				}
				else {
					continue;
				}
			}
			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
