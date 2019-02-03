package org.ec.jap.xmlaccessortype;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class Pie {

	private List<Slice> slice;

	public Pie() {
	}

	public List<Slice> getSlice() {
		return slice;
	}

	public void setSlice(List<Slice> slice) {
		this.slice = slice;
	}

}
