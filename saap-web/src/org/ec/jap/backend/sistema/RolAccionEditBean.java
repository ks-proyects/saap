package org.ec.jap.backend.sistema;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.sistema.RolAccionNegocioBO;
import org.ec.jap.bo.sistema.RolBO;
import org.ec.jap.entiti.sistema.Rol;
import org.ec.jap.entiti.sistema.RolAccionNegocio;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class RolAccionEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	RolBO rolBO;

	@EJB
	RolAccionNegocioBO rolAccionNegocioBO;

	List<RolAccionNegocio> rolAccionNegocios;

	private Rol rol;

	private TreeNode root;
	private TreeNode[] selectedNodes;

	public RolAccionEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			super.init();
			search(null);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}

	}

	@Override
	public void search(ActionEvent event) {
		try {
			rol = rolBO.findByPk(getParam1Integer());
			rolAccionNegocios = rolAccionNegocioBO.generateTree(rol);
			generateTree();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			List<RolAccionNegocio> rolAccionNegociosSeleccionado = new ArrayList<>(0);
			if (selectedNodes != null && selectedNodes.length > 0) {
				for (TreeNode node : selectedNodes) {
					rolAccionNegociosSeleccionado.add((RolAccionNegocio) node.getData());
				}
			}
			rolAccionNegocioBO.saveAccions(getUsuarioCurrent(), rolAccionNegociosSeleccionado, rol);
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
		return null;
	}

	public void generateTree() throws Exception {
		root = new DefaultTreeNode("Root", null);
		root.setExpanded(true);
		getNodes(-1, root);

	}

	public void getNodes(Integer idPadre, TreeNode node) {
		for (RolAccionNegocio ss : rolAccionNegocios) {
			TreeNode ssNode = new DefaultTreeNode(ss, node);
			ssNode.setSelected(ss.getSeleccionado());
			ssNode.setExpanded(false);
		}
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

}
