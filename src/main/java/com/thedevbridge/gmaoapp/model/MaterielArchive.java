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
@Table(name = "materiel_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "MaterielArchive.findAll", query = "SELECT m FROM MaterielArchive m"),
		@NamedQuery(name = "MaterielArchive.findByIdMateriel", query = "SELECT m FROM MaterielArchive m WHERE m.idMateriel = :idMateriel"),
		@NamedQuery(name = "MaterielArchive.findByDesignation", query = "SELECT m FROM MaterielArchive m WHERE m.designation = :designation"),
		@NamedQuery(name = "MaterielArchive.findByModelMateriel", query = "SELECT m FROM MaterielArchive m WHERE m.modelMateriel = :modelMateriel"),
		@NamedQuery(name = "MaterielArchive.findByTypeMateriel", query = "SELECT m FROM MaterielArchive m WHERE m.typeMateriel = :typeMateriel")})
public class MaterielArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_materiel")
	private Long idMateriel;
	@Column(name = "designation")
	private String designation;
	@Column(name = "model_materiel")
	private String modelMateriel;
	@Column(name = "type_materiel")
	private String typeMateriel;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idMateriel")
	private List<ExamplaireMaterielArchive> examplaireMaterielArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idMateriel")
	private List<ProvientArchive> provientArchiveList;

	public MaterielArchive() {
	}

	public MaterielArchive(Long idMateriel) {
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

	public String getModelMateriel() {
		return modelMateriel;
	}

	public void setModelMateriel(String modelMateriel) {
		this.modelMateriel = modelMateriel;
	}

	public String getTypeMateriel() {
		return typeMateriel;
	}

	public void setTypeMateriel(String typeMateriel) {
		this.typeMateriel = typeMateriel;
	}

	@XmlTransient
	public List<ExamplaireMaterielArchive> getExamplaireMaterielArchiveList() {
		return examplaireMaterielArchiveList;
	}

	public void setExamplaireMaterielArchiveList(
			List<ExamplaireMaterielArchive> examplaireMaterielArchiveList) {
		this.examplaireMaterielArchiveList = examplaireMaterielArchiveList;
	}

	@XmlTransient
	public List<ProvientArchive> getProvientArchiveList() {
		return provientArchiveList;
	}

	public void setProvientArchiveList(List<ProvientArchive> provientArchiveList) {
		this.provientArchiveList = provientArchiveList;
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
		if (!(object instanceof MaterielArchive)) {
			return false;
		}
		MaterielArchive other = (MaterielArchive) object;
		if ((this.idMateriel == null && other.idMateriel != null)
				|| (this.idMateriel != null && !this.idMateriel
						.equals(other.idMateriel))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.MaterielArchive[ idMateriel="
				+ idMateriel + " ]";
	}

}
