/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedevbridge.gmaoapp.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author wabo
 */
@Entity
@Table(name = "personnel")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Personnel.findAll", query = "SELECT p FROM Personnel p"),
		@NamedQuery(name = "Personnel.findByIdPersonnel", query = "SELECT p FROM Personnel p WHERE p.idPersonnel = :idPersonnel"),
		@NamedQuery(name = "Personnel.findByNomPersonnel", query = "SELECT p FROM Personnel p WHERE p.nomPersonnel = :nomPersonnel"),
		@NamedQuery(name = "Personnel.findByPrenomPersonnel", query = "SELECT p FROM Personnel p WHERE p.prenomPersonnel = :prenomPersonnel"),
		@NamedQuery(name = "Personnel.findByLogin", query = "SELECT p FROM Personnel p WHERE p.login = :login"),
		@NamedQuery(name = "Personnel.findByPassword", query = "SELECT p FROM Personnel p WHERE p.password = :password")})
public class Personnel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_personnel")
	private Long idPersonnel;
	@Column(name = "nom_personnel")
	private String nomPersonnel;
	@Column(name = "prenom_personnel")
	private String prenomPersonnel;
	@Column(name = "login")
	private String login;
	@Column(name = "password")
	private String password;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersonnel")
	private List<DirecteurTechnique> directeurTechniqueList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersonnel")
	private List<Technicien> technicienList;

	public Personnel() {
	}

	public Personnel(Long idPersonnel) {
		this.idPersonnel = idPersonnel;
	}

	public Long getIdPersonnel() {
		return idPersonnel;
	}

	public void setIdPersonnel(Long idPersonnel) {
		this.idPersonnel = idPersonnel;
	}

	public String getNomPersonnel() {
		return nomPersonnel;
	}

	public void setNomPersonnel(String nomPersonnel) {
		this.nomPersonnel = nomPersonnel;
	}

	public String getPrenomPersonnel() {
		return prenomPersonnel;
	}

	public void setPrenomPersonnel(String prenomPersonnel) {
		this.prenomPersonnel = prenomPersonnel;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlTransient
	public List<DirecteurTechnique> getDirecteurTechniqueList() {
		return directeurTechniqueList;
	}

	public void setDirecteurTechniqueList(
			List<DirecteurTechnique> directeurTechniqueList) {
		this.directeurTechniqueList = directeurTechniqueList;
	}

	@XmlTransient
	public List<Technicien> getTechnicienList() {
		return technicienList;
	}

	public void setTechnicienList(List<Technicien> technicienList) {
		this.technicienList = technicienList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPersonnel != null ? idPersonnel.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Personnel)) {
			return false;
		}
		Personnel other = (Personnel) object;
		if ((this.idPersonnel == null && other.idPersonnel != null)
				|| (this.idPersonnel != null && !this.idPersonnel
						.equals(other.idPersonnel))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Personnel[ idPersonnel="
				+ idPersonnel + " ]";
	}

}
