package behaviouralmodel;
import java.util.*;

/**
 * @author Keirron
 * Data storage for actions //Depreciated
 */

public class Blackboard {
	
	private HashMap<String, String> strings = new HashMap<String, String>();
	private HashMap<String, Integer> ints = new HashMap<String, Integer>();
	private HashMap<String, Vector2> vectors = new HashMap<String, Vector2>();
	
	public String GetString(String key)
	{
		if(strings.containsKey(key))
			return strings.get(key);
		return null;
	}
	
	public Integer GetInt(String key)
	{
		if(ints.containsKey(key))
			return ints.get(key);
		return null;
	}
	
	public Vector2 GetVector2(String key)
	{
		if(vectors.containsKey(key))
			return vectors.get(key);
		return null;
	}
	
	public void SetString(String key, String value)
	{
		if(key == null || value == null) return;
		strings.put(key, value);
	}
	
	public void SetInteger(String key, Integer value)
	{
		if(key == null || value == null) return;
		ints.put(key, value);
	}
	
	public void SetVector2(String key, Vector2 value)
	{
		if(key == null || value == null) return;
		vectors.put(key, value);
	}
}
