/**
 * 
 */
package org.ec.jap.backend.pagina;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 * @author fredd
 * 
 */
@ManagedBean
public class ImagesView {

	private List<String> images;

	@PostConstruct
	public void init() {
		images = new ArrayList<String>();
		for (int i = 1; i <= 3; i++) {
			images.add("nature" + i + ".jpg");
		}
	}

	public List<String> getImages() {
		return images;
	}
}
