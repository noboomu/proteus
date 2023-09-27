/**
 * 
 */
package io.sinistral.proteus.openapi.jaxrs2;

/**
 * @author jbauer
 */

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.sinistral.proteus.annotations.Chain;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.wrappers.JsonViewWrapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.*;
import io.swagger.v3.jaxrs2.OperationParser;
import io.swagger.v3.jaxrs2.ReaderListener;
import io.swagger.v3.jaxrs2.ResolvedParameter;
import io.swagger.v3.jaxrs2.SecurityParser;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;
import io.swagger.v3.jaxrs2.util.ReaderUtils;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpServerExchange;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Application;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Reader extends io.swagger.v3.jaxrs2.Reader
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);

	public static final String DEFAULT_MEDIA_TYPE_VALUE = "*/*";
	public static final String DEFAULT_DESCRIPTION = "default response";

	protected OpenAPIConfiguration config;

	private Application application;
	private OpenAPI openAPI;
	private Components components;
	private Paths paths;
	private Set<Tag> openApiTags;

	private static final String GET_METHOD = "get";
	private static final String POST_METHOD = "post";
	private static final String PUT_METHOD = "put";
	private static final String DELETE_METHOD = "delete";
	private static final String PATCH_METHOD = "patch";
	private static final String TRACE_METHOD = "trace";
	private static final String HEAD_METHOD = "head";
	private static final String OPTIONS_METHOD = "options";

	private Schema stringSchema;

	public Reader()
	{
// Json.mapper().addMixIn(ServerRequest.class, ServerRequestMixIn.class);

		this.openAPI = new OpenAPI();
		paths = new Paths();
		openApiTags = new LinkedHashSet<>();
		components = new Components();
		stringSchema = new Schema();
		stringSchema.setType("string");

	}

	public Reader(OpenAPI openAPI)
	{
		this();

		setConfiguration(new SwaggerConfiguration().openAPI(openAPI));
	}

	public Reader(OpenAPIConfiguration openApiConfiguration)
	{
		this();
		setConfiguration(openApiConfiguration);
	}

	public OpenAPI getOpenAPI()
	{
		return openAPI;
	}

	/**
	 * Scans a single class for Swagger annotations - does not invoke
	 * ReaderListeners
	 */
	public OpenAPI read(Class<?> cls)
	{
		return read(cls, resolveApplicationPath(), null, false, null, null, new LinkedHashSet<String>(), new ArrayList<Parameter>(), new HashSet<Class<?>>());
	}

	/**
	 * Scans a set of classes for both ReaderListeners and OpenAPI annotations.
	 * All found listeners will
	 * be instantiated before any of the classes are scanned for OpenAPI
	 * annotations - so they can be invoked
	 * accordingly.
	 * @param classes
	 *            a set of classes to scan
	 * @return the generated OpenAPI definition
	 */
	public OpenAPI read(Set<Class<?>> classes)
	{
		Set<Class<?>> sortedClasses = new TreeSet<>(new Comparator<Class<?>>()
		{
			@Override
			public int compare(Class<?> class1, Class<?> class2)
			{
				if (class1.equals(class2))
				{
					return 0;
				}
				else if (class1.isAssignableFrom(class2))
				{
					return -1;
				}
				else if (class2.isAssignableFrom(class1))
				{
					return 1;
				}
				return class1.getName().compareTo(class2.getName());
			}
		});
		sortedClasses.addAll(classes);

		Map<Class<?>, ReaderListener> listeners = new HashMap<>();

		for (Class<?> cls : sortedClasses)
		{
			if (ReaderListener.class.isAssignableFrom(cls) && !listeners.containsKey(cls))
			{
				try
				{
					listeners.put(cls, (ReaderListener) cls.newInstance());
				} catch (Exception e)
				{
					LOGGER.error("Failed to create ReaderListener", e);
				}
			}
		}

		for (ReaderListener listener : listeners.values())
		{
			try
			{
				listener.beforeScan(this, openAPI);
			} catch (Exception e)
			{
				LOGGER.error("Unexpected error invoking beforeScan listener [" + listener.getClass().getName() + "]", e);
			}
		}

		for (Class<?> cls : sortedClasses)
		{
			read(cls, resolveApplicationPath(), null, false, null, null, new LinkedHashSet<String>(), new ArrayList<Parameter>(), new HashSet<Class<?>>());
		}

		for (ReaderListener listener : listeners.values())
		{
			try
			{
				listener.afterScan(this, openAPI);
			} catch (Exception e)
			{
				LOGGER.error("Unexpected error invoking afterScan listener [" + listener.getClass().getName() + "]", e);
			}
		}
		return openAPI;
	}

	public static OpenAPIConfiguration deepCopy(OpenAPIConfiguration config) {
		if (config == null) {
			return null;
		}
		try {
			return Json.mapper().readValue(Json.pretty(config), SwaggerConfiguration.class);
		} catch (Exception e) {
			LOGGER.error("Exception cloning config: " + e.getMessage(), e);
			return config;
		}
	}

	@Override
	public void setConfiguration(OpenAPIConfiguration openApiConfiguration)
	{
		if (openApiConfiguration != null)
		{
			this.config = deepCopy(openApiConfiguration);
			if (openApiConfiguration.getOpenAPI() != null)
			{
				this.openAPI = this.config.getOpenAPI();
				if (this.openAPI.getComponents() != null)
				{
					this.components = this.openAPI.getComponents();
				}
			}
		}
	}

	public OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources)
	{
		return read(classes);
	}

	protected String resolveApplicationPath()
	{
		if (application != null)
		{
			Class<?> applicationToScan = this.application.getClass();
			ApplicationPath applicationPath;
			// search up in the hierarchy until we find one with the annotation,
			// this is needed because for example Weld proxies will not have the
			// annotation and the right class will be the superClass
			while ((applicationPath = applicationToScan.getAnnotation(ApplicationPath.class)) == null && !applicationToScan.getSuperclass().equals(Application.class))
			{
				applicationToScan = applicationToScan.getSuperclass();
			}

			if (applicationPath != null)
			{
				if (StringUtils.isNotBlank(applicationPath.value()))
				{
					return applicationPath.value();
				}
			}
			// look for inner application, e.g. ResourceConfig
			try
			{
				Application innerApp = application;
				Method m = application.getClass().getMethod("getApplication", null);
				while (m != null)
				{
					Application retrievedApp = (Application) m.invoke(innerApp, null);
					if (retrievedApp == null)
					{
						break;
					}
					if (retrievedApp.getClass().equals(innerApp.getClass()))
					{
						break;
					}
					innerApp = retrievedApp;
					applicationPath = innerApp.getClass().getAnnotation(ApplicationPath.class);
					if (applicationPath != null)
					{
						if (StringUtils.isNotBlank(applicationPath.value()))
						{
							return applicationPath.value();
						}
					}
					m = innerApp.getClass().getMethod("getApplication", null);
				}
			} catch (NoSuchMethodException e)
			{
				// no inner application found
			} catch (Exception e)
			{
				// no inner application found
			}
		}
		return "";
	}

	public OpenAPI read(Class<?> cls,
						String parentPath,
						String parentMethod,
						boolean isSubresource,
						RequestBody parentRequestBody,
						ApiResponses parentResponses,
						Set<String> parentTags,
						List<Parameter> parentParameters,
						Set<Class<?>> scannedResources)
	{

		Hidden hidden = cls.getAnnotation(Hidden.class);
		// class path
		final jakarta.ws.rs.Path apiPath = ReflectionUtils.getAnnotation(cls, jakarta.ws.rs.Path.class);

		if (hidden != null)
		{ // || (apiPath == null && !isSubresource)) {
			return openAPI;
		}

		io.swagger.v3.oas.annotations.responses.ApiResponse[] classResponses = ReflectionUtils
				.getRepeatableAnnotationsArray(cls, io.swagger.v3.oas.annotations.responses.ApiResponse.class);

		List<io.swagger.v3.oas.annotations.security.SecurityScheme> apiSecurityScheme = ReflectionUtils
				.getRepeatableAnnotations(cls, io.swagger.v3.oas.annotations.security.SecurityScheme.class);
		List<io.swagger.v3.oas.annotations.security.SecurityRequirement> apiSecurityRequirements = ReflectionUtils
				.getRepeatableAnnotations(cls, io.swagger.v3.oas.annotations.security.SecurityRequirement.class);

		ExternalDocumentation apiExternalDocs = ReflectionUtils.getAnnotation(cls, ExternalDocumentation.class);
		io.swagger.v3.oas.annotations.tags.Tag[] apiTags = ReflectionUtils.getRepeatableAnnotationsArray(cls, io.swagger.v3.oas.annotations.tags.Tag.class);
		Server[] apiServers = ReflectionUtils.getRepeatableAnnotationsArray(cls, Server.class);

		Consumes classConsumes = ReflectionUtils.getAnnotation(cls, Consumes.class);
		Produces classProduces = ReflectionUtils.getAnnotation(cls, Produces.class);

		// OpenApiDefinition
		OpenAPIDefinition openAPIDefinition = ReflectionUtils.getAnnotation(cls, OpenAPIDefinition.class);

		if (openAPIDefinition != null)
		{

			// info
			AnnotationsUtils.getInfo(openAPIDefinition.info()).ifPresent(info -> openAPI.setInfo(info));

			// OpenApiDefinition security requirements
			SecurityParser
					.getSecurityRequirements(openAPIDefinition.security())
					.ifPresent(s -> openAPI.setSecurity(s));
			//
			// OpenApiDefinition external docs
			AnnotationsUtils
					.getExternalDocumentation(openAPIDefinition.externalDocs())
					.ifPresent(docs -> openAPI.setExternalDocs(docs));

			// OpenApiDefinition tags
			AnnotationsUtils
					.getTags(openAPIDefinition.tags(), false)
					.ifPresent(tags -> openApiTags.addAll(tags));

			// OpenApiDefinition servers
			AnnotationsUtils.getServers(openAPIDefinition.servers()).ifPresent(servers -> openAPI.setServers(servers));

			// OpenApiDefinition extensions
			if (openAPIDefinition.extensions().length > 0)
			{
				openAPI.setExtensions(AnnotationsUtils
						.getExtensions(openAPIDefinition.extensions()));
			}

		}

		// class security schemes
		if (apiSecurityScheme != null)
		{
			for (io.swagger.v3.oas.annotations.security.SecurityScheme securitySchemeAnnotation : apiSecurityScheme)
			{
				Optional<SecurityParser.SecuritySchemePair> securityScheme = SecurityParser.getSecurityScheme(securitySchemeAnnotation);
				if (securityScheme.isPresent())
				{
					Map<String, SecurityScheme> securitySchemeMap = new HashMap<>();
					if (StringUtils.isNotBlank(securityScheme.get().key))
					{
						securitySchemeMap.put(securityScheme.get().key, securityScheme.get().securityScheme);
						if (components.getSecuritySchemes() != null && components.getSecuritySchemes().size() != 0)
						{
							components.getSecuritySchemes().putAll(securitySchemeMap);
						}
						else
						{
							components.setSecuritySchemes(securitySchemeMap);
						}
					}
				}
			}
		}

		// class security requirements
		List<SecurityRequirement> classSecurityRequirements = new ArrayList<>();
		if (apiSecurityRequirements != null)
		{
			Optional<List<SecurityRequirement>> requirementsObject = SecurityParser.getSecurityRequirements(
																											apiSecurityRequirements.toArray(
																																			new io.swagger.v3.oas.annotations.security.SecurityRequirement[apiSecurityRequirements
																																					.size()]));
			if (requirementsObject.isPresent())
			{
				classSecurityRequirements = requirementsObject.get();
			}
		}

		// class tags, consider only name to add to class operations
		final Set<String> classTags = new LinkedHashSet<>();
		if (apiTags != null)
		{
			AnnotationsUtils
					.getTags(apiTags, false).ifPresent(tags -> tags
							.stream()
							.map(t -> t.getName())
							.forEach(t -> classTags.add(t)));
		}

		// parent tags
		if (isSubresource)
		{
			if (parentTags != null)
			{
				classTags.addAll(parentTags);
			}
		}

		// servers
		final List<io.swagger.v3.oas.models.servers.Server> classServers = new ArrayList<>();
		if (apiServers != null)
		{
			AnnotationsUtils.getServers(apiServers).ifPresent(servers -> classServers.addAll(servers));
		}

		// class external docs
		Optional<io.swagger.v3.oas.models.ExternalDocumentation> classExternalDocumentation = AnnotationsUtils.getExternalDocumentation(apiExternalDocs);

		JavaType classType = TypeFactory.defaultInstance().constructType(cls);

		BeanDescription bd = Json.mapper().getSerializationConfig().introspect(classType);

		final List<Parameter> globalParameters = new ArrayList<>();

		// look for constructor-level annotated properties
		globalParameters.addAll(ReaderUtils.collectConstructorParameters(cls, components, classConsumes, null));

		// look for field-level annotated properties
		globalParameters.addAll(ReaderUtils.collectFieldParameters(cls, components, classConsumes, null));

		// iterate class methods
		Method[] methods = cls.getMethods();
		for (Method method : methods)
		{
			if (isOperationHidden(method))
			{
				continue;
			}

			Class<?>[] parameterTypes = Arrays.stream(method.getParameterTypes()).filter(p -> !p.isAssignableFrom(ServerRequest.class)).toArray(Class<?>[]::new);

			AnnotatedMethod annotatedMethod = bd.findMethod(method.getName(), parameterTypes);

			Produces methodProduces = ReflectionUtils.getAnnotation(method, Produces.class);
			Consumes methodConsumes = ReflectionUtils.getAnnotation(method, Consumes.class);

			if (ReflectionUtils.isOverriddenMethod(method, cls))
			{
				continue;
			}

			jakarta.ws.rs.Path methodPath = ReflectionUtils.getAnnotation(method, jakarta.ws.rs.Path.class);

			String operationPath = ReaderUtils.getPath(apiPath, methodPath, parentPath, isSubresource);

			// skip if path is the same as parent, e.g. for @ApplicationPath
			// annotated application
			// extending resource config.
			if (ignoreOperationPath(operationPath, parentPath) && !isSubresource)
			{
				continue;
			}

			Map<String, String> regexMap = new LinkedHashMap<>();
			operationPath = PathUtils.parsePath(operationPath, regexMap);
			if (operationPath != null)
			{
				if (config != null && ReaderUtils.isIgnored(operationPath, config))
				{
					continue;
				}

				final Class<?> subResource = getSubResourceWithJaxRsSubresourceLocatorSpecs(method);

				String httpMethod = ReaderUtils.extractOperationMethod(method, OpenAPIExtensions.chain());
				httpMethod = (httpMethod == null && isSubresource) ? parentMethod : httpMethod;

				if (StringUtils.isBlank(httpMethod) && subResource == null)
				{
					continue;
				}
				else if (StringUtils.isBlank(httpMethod) && subResource != null)
				{
					Type returnType = method.getGenericReturnType();
					if (shouldIgnoreClass(returnType.getTypeName()) && !returnType.equals(subResource))
					{
						continue;
					}
				}

				io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.v3.oas.annotations.Operation.class);
				JsonView jsonViewAnnotation = ReflectionUtils.getAnnotation(method, JsonView.class);
				if (apiOperation != null && apiOperation.ignoreJsonView())
				{
					jsonViewAnnotation = null;
				}

				Operation operation = parseMethod(
													method,
													globalParameters,
													methodProduces,
													classProduces,
													methodConsumes,
													classConsumes,
													classSecurityRequirements,
													classExternalDocumentation,
													classTags,
													classServers,
													isSubresource,
													parentRequestBody,
													parentResponses,
													jsonViewAnnotation,
													classResponses);
				if (operation != null)
				{
					// LOGGER.debug("operation is not null");

					List<Parameter> operationParameters = new ArrayList<>();
					List<Parameter> formParameters = new ArrayList<>();
					Annotation[][] paramAnnotations = getParameterAnnotations(method);
					if (annotatedMethod == null)
					{ // annotatedMethod not null only when method with 0-2
						// parameters
						Type[] genericParameterTypes = method.getGenericParameterTypes();

						genericParameterTypes = Arrays.stream(genericParameterTypes).filter(t -> !t.getTypeName().contains("ServerRequest")).toArray(Type[]::new);
//
// for( Type t : genericParameterTypes )
// {
// LOGGER.warn("Generic parameter type: " + t);
// }
//
// LOGGER.warn("paramAnnotations length: " + paramAnnotations.length + "
// genericParameterTypes length: " + genericParameterTypes.length);

						for (int i = 0; i < genericParameterTypes.length; i++)
						{
							final Type type = TypeFactory.defaultInstance().constructType(genericParameterTypes[i], cls);
							io.swagger.v3.oas.annotations.Parameter paramAnnotation = AnnotationsUtils
									.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class, paramAnnotations[i]);

							Type paramType = ParameterProcessor.getParameterType(paramAnnotation, true);

							if (paramType == null)
							{
								paramType = type;
							}
							else
							{
								if (!(paramType instanceof Class))
								{
									paramType = type;
								}
							}

							boolean isOptional = isOptionalType(TypeFactory.defaultInstance().constructType(paramType));

							ResolvedParameter resolvedParameter = getParameters(
																				paramType, Arrays.asList(paramAnnotations[i]), operation, classConsumes, methodConsumes,
																				jsonViewAnnotation);

							operationParameters.addAll(resolvedParameter.parameters);

							resolvedParameter.formParameters.stream().forEach(fp -> fp.setRequired(!isOptional));

							formParameters.addAll(resolvedParameter.formParameters);

							if (resolvedParameter.requestBody != null)
							{
								processRequestBody(
													resolvedParameter.requestBody,
													operation,
													methodConsumes,
													classConsumes,
													operationParameters,
													paramAnnotations[i],
													type,
													jsonViewAnnotation);
							}
						}
					}
					else
					{
						for (int i = 0; i < annotatedMethod.getParameterCount(); i++)
						{
							AnnotatedParameter param = annotatedMethod.getParameter(i);

							final Type type = TypeFactory.defaultInstance().constructType(param.getParameterType(), cls);

							io.swagger.v3.oas.annotations.Parameter paramAnnotation = AnnotationsUtils
									.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class, paramAnnotations[i]);
							Type paramType = ParameterProcessor.getParameterType(paramAnnotation, true);

							if (paramType == null)
							{
								paramType = type;
							}
							else
							{
								if (!(paramType instanceof Class))
								{
									paramType = type;
								}
							}

							boolean isOptional = isOptionalType(TypeFactory.defaultInstance().constructType(paramType));

							ResolvedParameter resolvedParameter = getParameters(
																				paramType, Arrays.asList(paramAnnotations[i]), operation, classConsumes, methodConsumes,
																				jsonViewAnnotation);

							operationParameters.addAll(resolvedParameter.parameters);

							resolvedParameter.formParameters.stream().forEach(fp -> fp.setRequired(!isOptional));

							formParameters.addAll(resolvedParameter.formParameters);

							if (resolvedParameter.requestBody != null)
							{
								processRequestBody(
													resolvedParameter.requestBody,
													operation,
													methodConsumes,
													classConsumes,
													operationParameters,
													paramAnnotations[i],
													type,
													jsonViewAnnotation);
							}
						}
					}
					// if we have form parameters, need to merge them into
					// single schema and use as request body..
					if (formParameters.size() > 0)
					{
						Schema mergedSchema = new ObjectSchema();

						boolean isRequired = false;

						for (Parameter formParam : formParameters)
						{
							if (formParam.getRequired() != null && formParam.getRequired())
							{
								isRequired = true;
							}

							mergedSchema.addProperties(formParam.getName(), formParam.getSchema());
						}

						Parameter merged = new Parameter().schema(mergedSchema);

						merged.setRequired(isRequired);

						processRequestBody(
											merged,
											operation,
											methodConsumes,
											classConsumes,
											operationParameters,
											new Annotation[0],
											null,
											jsonViewAnnotation);

					}
					if (operationParameters.size() > 0)
					{
						for (Parameter operationParameter : operationParameters)
						{
							operation.addParametersItem(operationParameter);
						}
					}

					// if subresource, merge parent parameters
					if (parentParameters != null)
					{
						for (Parameter parentParameter : parentParameters)
						{
							operation.addParametersItem(parentParameter);
						}
					}

					boolean hasJsonWrapper = false;

					if(method.isAnnotationPresent(Chain.class))
					{
						Chain chainAnnotation = method.getAnnotation(Chain.class);

						Class<? extends HandlerWrapper>[] wrappers = chainAnnotation.value();

						for(Class<? extends HandlerWrapper> wrapper : wrappers)
						{
							if(wrapper.equals(JsonViewWrapper.class))
							{
								LOGGER.debug("Found json wrapper class on method");
								hasJsonWrapper = true;
							}
						}
					} else 	if(cls.isAnnotationPresent(Chain.class))
					{
						Chain chainAnnotation = cls.getAnnotation(Chain.class);

						Class<? extends HandlerWrapper>[] wrappers = chainAnnotation.value();

						for(Class<? extends HandlerWrapper> wrapper : wrappers)
						{
							if(wrapper.equals(JsonViewWrapper.class))
							{
								LOGGER.debug("Found json wrapper class on class");
								hasJsonWrapper = true;
							}
						}
					}

					LOGGER.debug("hasJsonWrapper");

					if(hasJsonWrapper && config.getUserDefinedOptions().containsKey("jsonViewQueryParameterName"))
					{
						Parameter contextParameter = new Parameter();
						contextParameter.description("JsonView class")
								.allowEmptyValue(true)
								.required(false)
								.name(config.getUserDefinedOptions().get("jsonViewQueryParameterName").toString())
								.in("query")
								.schema(stringSchema);

						operation.addParametersItem(contextParameter);
					}

					if (subResource != null && !scannedResources.contains(subResource))
					{
						scannedResources.add(subResource);
						read(
								subResource, operationPath, httpMethod, true, operation.getRequestBody(), operation.getResponses(), classTags, operation.getParameters(),
								scannedResources);
						// remove the sub resource so that it can visit it later
						// in another path
						// but we have a room for optimization in the future to
						// reuse the scanned result
						// by caching the scanned resources in the reader
						// instance to avoid actual scanning
						// the the resources again
						scannedResources.remove(subResource);
						// don't proceed with root resource operation, as it's
						// handled by subresource
						continue;
					}

					final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
					if (chain.hasNext())
					{
						final OpenAPIExtension extension = chain.next();
						extension.decorateOperation(operation, method, chain);
					}

					PathItem pathItemObject;

					if (openAPI.getPaths() != null && openAPI.getPaths().get(operationPath) != null)
					{
						pathItemObject = openAPI.getPaths().get(operationPath);
					}
					else
					{
						pathItemObject = new PathItem();
					}

					if (StringUtils.isBlank(httpMethod))
					{
						continue;
					}

					setPathItemOperation(pathItemObject, httpMethod, operation);

					paths.addPathItem(operationPath, pathItemObject);
					if (openAPI.getPaths() != null)
					{
						this.paths.putAll(openAPI.getPaths());
					}

					openAPI.setPaths(this.paths);

					LOGGER.debug("Completed paths");

				}
			}
		}

		// if no components object is defined in openApi instance passed by
		// client, set openAPI.components to resolved components (if not empty)
		if (!isEmptyComponents(components) && openAPI.getComponents() == null)
		{
			openAPI.setComponents(components);
		}

		// add tags from class to definition tags
		AnnotationsUtils
				.getTags(apiTags, true).ifPresent(tags -> openApiTags.addAll(tags));

		if (!openApiTags.isEmpty())
		{
			Set<Tag> tagsSet = new LinkedHashSet<>();
			if (openAPI.getTags() != null)
			{
				for (Tag tag : openAPI.getTags())
				{
					if (tagsSet.stream().noneMatch(t -> t.getName().equals(tag.getName())))
					{
						tagsSet.add(tag);
					}
				}
			}
			for (Tag tag : openApiTags)
			{
				if (tagsSet.stream().noneMatch(t -> t.getName().equals(tag.getName())))
				{
					tagsSet.add(tag);
				}
			}
			openAPI.setTags(new ArrayList<>(tagsSet));
		}

		return openAPI;
	}

	public boolean isOptionalType(JavaType propType)
	{
		return Arrays.asList("com.google.common.base.Optional", "java.util.Optional")
				.contains(propType.getRawClass().getCanonicalName());
	}

	public static Annotation[][] getParameterAnnotations(Method method)
	{

		Annotation[][] methodAnnotations = method.getParameterAnnotations();

// LOGGER.warn("methodAnnotations length at start: " +
// methodAnnotations.length);

		java.lang.reflect.Parameter[] params = method.getParameters();

		List<Integer> filteredParameterIndices = new ArrayList<>();

		for (int i = 0; i < params.length; i++)
		{
			Annotation[] paramAnnotations = methodAnnotations[i];

			if (!params[i].getType().isAssignableFrom(ServerRequest.class) && !params[i].getType().getName().startsWith("io.undertow"))
			{
				// String annotationStrings =
				// Arrays.stream(paramAnnotations).map(a ->
				// a.annotationType().getName()).collect(Collectors.joining("
				// "));

// LOGGER.debug("\nparameter: " + params[i] + " | name: " + params[i].getName()
// + " type: " + params[i].getType() + " -> " + annotationStrings);

				if (paramAnnotations.length == 0)
				{
					final String parameterName = params[i].getName();

					// LOGGER.debug("creating query parameter for " +
					// parameterName);

					QueryParam queryParam = new QueryParam()
					{

						@Override
						public String value()
						{
							return parameterName;
						}

						@Override
						public Class<? extends Annotation> annotationType()
						{
							return QueryParam.class;
						}
					};

					methodAnnotations[i] = new Annotation[] { queryParam };
				}
			}
			else
			{
				filteredParameterIndices.add(i);
			}
		}

		ArrayList<Annotation[]> annotations = Arrays.stream(methodAnnotations).collect(Collectors.toCollection(ArrayList::new));

		for (int index : filteredParameterIndices)
		{
			annotations.remove(index);
		}

		methodAnnotations = annotations.stream().toArray(Annotation[][]::new);

		Method overriddenmethod = ReflectionUtils.getOverriddenMethod(method);

		if (overriddenmethod != null)
		{
			Annotation[][] overriddenAnnotations = overriddenmethod
					.getParameterAnnotations();

			for (int i = 0; i < methodAnnotations.length; i++)
			{
				List<Type> types = new ArrayList<>();
				for (int j = 0; j < methodAnnotations[i].length; j++)
				{
					types.add(methodAnnotations[i][j].annotationType());
				}
				for (int j = 0; j < overriddenAnnotations[i].length; j++)
				{
					if (!types.contains(overriddenAnnotations[i][j]
							.annotationType()))
					{
						methodAnnotations[i] = ArrayUtils.add(
																methodAnnotations[i],
																overriddenAnnotations[i][j]);
					}
				}

			}
		}
		return methodAnnotations;
	}

	protected Content processContent(Content content, Schema schema, Consumes methodConsumes, Consumes classConsumes)
	{
		if (content == null)
		{
			content = new Content();
		}
		if (methodConsumes != null)
		{
			for (String value : methodConsumes.value())
			{
				setMediaTypeToContent(schema, content, value);
			}
		}
		else if (classConsumes != null)
		{
			for (String value : classConsumes.value())
			{
				setMediaTypeToContent(schema, content, value);
			}
		}
		else
		{
			setMediaTypeToContent(schema, content, DEFAULT_MEDIA_TYPE_VALUE);
		}
		return content;
	}

	protected void processRequestBody(	Parameter requestBodyParameter, Operation operation,
										Consumes methodConsumes, Consumes classConsumes,
										List<Parameter> operationParameters,
										Annotation[] paramAnnotations, Type type,
										JsonView jsonViewAnnotation)
	{

		boolean isOptional = !(requestBodyParameter.getRequired() != null ? requestBodyParameter.getRequired() : true);

		if (type != null && !isOptional)
		{
			JavaType classType = TypeFactory.defaultInstance().constructType(type);

			if (classType != null)
			{
				isOptional = isOptionalType(classType);
				type = classType;
			}
		}

		io.swagger.v3.oas.annotations.parameters.RequestBody requestBodyAnnotation = getRequestBody(Arrays.asList(paramAnnotations));
		if (requestBodyAnnotation != null)
		{
			Optional<RequestBody> optionalRequestBody = OperationParser.getRequestBody(requestBodyAnnotation, classConsumes, methodConsumes, components, jsonViewAnnotation);
			if (optionalRequestBody.isPresent())
			{
				RequestBody requestBody = optionalRequestBody.get();
				if (StringUtils.isBlank(requestBody.get$ref()) &&
						(requestBody.getContent() == null || requestBody.getContent().isEmpty()))
				{
					if (requestBodyParameter.getSchema() != null)
					{
						Content content = processContent(requestBody.getContent(), requestBodyParameter.getSchema(), methodConsumes, classConsumes);
						requestBody.setContent(content);
					}
				}
				else if (StringUtils.isBlank(requestBody.get$ref()) &&
						requestBody.getContent() != null &&
						!requestBody.getContent().isEmpty())
				{
					if (requestBodyParameter.getSchema() != null)
					{
						for (MediaType mediaType : requestBody.getContent().values())
						{
							if (mediaType.getSchema() == null)
							{
								if (requestBodyParameter.getSchema() == null)
								{
									mediaType.setSchema(new Schema());
								}
								else
								{
									mediaType.setSchema(requestBodyParameter.getSchema());
								}
							}
							if (StringUtils.isBlank(mediaType.getSchema().getType()))
							{
								mediaType.getSchema().setType(requestBodyParameter.getSchema().getType());
							}
						}
					}
				}
				requestBody.setRequired(!isOptional);
				operation.setRequestBody(requestBody);
			}
		}
		else
		{
			if (operation.getRequestBody() == null)
			{
				boolean isRequestBodyEmpty = true;
				RequestBody requestBody = new RequestBody();
				if (StringUtils.isNotBlank(requestBodyParameter.get$ref()))
				{
					requestBody.set$ref(requestBodyParameter.get$ref());
					isRequestBodyEmpty = false;
				}
				if (StringUtils.isNotBlank(requestBodyParameter.getDescription()))
				{
					requestBody.setDescription(requestBodyParameter.getDescription());
					isRequestBodyEmpty = false;
				}
				if (Boolean.TRUE.equals(requestBodyParameter.getRequired()))
				{
					requestBody.setRequired(requestBodyParameter.getRequired());
					isRequestBodyEmpty = false;
				}

				if (requestBodyParameter.getSchema() != null)
				{
					Content content = processContent(null, requestBodyParameter.getSchema(), methodConsumes, classConsumes);
					requestBody.setContent(content);
					isRequestBodyEmpty = false;
				}
				if (!isRequestBodyEmpty)
				{
					// requestBody.setExtensions(extensions);
					requestBody.setRequired(!isOptional);
					operation.setRequestBody(requestBody);
				}
			}
		}
	}

	private io.swagger.v3.oas.annotations.parameters.RequestBody getRequestBody(List<Annotation> annotations)
	{
		if (annotations == null)
		{
			return null;
		}
		for (Annotation a : annotations)
		{
			if (a instanceof io.swagger.v3.oas.annotations.parameters.RequestBody)
			{
				return (io.swagger.v3.oas.annotations.parameters.RequestBody) a;
			}
		}
		return null;
	}

	private void setMediaTypeToContent(Schema schema, Content content, String value)
	{
		MediaType mediaTypeObject = new MediaType();
		mediaTypeObject.setSchema(schema);
		content.addMediaType(value, mediaTypeObject);
	}

	public Operation parseMethod(
									Method method,
									List<Parameter> globalParameters,
									JsonView jsonViewAnnotation)
	{
		JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
		return parseMethod(
							classType.getClass(),
							method,
							globalParameters,
							null,
							null,
							null,
							null,
							new ArrayList<>(),
							Optional.empty(),
							new HashSet<>(),
							new ArrayList<>(),
							false,
							null,
							null,
							jsonViewAnnotation,
							null);
	}

	public Operation parseMethod(
									Method method,
									List<Parameter> globalParameters,
									Produces methodProduces,
									Produces classProduces,
									Consumes methodConsumes,
									Consumes classConsumes,
									List<SecurityRequirement> classSecurityRequirements,
									Optional<io.swagger.v3.oas.models.ExternalDocumentation> classExternalDocs,
									Set<String> classTags,
									List<io.swagger.v3.oas.models.servers.Server> classServers,
									boolean isSubresource,
									RequestBody parentRequestBody,
									ApiResponses parentResponses,
									JsonView jsonViewAnnotation,
									io.swagger.v3.oas.annotations.responses.ApiResponse[] classResponses)
	{
		JavaType classType = TypeFactory.defaultInstance().constructType(method.getDeclaringClass());
		return parseMethod(
							classType.getClass(),
							method,
							globalParameters,
							methodProduces,
							classProduces,
							methodConsumes,
							classConsumes,
							classSecurityRequirements,
							classExternalDocs,
							classTags,
							classServers,
							isSubresource,
							parentRequestBody,
							parentResponses,
							jsonViewAnnotation,
							classResponses);
	}

	private Operation parseMethod(
									Class<?> cls,
									Method method,
									List<Parameter> globalParameters,
									Produces methodProduces,
									Produces classProduces,
									Consumes methodConsumes,
									Consumes classConsumes,
									List<SecurityRequirement> classSecurityRequirements,
									Optional<io.swagger.v3.oas.models.ExternalDocumentation> classExternalDocs,
									Set<String> classTags,
									List<io.swagger.v3.oas.models.servers.Server> classServers,
									boolean isSubresource,
									RequestBody parentRequestBody,
									ApiResponses parentResponses,
									JsonView jsonViewAnnotation,
									io.swagger.v3.oas.annotations.responses.ApiResponse[] classResponses)
	{

		if (Arrays.stream(method.getParameters()).filter(p -> p.getType().isAssignableFrom(HttpServerExchange.class)).count() > 0L)
		{
			return null;
		}

		Operation operation = new Operation();

		io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.v3.oas.annotations.Operation.class);

		List<io.swagger.v3.oas.annotations.security.SecurityRequirement> apiSecurity = ReflectionUtils
				.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.security.SecurityRequirement.class);

		List<io.swagger.v3.oas.annotations.callbacks.Callback> apiCallbacks = ReflectionUtils
				.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.callbacks.Callback.class);

		List<Server> apiServers = ReflectionUtils.getRepeatableAnnotations(method, Server.class);

		List<io.swagger.v3.oas.annotations.tags.Tag> apiTags = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.tags.Tag.class);

		List<io.swagger.v3.oas.annotations.Parameter> apiParameters = ReflectionUtils.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.Parameter.class);

		List<io.swagger.v3.oas.annotations.responses.ApiResponse> apiResponses = ReflectionUtils
				.getRepeatableAnnotations(method, io.swagger.v3.oas.annotations.responses.ApiResponse.class);
		io.swagger.v3.oas.annotations.parameters.RequestBody apiRequestBody = ReflectionUtils.getAnnotation(method, io.swagger.v3.oas.annotations.parameters.RequestBody.class);

		ExternalDocumentation apiExternalDocumentation = ReflectionUtils.getAnnotation(method, ExternalDocumentation.class);

		// callbacks
		Map<String, Callback> callbacks = new LinkedHashMap<>();

		if (apiCallbacks != null)
		{
			for (io.swagger.v3.oas.annotations.callbacks.Callback methodCallback : apiCallbacks)
			{
				Map<String, Callback> currentCallbacks = getCallbacks(methodCallback, methodProduces, classProduces, methodConsumes, classConsumes, jsonViewAnnotation);
				callbacks.putAll(currentCallbacks);
			}
		}
		if (callbacks.size() > 0)
		{
			operation.setCallbacks(callbacks);
		}

		// security
		classSecurityRequirements.forEach(operation::addSecurityItem);
		if (apiSecurity != null)
		{
			Optional<List<SecurityRequirement>> requirementsObject = SecurityParser.getSecurityRequirements(apiSecurity.toArray(
																																new io.swagger.v3.oas.annotations.security.SecurityRequirement[apiSecurity
																																		.size()]));
			if (requirementsObject.isPresent())
			{
				requirementsObject.get().stream()
						.filter(r -> operation.getSecurity() == null || !operation.getSecurity().contains(r))
						.forEach(operation::addSecurityItem);
			}
		}

		// servers
		if (classServers != null)
		{
			classServers.forEach(operation::addServersItem);
		}

		if (apiServers != null)
		{
			AnnotationsUtils.getServers(apiServers.toArray(new Server[apiServers.size()])).ifPresent(servers -> servers.forEach(operation::addServersItem));
		}

		// external docs
		AnnotationsUtils.getExternalDocumentation(apiExternalDocumentation).ifPresent(operation::setExternalDocs);

		// method tags
		if (apiTags != null)
		{
			apiTags.stream()
					.filter(t -> operation.getTags() == null || (operation.getTags() != null && !operation.getTags().contains(t.name())))
					.map(t -> t.name())
					.forEach(operation::addTagsItem);
			AnnotationsUtils.getTags(apiTags.toArray(new io.swagger.v3.oas.annotations.tags.Tag[apiTags.size()]), true).ifPresent(tags -> openApiTags.addAll(tags));
		}

		// parameters
		if (globalParameters != null)
		{
			for (Parameter globalParameter : globalParameters)
			{
				operation.addParametersItem(globalParameter);
			}
		}
		if (apiParameters != null)
		{

			getParametersListFromAnnotation(
											apiParameters.toArray(new io.swagger.v3.oas.annotations.Parameter[apiParameters.size()]),
											classConsumes,
											methodConsumes,
											operation,
											jsonViewAnnotation).ifPresent(p -> p.forEach(operation::addParametersItem));
		}

		// RequestBody in Method
		if (apiRequestBody != null && operation.getRequestBody() == null)
		{


			OperationParser.getRequestBody(apiRequestBody, classConsumes, methodConsumes, components, jsonViewAnnotation).ifPresent(
																																	operation::setRequestBody);

			LOGGER.debug("request body: " + operation.getRequestBody().toString());
		}

		// operation id
		if (StringUtils.isBlank(operation.getOperationId()))
		{
			operation.setOperationId(getOperationId(method.getName()));
		}

		/*
				if (StringUtils.isBlank(operation.getOperationId()))
		{
			String className = toLowerCase(method.getDeclaringClass().getSimpleName().charAt(0)) + method.getDeclaringClass().getSimpleName().substring(1);

			String operationId = String.format("%s%s",className,toUpperCase(method.getName().charAt(0)) + method.getName().substring(1));

			operation.setOperationId(operationId);
		}

		 */

		// classResponses
		if (classResponses != null && classResponses.length > 0)
		{

			OperationParser.getApiResponses(
											classResponses,
											classProduces,
											methodProduces,
											components,
											jsonViewAnnotation)
					.ifPresent(responses ->
					{
						if (operation.getResponses() == null)
						{
							operation.setResponses(responses);
						}
						else
						{
							responses.forEach(operation.getResponses()::addApiResponse);
						}
					});
		}

		if (apiOperation != null)
		{
			setOperationObjectFromApiOperationAnnotation(operation, apiOperation, methodProduces, classProduces, methodConsumes, classConsumes, jsonViewAnnotation);
		}

		// apiResponses
		if (apiResponses != null && apiResponses.size() > 0)
		{
			OperationParser.getApiResponses(
											apiResponses.toArray(new io.swagger.v3.oas.annotations.responses.ApiResponse[apiResponses.size()]),
											classProduces,
											methodProduces,
											components,
											jsonViewAnnotation)
					.ifPresent(responses ->
					{
						if (operation.getResponses() == null)
						{
							operation.setResponses(responses);
						}
						else
						{
							responses.forEach(operation.getResponses()::addApiResponse);
						}
					});
		}

		// class tags after tags defined as field of @Operation
		if (classTags != null)
		{
			classTags.stream()
					.filter(t -> operation.getTags() == null || (operation.getTags() != null && !operation.getTags().contains(t)))
					.forEach(operation::addTagsItem);
		}

		// external docs of class if not defined in annotation of method or as
		// field of Operation annotation
		if (operation.getExternalDocs() == null)
		{
			classExternalDocs.ifPresent(operation::setExternalDocs);
		}

		// if subresource, merge parent requestBody
		if (isSubresource && parentRequestBody != null)
		{
			if (operation.getRequestBody() == null)
			{
				operation.requestBody(parentRequestBody);
			}
			else
			{
				Content content = operation.getRequestBody().getContent();
				if (content == null)
				{
					content = parentRequestBody.getContent();
					operation.getRequestBody().setContent(content);
				}
				else if (parentRequestBody.getContent() != null)
				{
					for (String parentMediaType : parentRequestBody.getContent().keySet())
					{
						if (content.get(parentMediaType) == null)
						{
							content.addMediaType(parentMediaType, parentRequestBody.getContent().get(parentMediaType));
						}
					}
				}
			}
		}

		// handle return type, add as response in case.
		Type returnType = method.getGenericReturnType();
		final Class<?> subResource = getSubResourceWithJaxRsSubresourceLocatorSpecs(method);

		if (!shouldIgnoreClass(returnType.getTypeName()) && !returnType.equals(subResource))
		{
			LOGGER.debug("processing class " + returnType + " " + returnType.getTypeName());

			JavaType classType = TypeFactory.defaultInstance().constructType(returnType);

			if (classType != null && classType.getRawClass() != null)
			{
				if (classType.getRawClass().isAssignableFrom(ServerResponse.class))
				{
					if (classType.containedType(0) != null)
					{
						returnType = classType.containedType(0);
					}
				}
				else if (classType.getRawClass().isAssignableFrom(CompletableFuture.class))
				{
					Class<?> futureCls = classType.containedType(0).getRawClass();

					if (futureCls.isAssignableFrom(ServerResponse.class))
					{
						final JavaType futureType = TypeFactory.defaultInstance().constructType(classType.containedType(0));
						returnType = futureType.containedType(0);
					}
					else
					{
						returnType = classType.containedType(0);
					}
				}
			}

			ResolvedSchema resolvedSchema = ModelConverters.getInstance()
					.resolveAsResolvedSchema(new AnnotatedType(returnType).resolveAsRef(true).jsonViewAnnotation(jsonViewAnnotation));

			if (resolvedSchema.schema != null)
			{
				Schema returnTypeSchema = resolvedSchema.schema;
				Content content = new Content();
				MediaType mediaType = new MediaType().schema(returnTypeSchema);
				AnnotationsUtils.applyTypes(classProduces == null ? new String[0] : classProduces.value(),
											methodProduces == null ? new String[0] : methodProduces.value(), content, mediaType);
				if (operation.getResponses() == null)
				{
					operation.responses(
										new ApiResponses()._default(
																	new ApiResponse().description(DEFAULT_DESCRIPTION)
																			.content(content)));
				}
				if (operation.getResponses().getDefault() != null &&
						StringUtils.isBlank(operation.getResponses().getDefault().get$ref()))
				{
					if (operation.getResponses().getDefault().getContent() == null)
					{
						operation.getResponses().getDefault().content(content);
					}
					else
					{
						for (String key : operation.getResponses().getDefault().getContent().keySet())
						{
							if (operation.getResponses().getDefault().getContent().get(key).getSchema() == null)
							{
								operation.getResponses().getDefault().getContent().get(key).setSchema(returnTypeSchema);
							}
						}
					}
				}
				Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
				if (schemaMap != null)
				{
					schemaMap.forEach((key, schema) -> components.addSchemas(key, schema));
				}

			}
		}
		if (operation.getResponses() == null || operation.getResponses().isEmpty())
		{
			LOGGER.debug("responses are null or empty");

			// Content content = new Content();
			// MediaType mediaType = new MediaType();
			// AnnotationsUtils.applyTypes(classProduces == null ? new String[0]
			// : classProduces.value(),
			// methodProduces == null ? new String[0] : methodProduces.value(),
			// content, mediaType);

			ApiResponse apiResponseObject = new ApiResponse().description(DEFAULT_DESCRIPTION);// .content(content);
			operation.setResponses(new ApiResponses()._default(apiResponseObject));
		}

		return operation;
	}

	private boolean shouldIgnoreClass(String className)
	{
		if (StringUtils.isBlank(className))
		{
			return true;
		}
		boolean ignore = false;
		ignore = ignore || className.startsWith("javax.ws.rs.");
		ignore = ignore || className.equalsIgnoreCase("void");
		ignore = ignore || className.startsWith("io.undertow");
		ignore = ignore || className.startsWith("java.lang.Void");
		return ignore;
	}

	private Map<String, Callback> getCallbacks(
												io.swagger.v3.oas.annotations.callbacks.Callback apiCallback,
												Produces methodProduces,
												Produces classProduces,
												Consumes methodConsumes,
												Consumes classConsumes,
												JsonView jsonViewAnnotation)
	{
		Map<String, Callback> callbackMap = new HashMap<>();
		if (apiCallback == null)
		{
			return callbackMap;
		}

		Callback callbackObject = new Callback();
		if (StringUtils.isNotBlank(apiCallback.ref()))
		{
			callbackObject.set$ref(apiCallback.ref());
			callbackMap.put(apiCallback.name(), callbackObject);
			return callbackMap;
		}
		PathItem pathItemObject = new PathItem();
		for (io.swagger.v3.oas.annotations.Operation callbackOperation : apiCallback.operation())
		{
			Operation callbackNewOperation = new Operation();
			setOperationObjectFromApiOperationAnnotation(
															callbackNewOperation,
															callbackOperation,
															methodProduces,
															classProduces,
															methodConsumes,
															classConsumes,
															jsonViewAnnotation);
			setPathItemOperation(pathItemObject, callbackOperation.method(), callbackNewOperation);
		}

		callbackObject.addPathItem(apiCallback.callbackUrlExpression(), pathItemObject);
		callbackMap.put(apiCallback.name(), callbackObject);

		return callbackMap;
	}

	private void setPathItemOperation(PathItem pathItemObject, String method, Operation operation)
	{
		switch (method)
		{
			case POST_METHOD:
				pathItemObject.post(operation);
				break;
			case GET_METHOD:
				pathItemObject.get(operation);
				break;
			case DELETE_METHOD:
				pathItemObject.delete(operation);
				break;
			case PUT_METHOD:
				pathItemObject.put(operation);
				break;
			case PATCH_METHOD:
				pathItemObject.patch(operation);
				break;
			case TRACE_METHOD:
				pathItemObject.trace(operation);
				break;
			case HEAD_METHOD:
				pathItemObject.head(operation);
				break;
			case OPTIONS_METHOD:
				pathItemObject.options(operation);
				break;
			default:
				// Do nothing here
				break;
		}
	}

	protected void setOperationObjectFromApiOperationAnnotation(
																Operation operation,
																io.swagger.v3.oas.annotations.Operation apiOperation,
																Produces methodProduces,
																Produces classProduces,
																Consumes methodConsumes,
																Consumes classConsumes,
																JsonView jsonViewAnnotation)
	{
		if (StringUtils.isNotBlank(apiOperation.summary()))
		{
			operation.setSummary(apiOperation.summary());
		}
		if (StringUtils.isNotBlank(apiOperation.description()))
		{
			operation.setDescription(apiOperation.description());
		}
		if (StringUtils.isNotBlank(apiOperation.operationId()))
		{
			operation.setOperationId(getOperationId(apiOperation.operationId()));
		}
		if (apiOperation.deprecated())
		{
			operation.setDeprecated(apiOperation.deprecated());
		}

		ReaderUtils.getStringListFromStringArray(apiOperation.tags()).ifPresent(tags ->
		{
			tags.stream()
					.filter(t -> operation.getTags() == null || (operation.getTags() != null && !operation.getTags().contains(t)))
					.forEach(operation::addTagsItem);
		});

		if (operation.getExternalDocs() == null)
		{ // if not set in root annotation
			AnnotationsUtils.getExternalDocumentation(apiOperation.externalDocs()).ifPresent(operation::setExternalDocs);
		}

		OperationParser.getApiResponses(apiOperation.responses(), classProduces, methodProduces, components, jsonViewAnnotation).ifPresent(responses ->
		{
			if (operation.getResponses() == null)
			{
				operation.setResponses(responses);
			}
			else
			{
				responses.forEach(operation.getResponses()::addApiResponse);
			}
		});
		AnnotationsUtils.getServers(apiOperation.servers()).ifPresent(servers -> servers.forEach(operation::addServersItem));

		getParametersListFromAnnotation(
										apiOperation.parameters(),
										classConsumes,
										methodConsumes,
										operation,
										jsonViewAnnotation).ifPresent(p -> p.forEach(operation::addParametersItem));

		// security
		Optional<List<SecurityRequirement>> requirementsObject = SecurityParser.getSecurityRequirements(apiOperation.security());
		if (requirementsObject.isPresent())
		{
			requirementsObject.get().stream()
					.filter(r -> operation.getSecurity() == null || !operation.getSecurity().contains(r))
					.forEach(operation::addSecurityItem);
		}

		// RequestBody in Operation
		if (apiOperation != null && apiOperation.requestBody() != null && operation.getRequestBody() == null)
		{
			OperationParser.getRequestBody(apiOperation.requestBody(), classConsumes, methodConsumes, components, jsonViewAnnotation).ifPresent(
																																				requestBodyObject -> operation
																																						.setRequestBody(
																																										requestBodyObject));
		}

		// Extensions in Operation
		if (apiOperation.extensions().length > 0)
		{
			Map<String, Object> extensions = AnnotationsUtils.getExtensions(apiOperation.extensions());
			if (extensions != null)
			{
				for (String ext : extensions.keySet())
				{
					operation.addExtension(ext, extensions.get(ext));
				}
			}
		}
	}

	protected String getOperationId(String operationId)
	{
		boolean operationIdUsed = existOperationId(operationId);
		String operationIdToFind = null;
		int counter = 0;
		while (operationIdUsed)
		{
			operationIdToFind = String.format("%s_%d", operationId, ++counter);
			operationIdUsed = existOperationId(operationIdToFind);
		}
		if (operationIdToFind != null)
		{
			operationId = operationIdToFind;
		}
		return operationId;
	}

	private boolean existOperationId(String operationId)
	{
		if (openAPI == null)
		{
			return false;
		}
		if (openAPI.getPaths() == null || openAPI.getPaths().isEmpty())
		{
			return false;
		}
		for (PathItem path : openAPI.getPaths().values())
		{
			String pathOperationId = extractOperationIdFromPathItem(path);
			if (operationId.equalsIgnoreCase(pathOperationId))
			{
				return true;
			}

		}
		return false;
	}

	protected Optional<List<Parameter>> getParametersListFromAnnotation(io.swagger.v3.oas.annotations.Parameter[] parameters, Consumes classConsumes, Consumes methodConsumes,
																		Operation operation, JsonView jsonViewAnnotation)
	{
		if (parameters == null)
		{
			return Optional.empty();
		}
		List<Parameter> parametersObject = new ArrayList<>();
		for (io.swagger.v3.oas.annotations.Parameter parameter : parameters)
		{

			ResolvedParameter resolvedParameter = getParameters(
																ParameterProcessor.getParameterType(parameter), Collections.singletonList(parameter), operation, classConsumes,
																methodConsumes, jsonViewAnnotation);
			parametersObject.addAll(resolvedParameter.parameters);
		}
		if (parametersObject.size() == 0)
		{
			return Optional.empty();
		}
		return Optional.of(parametersObject);
	}

	protected ResolvedParameter getParameters(	Type type, List<Annotation> annotations, Operation operation, Consumes classConsumes,
												Consumes methodConsumes, JsonView jsonViewAnnotation)
	{

		final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
		if (!chain.hasNext())
		{
			return new ResolvedParameter();
		}

		LOGGER.debug("getParameters for {}", type);

		if(type.toString().equalsIgnoreCase("[map type; class java.util.Map, [simple type, class java.lang.String] -> [simple type, class java.nio.file.Path]]"))
		{
			type = TypeFactory.defaultInstance().constructCollectionType(java.util.List.class,java.nio.file.Path.class);

		}
		else if(type.toString().equalsIgnoreCase("[map type; class java.util.Map, [simple type, class java.lang.String] -> [simple type, class java.io.File]]"))
		{
			type = TypeFactory.defaultInstance().constructCollectionType(java.util.List.class,java.io.File.class);
		}

		Set<Type> typesToSkip = new HashSet<>();
		final OpenAPIExtension extension = chain.next();
		LOGGER.debug("trying extension {}", extension);

		final ResolvedParameter extractParametersResult = extension
				.extractParameters(annotations, type, typesToSkip, components, classConsumes, methodConsumes, true, jsonViewAnnotation, chain);
		return extractParametersResult;
	}

	private String extractOperationIdFromPathItem(PathItem path)
	{
		if (path.getGet() != null)
		{
			return path.getGet().getOperationId();
		}
		else if (path.getPost() != null)
		{
			return path.getPost().getOperationId();
		}
		else if (path.getPut() != null)
		{
			return path.getPut().getOperationId();
		}
		else if (path.getDelete() != null)
		{
			return path.getDelete().getOperationId();
		}
		else if (path.getOptions() != null)
		{
			return path.getOptions().getOperationId();
		}
		else if (path.getHead() != null)
		{
			return path.getHead().getOperationId();
		}
		else if (path.getPatch() != null)
		{
			return path.getPatch().getOperationId();
		}
		return "";
	}

	private boolean isEmptyComponents(Components components)
	{
		if (components == null)
		{
			return true;
		}
		if (components.getSchemas() != null && components.getSchemas().size() > 0)
		{
			return false;
		}
		if (components.getSecuritySchemes() != null && components.getSecuritySchemes().size() > 0)
		{
			return false;
		}
		if (components.getCallbacks() != null && components.getCallbacks().size() > 0)
		{
			return false;
		}
		if (components.getExamples() != null && components.getExamples().size() > 0)
		{
			return false;
		}
		if (components.getExtensions() != null && components.getExtensions().size() > 0)
		{
			return false;
		}
		if (components.getHeaders() != null && components.getHeaders().size() > 0)
		{
			return false;
		}
		if (components.getLinks() != null && components.getLinks().size() > 0)
		{
			return false;
		}
		if (components.getParameters() != null && components.getParameters().size() > 0)
		{
			return false;
		}
		if (components.getRequestBodies() != null && components.getRequestBodies().size() > 0)
		{
			return false;
		}
		if (components.getResponses() != null && components.getResponses().size() > 0)
		{
			return false;
		}

		return true;
	}

	protected boolean isOperationHidden(Method method)
	{
		io.swagger.v3.oas.annotations.Operation apiOperation = ReflectionUtils.getAnnotation(method, io.swagger.v3.oas.annotations.Operation.class);
		if (apiOperation != null && apiOperation.hidden())
		{
			return true;
		}
		Hidden hidden = method.getAnnotation(Hidden.class);
		if (hidden != null)
		{
			return true;
		}
		if (config != null && !Boolean.TRUE.equals(config.isReadAllResources()) && apiOperation == null)
		{
			return true;
		}
		return false;
	}

	public void setApplication(Application application)
	{
		this.application = application;
	}

	protected boolean ignoreOperationPath(String path, String parentPath)
	{

		if (StringUtils.isBlank(path) && StringUtils.isBlank(parentPath))
		{
			return true;
		}
		else if (StringUtils.isNotBlank(path) && StringUtils.isBlank(parentPath))
		{
			return false;
		}
		else if (StringUtils.isBlank(path) && StringUtils.isNotBlank(parentPath))
		{
			return false;
		}
		if (parentPath != null && !"".equals(parentPath) && !"/".equals(parentPath))
		{
			if (!parentPath.startsWith("/"))
			{
				parentPath = "/" + parentPath;
			}
			if (parentPath.endsWith("/"))
			{
				parentPath = parentPath.substring(0, parentPath.length() - 1);
			}
		}
		if (path != null && !"".equals(path) && !"/".equals(path))
		{
			if (!path.startsWith("/"))
			{
				path = "/" + path;
			}
			if (path.endsWith("/"))
			{
				path = path.substring(0, path.length() - 1);
			}
		}
		if (path.equals(parentPath))
		{
			return true;
		}
		return false;
	}

	protected Class<?> getSubResourceWithJaxRsSubresourceLocatorSpecs(Method method)
	{
		final Class<?> rawType = method.getReturnType();
		final Class<?> type;
		if (Class.class.equals(rawType))
		{
			type = getClassArgument(method.getGenericReturnType());
			if (type == null)
			{
				return null;
			}
		}
		else
		{
			type = rawType;
		}

		if (method.getAnnotation(jakarta.ws.rs.Path.class) != null)
		{
			if (ReaderUtils.extractOperationMethod(method, null) == null)
			{
				return type;
			}
		}
		return null;
	}

	private static Class<?> getClassArgument(Type cls)
	{
		if (cls instanceof ParameterizedType)
		{
			final ParameterizedType parameterized = (ParameterizedType) cls;
			final Type[] args = parameterized.getActualTypeArguments();
			if (args.length != 1)
			{
				LOGGER.error("Unexpected class definition: {}", cls);
				return null;
			}
			final Type first = args[0];
			if (first instanceof Class)
			{
				return (Class<?>) first;
			}
			else
			{
				return null;
			}
		}
		else
		{
			LOGGER.error("Unknown class definition: {}", cls);
			return null;
		}
	}
}
