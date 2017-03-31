/**
 * 
 */
package com.wurrly.utilities;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
 

/**
 * @author jbauer
 *
 */
public class JsonMapper 
{
	
	private static ObjectMapper DEFAULT_MAPPER = null;
	 
	public static ObjectMapper MSGPACK_MAPPER = null;

 	private final static  ConcurrentHashMap<Object,ObjectWriter> WRITER_CACHE = new ConcurrentHashMap<>(); 

	public final static String EMPTY_JSON_ARRAY_STRING = JsonMapper.getInstance().createArrayNode().toString();

	private static Logger Logger = LoggerFactory.getLogger(JsonMapper.class.getCanonicalName());

	public static String toJSONString(Object object)
	{ 
			try
			{
				return  getInstance().writeValueAsString(object);
			} catch (Exception e)
			{
				Logger.error(e.getMessage(),e);
				return "";
			}
		
		
	}
	
	public static JsonNode toJSON(Object object)
	{ 
			try
			{
				return  getInstance().valueToTree(object);
			} catch (Exception e)
			{
				Logger.error(e.getMessage(),e);
				return null;
			}
		
		
	}
	
	public static String toPrettyJSON(Object object)
	{ 
			try
			{
				
				return  getPrettyWriter().writeValueAsString(object);
			} catch (Exception e)
			{
				Logger.error(e.getMessage(),e);
				return "";
			}
		
		
	}
	
