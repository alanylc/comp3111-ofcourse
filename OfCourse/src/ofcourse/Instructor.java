package ofcourse;

public class Instructor extends Ratable {
	public Instructor(String name) {
		super(name);
	}
	

	@Override
	public String toString() {
		return getName();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result +  ((this.getName() == null) ? 0 : this.getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instructor other = (Instructor) obj;
		if (!this.getName().equals(other.getName()))
			return false;
		return true;
	}
	
}
