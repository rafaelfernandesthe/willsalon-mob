package br.com.doutorti.willsalon.model.utils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.json.JSONObject;

import com.google.gson.Gson;

@FacesConverter(value = "autoCompleteConverter")
public class AutoCompleteConverter implements Converter, Serializable {
	private static final long serialVersionUID = -2245566093160794522L;

	private static final String ATTRIBUTE_CLASS = "AutoCompleteConverter.class";

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value == null || value.trim().isEmpty() || value.equals("{")
				|| value.equals("\"\""))
			return null;

		JSONObject jsonResult = new JSONObject(value);
		if (jsonResult.has("birthDate")) {
			String birthDateString = (String) jsonResult.get("birthDate");
			birthDateString = birthDateString.replace(",", "")
					.replace(" ", "-");
			try {
				Date birthDate = new SimpleDateFormat("MMM-dd-yyyy")
						.parse(birthDateString);
				jsonResult.put("birthDate",
						new SimpleDateFormat("yyyy-MM-dd").format(birthDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return new Gson().fromJson(jsonResult.toString(), (Class<?>) component
				.getAttributes().get(ATTRIBUTE_CLASS));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			component.getAttributes().put(ATTRIBUTE_CLASS, value.getClass());
			return new Gson().toJson(value);
		} else
			return null;
	}
}