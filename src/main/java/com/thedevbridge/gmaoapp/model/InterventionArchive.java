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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "intervention_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "InterventionArchive.findAll", query = "SELECT i FROM InterventionArchive i"),
		@NamedQuery(name = "InterventionArchive.findByIdIntervention", query = "SELECT i FROM InterventionArchive i WHERE i.idIntervention = :idIntervention"),
		@NamedQuery(name = "InterventionArchive.findByDateIntervention", query = "SELECT i FROM InterventionArchive i WHERE i.dateIntervention = :dateIntervention"),
		@NamedQuery(name = "InterventionArchive.findByDescriptionIntervention", query = "SELECT i FROM InterventionArchive i WHERE i.descriptionIntervention = :descriptionIntervention"),
		@NamedQuery(name = "InterventionArchive.findByStatutRapport", query = "SELECT i FROM InterventionArchive i WHERE i.statutRapport = :statutRapport"),
		@NamedQuery(name = "InterventionArchive.findByTempsMis", query = "SELECT i FROM InterventionArchive i WHERE i.tempsMis = :tempsMis")})
public class InterventionArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_intervention")
	private Long idIntervention;
	@Column(name = "date_intervention")
	private String dateIntervention;
	@Column(name = "description_intervention")
	private String descriptionIntervention;
	@Column(name = "statut_rapport")
	private Boolean statutRapport;
	@Column(name = "temps_mis")
	private Integer tempsMis;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idIntervention")
	private List<RapportArchive> rapportArchiveList;
	@JoinColumn(name = "id_directeur_tech", referencedColumnName = "id_directeur_tech")
	@ManyToOne(optional = false)
	private DirecteurTechniqueArchive idDirecteurTech;
	@JoinColumn(name = "id_exemplaire_mat", referencedColumnName = "id_exemplaire_materiel")
	@ManyToOne(optional = false)
	private ExamplaireMaterielArchive idExemplaireMat;

	public InterventionArchive() {
	}

	public InterventionArchive(Long idIntervention) {
		this.idIntervention = idIntervention;
	}

	public Long getIdIntervention() {
		return idIntervention;
	}

	public void setIdIntervention(Long idIntervention) {
		this.idIntervention = idIntervention;
	}

	public String getDateIntervention() {
		return dateIntervention;
	}

	public void setDateIntervention(String dateIntervention) {
		this.dateIntervention = dateIntervention;
	}

	public String getDescriptionIntervention() {
		return descriptionIntervention;
	}

	public void setDescriptionIntervention(String descriptionIntervention) {
		this.descriptionIntervention = descriptionIntervention;
	}

	public Boolean getStatutRapport() {
		return statutRapport;
	}

	public void setStatutRapport(Boolean statutRapport) {
		this.statutRapport = statutRapport;
	}

	public Integer getTempsMis() {
		return tempsMis;
	}

	public void setTempsMis(Integer tempsMis) {
		this.tempsMis = tempsMis;
	}

	@XmlTransient
	public List<RapportArchive> getRapportArchiveList() {
		return rapportArchiveList;
	}

	public void setRapportArchiveList(List<RapportArchive> rapportArchiveList) {
		this.rapportArchiveList = rapportArchiveList;
	}

	public DirecteurTechniqueArchive getIdDirecteurTech() {
		return idDirecteurTech;
	}

	public void setIdDirecteurTech(DirecteurTechniqueArchive idDirecteurTech) {
		this.idDirecteurTech = idDirecteurTech;
	}

	public ExamplaireMaterielArchive getIdExemplaireMat() {
		return idExemplaireMat;
	}

	public void setIdExemplaireMat(ExamplaireMaterielArchive idExemplaireMat) {
		this.idExemplaireMat = idExemplaireMat;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idIntervention != null ? idIntervention.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof InterventionArchive)) {
			return false;
		}
		InterventionArchive other = (InterventionArchive) object;
		if ((this.idIntervention == null && other.idIntervention != null)
				|| (this.idIntervention != null && !this.idIntervention
						.equals(other.idIntervention))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.InterventionArchive[ idIntervention="
				+ idIntervention + " ]";
	}

}
