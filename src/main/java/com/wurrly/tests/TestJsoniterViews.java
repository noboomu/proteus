/**
 * 
 */
package com.wurrly.tests;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.jsoniter.ReflectionDecoderFactory;
import com.jsoniter.annotation.JsoniterAnnotationSupport;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.jsoniter.output.ReflectionEncoderFactory;
import com.jsoniter.spi.Binding;
import com.jsoniter.spi.ClassDescriptor;
import com.jsoniter.spi.EmptyExtension;
import com.jsoniter.spi.Encoder;
import com.jsoniter.spi.JsoniterSpi;
import com.wurrly.models.User;

/**
 * @author jbauer
 *
 */
public class TestJsoniterViews
{

	/**
	 * 
	 */
	public TestJsoniterViews()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
		try
		{
			TestJsoniterViews v = new TestJsoniterViews();
			v.updateJsoniter();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public void updateJsoniter() throws Exception
	{
		JsoniterAnnotationSupport.enable();
		
		User user = new User();
		user.setId(234234L);
		user.setUsername("blahbalh");
		   System.out.println(JsonStream.serialize(user));

		ClassDescriptor  o = JsoniterSpi.getEncodingClassDescriptor(User.class,false);
		Encoder encoder = ReflectionEncoderFactory.create(User.class);
		 
        JsoniterSpi.registerTypeDecoder(User.class, ReflectionDecoderFactory.create(User.class));
        System.out.println(o.allBindings());
        
        
        Binding b = o.allEncoderBindings().get(0);
        
        System.out.println(b);

		 JsoniterSpi.registerExtension(new EmptyExtension() {
	            @Override
	            public void updateClassDescriptor(ClassDescriptor desc) {
	                for (Binding binding : desc.allBindings()) {
	                   System.out.println(JsonStream.serialize(binding));
	                }
	            }
	        });
		 
		 System.out.println(JsoniterSpi.getExtensions());
		 
		 JsoniterSpi.dump();
		 
		   JsoniterSpi.registerPropertyEncoder(User.class, "username", new Encoder() {
	            @Override
	            public void encode(Object obj, JsonStream stream) throws IOException {
	                String str = (String) obj;
	                stream.writeVal(str + " hahahaha");
	            }

	            @Override
	            public Any wrap(Object obj) {
	                throw new UnsupportedOperationException();
	            }
	        });
		   
		   ByteArrayOutputStream baos = new ByteArrayOutputStream();
		   JsonStream  stream = new JsonStream(baos, 4096);
		   stream.writeVal(user);
	        stream.close();
		   System.out.println(baos.toString());
//		 JsoniterSpi.registerExtension(new EmptyExtension() {
//	            @Override
//	            public Decoder createDecoder(String cacheKey, Type type) {
//	                if (type == User.class) {
//	                    return new Decoder() {
//	                        @Override
//	                        public Object decode(final JsonIterator iter1) throws IOException {
//	                            return new User() {{
//	                                date = new Date(iter1.readLong());
//	                            }};
//	                        }
//	                    };
//	                }
//	                return null;
//	            }
//	        });
	}

}
