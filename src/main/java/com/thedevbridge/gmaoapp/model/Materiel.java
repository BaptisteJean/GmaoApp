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
@Table(name = "materiel")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Materiel.findAll", query = "SELECT m FROM Materiel m"),
		@NamedQuery(name = "Materiel.findByIdMateriel", query = "SELECT m FROM Materiel m WHERE m.idMateriel = :idMateriel"),
		@NamedQuery(name = "Materiel.findByDesignation", query = "SELECT m FROM Materiel m WHERE m.designation = :designation"),
		@NamedQuery(name = "Materiel.findByTypeMateriel", query = "SELECT m FROM Materiel m WHERE m.typeMateriel = :typeMateriel"),
		@NamedQuery(name = "Materiel.findByModelMateriel", query = "SELECT m FROM Materiel m WHERE m.modelMateriel = :modelMateriel")})
public class Materiel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_materiel")
	private Long idMateriel;
	@Column(name = "designation")
	private String designation;
	@Column(name = "type_materiel")
	private String typeMateriel;
	@Column(name = "model_materiel")
	private String modelMateriel;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idMateriel")
	private List<ExamplaireMateriel> examplaireMaterielList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idMateriel")
	private List<Provient> provientList;

	public Materiel() {
	}

	public Materiel(Long idMateriel) {
		this.idMateriel = idMateriel;
	}

	public Long getIdMateriel() {
		return idMateriel;
	}

	public void setIdMateriel(Long idMateriel) {
		this.idMateriel = idMateriel;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getTypeMateriel() {
		return typeMateriel;
	}

	public void setTypeMateriel(String typeMateriel) {
		this.typeMateriel = typeMateriel;
	}

	public String getModelMateriel() {
		return modelMateriel;
	}

	public void setModelMateriel(String modelMateriel) {
		this.modelMateriel = modelMateriel;
	}

	@XmlTransient
	public List<ExamplaireMateriel> getExamplaireMaterielList() {
		return examplaireMaterielList;
	}

	public void setExamplaireMaterielList(
			List<ExamplaireMateriel> examplaireMaterielList) {
		this.examplaireMaterielList = examplaireMaterielList;
	}

	@XmlTransient
	public List<Provient> getProvientList() {
		return provientList;
	}

	public void setProvientList(List<Provient> provientList) {
		this.provientList = provientList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idMateriel != null ? idMateriel.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Materiel)) {
			return false;
		}
		Materiel other = (Materiel) object;
		if ((this.idMateriel == null && other.idMateriel != null)
				|| (this.idMateriel != null && !this.idMateriel
						.equals(other.idMateriel))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Materiel[ idMateriel="
				+ idMateriel + " ]";
	}

}
