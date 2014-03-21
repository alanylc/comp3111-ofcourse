package ofcourse;

public interface ISerialize<T> {
	public String serialize();
	public T deserialize(String data);
}
