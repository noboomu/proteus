/**
 * 
 */
package com.wurrly.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jsoniter.annotation.JsoniterAnnotationSupport;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Encoder;
import com.jsoniter.spi.JsoniterSpi;
import com.jsoniter.spi.TypeLiteral;
import com.wurrly.tests.JsonViewTest.Views;

/**
 * @author jbauer
 *
 */
public class JsonViewTest
{

	public static ObjectMapper mapper = new ObjectMapper();
	ObjectWriter defaultWriter = mapper.writerWithView(Views.Default.class);
	ObjectWriter privateWriter = mapper.writerWithView(Views.Private.class);

	public static class Views
	{
		public static class Default
		{
			
		}
		
		public static class Private
		{
			
		}
	}
	
	public static Map<Type,Map<Class<?>,JsonViewType>> JsonViewTypeMap = new HashMap<>();

	public static class JsonViewType implements Type
	{
		private final Type type;
		private final Class<?> viewClass;
		
		public JsonViewType(Type type, Class<?> viewClass)
		{
			this.viewClass = viewClass;
			this.type = type;
			
			Map<Class<?>,JsonViewType> currentMap = JsonViewTypeMap.get(type);
			
			if(currentMap ==  null)
			{
				currentMap = new HashMap<>();
			}
			
			currentMap.put(viewClass, this);
			
			JsonViewTypeMap.put(type, currentMap);
			
		}
		
		public String getTypeName()
		{
			return this.type.getTypeName() + "[" + viewClass.getSimpleName() + "]";
		}
	}
	
 	
	public static class User
	{
	
		public Long id = 123L;
		public String username = "test";
		@JsonView(Views.Private.class)
		public String privateField = "privateField";
		@JsonView(Views.Default.class)
		public String defaultField = "defaultField";

	}
	
	  private ByteArrayOutputStream baos;
	    private JsonStream stream;

	    public void setUp() {
	        baos = new ByteArrayOutputStream();
	        stream = new JsonStream(baos, 4096);
	    }
	
	public static void main(String[] args) {
		try {
			JsoniterAnnotationSupport.enable();

			JsonViewTest t = new JsonViewTest();
			
			t.updateJsoniter();
			
			t.testJsoniter();
			
			t.testJackson();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private User user = new User();
	
	public JsonViewTest()
	{
		
		setUp();
		 
		
	}
	
	public void updateJsoniter()
	{
		System.out.println("updateJsoniter");

		try {
			
			Map<String,List<Field>> viewFields = new HashMap<>();
			
			Arrays.stream(User.class.getFields()).forEach( f -> {
				
				 JsonView js = f.getAnnotation(JsonView.class);
				 
				if( js == null )
				{
					List<Field> fieldList = viewFields.get("none");
					if(fieldList == null)
					{
						fieldList = new ArrayList<>();
					}
					fieldList.add(f);
					viewFields.put("none", fieldList);
				}
				else if( js.value()[0].equals(Views.Default.class) )
				{
					List<Field> fieldList = viewFields.get(Views.Default.class.getName());
					if(fieldList == null)
					{
						fieldList = new ArrayList<>();
					}
					fieldList.add(f);
					viewFields.put(Views.Default.class.getName(), fieldList);
					
					fieldList = viewFields.get(Views.Private.class.getName());
					viewFields.get(Views.Private.class.getName());
					if(fieldList == null)
					{
						fieldList = new ArrayList<>();
					}
					fieldList.add(f);
					viewFields.put(Views.Private.class.getName(), fieldList);
					
				}
				else if( js.value()[0].equals(Views.Private.class) )
				{
					List<Field> fieldList = viewFields.get(Views.Private.class.getName());
					if(fieldList == null)
					{
						fieldList = new ArrayList<>();
					}
					fieldList.add(f);
					viewFields.put(Views.Private.class.getName(), fieldList);
				} 
			});
			
			System.out.println("viewFields: " + viewFields);
			System.out.println("JsonViewTypeMap: " + JsonViewTypeMap);
			JsonViewType defaultUserType = new JsonViewType(User.class, Views.Default.class);
			JsonViewType privateUserType = new JsonViewType(User.class, Views.Private.class);
			
			TypeLiteral<User> defaultUserTypeLiteral = TypeLiteral.create(defaultUserType);
			TypeLiteral<User> privateUserTypeLiteral = TypeLiteral.create(privateUserType);

			System.out.println("defaultUserType: " + defaultUserType);
			System.out.println("privateUserType: " + privateUserType);
			System.out.println("JsonViewTypeMap: " + JsonViewTypeMap);

		    JsoniterSpi.registerTypeEncoder(User.class, new Encoder() {
	            @Override
	            public void encode(Object obj, JsonStream stream) throws IOException {
	                
	            	User user = (User)obj;
	            	stream.writeObjectStart();
	            	 stream.writeObjectField("id");
	                 stream.writeVal(user.id);
	                 stream.writeMore();
	                 stream.writeObjectField("username");
	                 stream.writeVal(user.username);
		            stream.writeObjectEnd();

	            }

	            @Override
	            public Any wrap(Object obj) {
	                return null;
	            }
	        });
		    
		    try {
				 // stream.writeVal(defaultUserTypeLiteral, user);
				  stream.writeVal(user);
			      stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("stream: " + baos.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		 
		
	}
	
	public void testJsoniter()
	{
		System.out.println("testJsoniter");

		try {
		 
				
				String json = defaultWriter.writeValueAsString(user);
				System.out.println("default:\n" + json);
				
				json = privateWriter.writeValueAsString(user);
				System.out.println("private:\n" + json);
				
			 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void testJackson()
	{
		System.out.println("testJackson");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
