package org.ec.jap.xmlaccessortype;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Slice {

	@XmlAttribute
	private String title;

	@XmlAttribute(name = "pull_out")
	private Boolean pullOut;

	@XmlValue
	protected BigDecimal value;
		

	public Slice() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Boolean getPullOut() {
		return pullOut;
	}

	public void setPullOut(Boolean pullOut) {
		this.pullOut = pullOut;
	}
	

}
