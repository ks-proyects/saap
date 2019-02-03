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
import org.ec.jap.bo.sistema.PerfilBO;
import org.ec.jap.bo.sistema.PerfilElementoSistemaBO;
import org.ec.jap.entiti.sistema.Perfil;
import org.ec.jap.entiti.sistema.PerfilElementoSistema;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class PerfilOpcionEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	PerfilBO perfilBO;

	@EJB
	PerfilElementoSistemaBO perfilElementoSistemaBO;

	List<PerfilElementoSistema> perfilElementoSistemas;

	private Perfil perfil;

	private TreeNode root;
	private TreeNode[] selectedNodes;

	public PerfilOpcionEditBean() {
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
			perfil = perfilBO.findByPk(getParam2Integer());
			perfilElementoSistemas = perfilElementoSistemaBO.generateTree(perfil);
			generateTree();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			List<PerfilElementoSistema> perfilElementoSistemasSeleccioandos = new ArrayList<>(0);
			if (selectedNodes != null && selectedNodes.length > 0) {
				
				for (TreeNode node : selectedNodes) {
					perfilElementoSistemasSeleccioandos.add((PerfilElementoSistema) node.getData());
				}
			
			}
			perfilElementoSistemaBO.saveOptions(getUsuarioCurrent(), perfilElementoSistemasSeleccioandos, perfil);
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
		for (PerfilElementoSistema ss : perfilElementoSistemas) {
			if (idPadre.equals(ss.getIdElementoSistema().getIdElementoSistemaPadre())) {
				TreeNode ssNode = new DefaultTreeNode(ss, node);
				ssNode.setSelected(ss.getSeleccionado());
				ssNode.setExpanded(false);
				getNodes(ss.getIdElementoSistema().getIdElementoSistema(), ssNode);
			}
		}
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
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