	public static ObjectMapper getInstance()
	{
		if( DEFAULT_MAPPER == null )
		{
			DEFAULT_MAPPER = new ObjectMapper();
			DEFAULT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DEFAULT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
			DEFAULT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			DEFAULT_MAPPER.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH,true); 
			DEFAULT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			
			DEFAULT_MAPPER.registerModule(new AfterburnerModule());
			DEFAULT_MAPPER.registerModule(new Jdk8Module());
			
			 
 			//DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
			//defaultInstance.setDateFormat(format);
 			
		}
		return DEFAULT_MAPPER;
	}
	 
	 
	public static ObjectWriter getPrettyWriter()
	{
		ObjectWriter writer = WRITER_CACHE.get("pretty");
		if( writer == null )
		{
			writer = getInstance().writerWithDefaultPrettyPrinter();
			WRITER_CACHE.put("pretty", writer);
		}
		return writer;
	}
	
	public static ObjectMapper getMsgPackInstance()
	{
		if( MSGPACK_MAPPER == null )
		{
			MSGPACK_MAPPER = new ObjectMapper(new MessagePackFactory());
			MSGPACK_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); 
			MSGPACK_MAPPER.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH,true); 
			MSGPACK_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			MSGPACK_MAPPER.registerModule(new AfterburnerModule());

			//DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
			//defaultInstance.setDateFormat(format);
 			
		}
		return MSGPACK_MAPPER;
	}
	 

    public static  JsonNode toJSON(Class<?> view, Object object)
    {
    	ObjectWriter writer = WRITER_CACHE.get(view );
 
    	try
    	{
    		if( writer == null)
        	{ 
    	    	writer = getInstance().writerWithView(view);
    	    	WRITER_CACHE.put(view , writer);
        	}
    	 
    		final String jsonString = writer.writeValueAsString(object);
    		 
    		final JsonNode node = getInstance().readTree(jsonString);
    		 
    		return node;
    	} catch(Exception e)
    	{
    		Logger.error("\n" + e.getMessage() + "\nobject: " + object.getClass().getSimpleName(),e);
    		return null;
    	}
    	
    }
    
    public static  String toString(Class<?> view, Object object)
    {
    	ObjectWriter writer = WRITER_CACHE.get(view );
 
    	try
    	{
    		if( writer == null)
        	{ 
    	    	writer = getInstance().writerWithView(view);
    	    	WRITER_CACHE.put(view , writer);
        	}
    	 
    		return  writer.writeValueAsString(object);
    		  
    	} catch(Exception e)
    	{
    		Logger.error(e.getMessage() + "\nobject: " + object.getClass().getSimpleName(),e);
    		return null;
    	}
    	
    }
    
    
    public static ObjectNode wrap(String name, Class<?> view, Object object)
    {
    	ObjectWriter writer = WRITER_CACHE.get(view);
    	ObjectNode resultNode = getInstance().createObjectNode();

    	try
    	{
    		if( writer == null)
        	{ 
    	    	writer = getInstance().writerWithView(view);
    	    	WRITER_CACHE.put(view, writer);
        	}
    	
    		JsonNode objectNode = null;
        
        	  
        	if (object instanceof List<?>) {   
        	    List<?> list = (List<?>) object;
        	    if( list.size() < 1)
        	    {
        	    	 objectNode = getInstance().createArrayNode();
        	    }
        	    else
        	    {
        	    	 objectNode = getInstance().readTree(writer.writeValueAsString(object));
        	    }
        	 } else if (object instanceof Collection<?>) {
        	    Collection<?> col = (Collection<?>) object;
        	    if( col.size() < 1)
        	    {
        	    	 objectNode = getInstance().createArrayNode();
        	    }
        	    else
        	    {
        	    	 objectNode = getInstance().readTree(writer.writeValueAsString(object));
        	    }
        	 }
        	 else
        	 {
        		 objectNode = getInstance().readTree(writer.writeValueAsString(object));
        	 }
        	
        	 
        	
        	if( objectNode == null )
        	{
        		objectNode = getInstance().createObjectNode();
        	}
        	
    	  
    	
    	resultNode.set(name, objectNode);
    	
    	return resultNode;
    	
    	} catch(Exception e)
    	{
    		Logger.error(e.getMessage(),e);
    		return null;
    	}
    	
    }
     
    
   public static ObjectNode wrapNode(String name, JsonNode node)
   {
   		ObjectNode resultNode =  getInstance().createObjectNode();
   		resultNode.set(name, node);
   		return resultNode;
   }
     
    
    public static  JsonNode withJSONView(Class<?> view, Object object)
    {
    	ObjectWriter writer = WRITER_CACHE.get(view);
 
    	try
    	{
    		if( writer == null)
        	{ 
    	    	writer =  getInstance().writerWithView(view);
    	    	WRITER_CACHE.put(view, writer);
        	}
    	
     		
    		final String jsonString = writer.writeValueAsString(object);
    		
 
    		final JsonNode node =  getInstance().readTree(jsonString);
    		
 
    		return node;
    	} catch(Exception e)
    	{
    		Logger.error(e.getMessage() + "\nobject: " + object.getClass().getSimpleName(),e);
    		return null;
    	}
    	
    }
    
    public static ObjectNode wrapObjectNode(Class<?> view, Object object)
    {
  
     	ObjectWriter writer = WRITER_CACHE.get(view);
    	final ObjectNode resultNode =  getInstance().createObjectNode();

    	try
    	{
    		if( writer == null)
        	{ 
    	    	writer =  getInstance().writerWithView(view);
    	    	WRITER_CACHE.put(view, writer);
        	}
    	
    	final JsonNode objectNode =  getInstance().readTree(writer.writeValueAsString(object));
    	 
  
		
    	resultNode.set(object.getClass().getSimpleName().toLowerCase(), objectNode);
    	
    	return resultNode;
    	
    	} catch(Exception e)
    	{
    		Logger.error(e.getMessage(),e);
    		return null;
    	}
    	
    }
 
    /**
     *	Return an ObjectNode created from this object with the given view JSON rendition as 
     *	a property with the given name.
     *
     * @param  name  the name of the property
     * @param  view the view class
     * @return    the objectNode 
     */
    
    public static ObjectNode wrapObjectNode(String name, Class<?> view, Object object)
    {
    	ObjectWriter writer = WRITER_CACHE.get(view);
    	ObjectNode resultNode =  getInstance().createObjectNode();

    	try
    	{
    		if( writer == null)
        	{ 
    	    	writer =  getInstance().writerWithView(view);
    	    	WRITER_CACHE.put(view, writer);
        	}
    	
    	JsonNode objectNode =  getInstance().readTree(writer.writeValueAsString(object));
    	 
    	if( objectNode == null )
    	{
    		objectNode =  getInstance().createObjectNode();
    	}
    	
    	resultNode.set(name, objectNode);
    	
    	return resultNode;
    	
    	} catch(Exception e)
    	{
    		Logger.error(e.getMessage(),e);
    		return null;
    	}
    	
    }
    
    public static  ObjectNode wrapJSONView(String name, Class<?> view, Object object)
    {
    	ObjectWriter writer = WRITER_CACHE.get(view);
    	ObjectNode resultNode =  getInstance().createObjectNode();
    	JsonNode objectNode = null;
    	
    	try
    	{
    		if( writer == null)
        	{ 
    	    	writer =  getInstance().writerWithView(view);
    	    	WRITER_CACHE.put(view, writer);
        	}
    	 
    	if (object instanceof List<?>) {   
    	    List<?> list = (List<?>) object;
    	    if( list.size() < 1)
    	    {
    	    	 objectNode =  getInstance().createArrayNode();
    	    }
    	    else
    	    {
    	    	 objectNode =  getInstance().readTree(writer.writeValueAsString(object));
    	    }
    	 } else if (object instanceof Collection<?>) {
    	    Collection<?> col = (Collection<?>) object;
    	    if( col.size() < 1)
    	    {
    	    	 objectNode =  getInstance().createArrayNode();
    	    }
    	    else
    	    {
    	    	 objectNode =  getInstance().readTree(writer.writeValueAsString(object));
    	    }
    	 }
    	 else
    	 {
    		 objectNode =  getInstance().readTree(writer.writeValueAsString(object));
    	 }
    	
    	 
    	
    	if( objectNode == null )
    	{
    		objectNode =  getInstance().createObjectNode();
    	}
    	
    	resultNode.set(name, objectNode);
    	
    	return resultNode;
    	
    	} catch(Exception e)
    	{
    		Logger.error(e.getMessage(),e);
    		return null;
    	}
    	
    }
    
    public static  JsonNode stringCollectionToJSONArray(Collection<String> elements)
    {
  
    	final ArrayNode array = JsonMapper.getInstance().createArrayNode();
    	elements.stream().forEach( element -> {
			try{
					array.add( JsonMapper.getInstance().readTree(element) );
		    } 
			catch(Exception e)
			{
		    } 
		});
		
		return array;
    	
    }
    
    public static  Set<String> toStringSet(Collection<?> elements)
    {
  
    	final Set<String> stringSet = new HashSet<>();
    	
    	elements.stream().forEach( element -> {
			try
			{
					stringSet.add( JsonMapper.toJSONString(element) );
		    } 
			catch(Exception e)
			{
		    } 
		});
		
		return stringSet;
    	
    }
    
    public static  String toJSONString(Class<?> view, Object object)
    {
    	ObjectWriter writer = WRITER_CACHE.get(view);
 
    	try
    	{
    		if( writer == null)
        	{ 
    	    	writer =  getInstance().writerWithView(view);
    	    	WRITER_CACHE.put(view, writer);
        	}
    	
     	return writer.writeValueAsString(object);
     	
    	} catch(Exception e)
    	{
    		Logger.error(e.getMessage(),e);
    		return null;
    	}
    	
    }
    
    @SuppressWarnings("unchecked")
	public static <T,V> JsonNode nodeWithSerializer(Class<V> view, T object, JsonSerializer<T> serializer )
    { 
    	ObjectMapper mapper = new ObjectMapper();
  
	    mapper.setConfig(mapper.getSerializationConfig().withView(view));

		SimpleModule module = new SimpleModule();
		module.addSerializer((Class<? extends T>)object.getClass(),serializer); 
		
		mapper.registerModule(module);
		
    	try
    	{  
    		return mapper.valueToTree(object);
     	
    	} catch(Exception e)
    	{
    		Logger.error(e.getMessage(),e);
    		return null;
    	}
    	
    }
}
