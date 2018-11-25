package api.empresas.interfaces;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error implements Serializable {

	private static final Dictionary dictionary = Dictionary.get();

	private String code;
	private String description;

	public Error() {
	}

	public Error(String code) {
		this.code = code;
		this.description = dictionary.valueOf(code);
	}

	public Error(String code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public String toString() {
		return "Error [code=" + code + ", description=" + description + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Error other = (Error) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
}
