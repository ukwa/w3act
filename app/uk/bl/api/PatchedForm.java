package uk.bl.api;

import static play.libs.F.None;
import static play.libs.F.Some;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.F.Option;

/**
 * This class is a patched version of a Play bug.<br>
 * The bug happens when you submit a form with errors and you want to prefill
 * the already entered data.
 * 
 * @author ndeverge
 * 
 * @param <T>
 */
public class PatchedForm<T> extends Form<T> {

	private final String rootName;
	private final Class<T> backedType;
	private final Map<String, List<ValidationError>> errors;

	public PatchedForm(final Class<T> clazz) {

		this(null, clazz);
	}

	@SuppressWarnings("unchecked")
	public PatchedForm(final String name, final Class<T> clazz) {
		this(name, clazz, new HashMap<String, String>(),
				new HashMap<String, List<ValidationError>>(), None());
	}

	/**
	 * Creates a new <code>Form</code>.
	 * 
	 * @param clazz
	 *            wrapped class
	 * @param data
	 *            the current form data (used to display the form)
	 * @param errors
	 *            the collection of errors associated with this form
	 * @param value
	 *            optional concrete value of type <code>T</code> if the form
	 *            submission was successful
	 */
	public PatchedForm(final String rootName, final Class<T> clazz,
			final Map<String, String> data,
			final Map<String, List<ValidationError>> errors,
			final Option<T> value) {
		super(rootName, clazz, data, errors, value);
		this.rootName = rootName;
		this.backedType = clazz;
		this.errors = errors;
	}

	private T blankInstance() {
		try {
			return backedType.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot instantiate " + backedType
					+ ". It must have a default constructor", e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Form<T> bind(final Map<String, String> data,
			final String... allowedFields) {
		DataBinder dataBinder = null;
		Map<String, String> objectData = data;
		if (rootName == null) {
			dataBinder = new DataBinder(blankInstance());
		} else {
			dataBinder = new DataBinder(blankInstance(), rootName);
			objectData = new HashMap<String, String>();
			for (String key : data.keySet()) {
				if (key.startsWith(rootName + ".")) {
					objectData.put(key.substring(rootName.length() + 1),
							data.get(key));
				}
			}
		}
		if (allowedFields.length > 0) {
			dataBinder.setAllowedFields(allowedFields);
		}
		dataBinder.setValidator(new SpringValidatorAdapter(
				play.data.validation.Validation.getValidator()));
		dataBinder.setConversionService(play.data.format.Formatters.conversion);
		dataBinder.setAutoGrowNestedPaths(true);
		dataBinder.bind(new MutablePropertyValues(objectData));
		dataBinder.validate();
		BindingResult result = dataBinder.getBindingResult();

		if (result.hasErrors()) {
			Map<String, List<ValidationError>> errors = new HashMap<String, List<ValidationError>>();
			for (FieldError error : result.getFieldErrors()) {
				String key = error.getObjectName() + "." + error.getField();
				if (key.startsWith("target.") && rootName == null) {
					key = key.substring(7);
				}
				List<Object> arguments = new ArrayList<Object>();
				for (Object arg : error.getArguments()) {
					if (!(arg instanceof org.springframework.context.support.DefaultMessageSourceResolvable)) {
						arguments.add(arg);
					}
				}
				if (!errors.containsKey(key)) {
					errors.put(key, new ArrayList<ValidationError>());
				}
				errors.get(key)
						.add(new ValidationError(key,
								error.isBindingFailure() ? "error.invalid"
										: error.getDefaultMessage(), arguments));
			}
			return new Form(rootName, backedType, data, errors, None());
		} else {
			Object globalError = null;
			if (result.getTarget() != null) {
				try {
					java.lang.reflect.Method v = result.getTarget().getClass()
							.getMethod("validate");
					globalError = v.invoke(result.getTarget());
				} catch (NoSuchMethodException e) {
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
			if (globalError != null) {
				Map<String, List<ValidationError>> errors = new HashMap<String, List<ValidationError>>();
				if (globalError instanceof String) {
					errors.put("", new ArrayList<ValidationError>());
					errors.get("").add(
							new ValidationError("", (String) globalError,
									new ArrayList()));
				} else if (globalError instanceof List) {
					errors.put("", (List<ValidationError>) globalError);
				} else if (globalError instanceof Map) {
					errors = (Map<String, List<ValidationError>>) globalError;
				}

				// Here is the patch !!
				if (result.getTarget() != null) {
					return new Form(rootName, backedType, data, errors,
							Some((T) result.getTarget()));
				} else {
					return new Form(rootName, backedType, data, errors, None());
				}
			}
			return new Form(rootName, backedType, new HashMap<String, String>(
					data), new HashMap<String, List<ValidationError>>(errors),
					Some((T) result.getTarget()));
		}
	}
	
	/**
	 * Gets the concrete value if the submission was a success.
	 * 
	 * @throws IllegalStateException
	 *             if there are errors binding the form, including the errors as
	 *             JSON in the message
	 */
	@Override
	public T get() {
		return null;
	}

}
