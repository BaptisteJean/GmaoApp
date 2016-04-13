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
@Table(name = "intervention")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Intervention.findAll", query = "SELECT i FROM Intervention i"),
		@NamedQuery(name = "Intervention.findByIdIntervention", query = "SELECT i FROM Intervention i WHERE i.idIntervention = :idIntervention"),
		@NamedQuery(name = "Intervention.findByDateIntervention", query = "SELECT i FROM Intervention i WHERE i.dateIntervention = :dateIntervention"),
		@NamedQuery(name = "Intervention.findByDescriptionIntervention", query = "SELECT i FROM Intervention i WHERE i.descriptionIntervention = :descriptionIntervention"),
		@NamedQuery(name = "Intervention.findByTempsMis", query = "SELECT i FROM Intervention i WHERE i.tempsMis = :tempsMis"),
		@NamedQuery(name = "Intervention.findByStatutRapport", query = "SELECT i FROM Intervention i WHERE i.statutRapport = :statutRapport")})
public class Intervention implements Serializable {
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
	@Column(name = "temps_mis")
	private Integer tempsMis;
	@Column(name = "statut_rapport")
	private Boolean statutRapport;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idIntervention")
	private List<Rapport> rapportList;
	@JoinColumn(name = "id_directeur_tech", referencedColumnName = "id_directeur_tech")
	@ManyToOne(optional = false)
	private DirecteurTechnique idDirecteurTech;
	@JoinColumn(name = "id_exemplaire_mat", referencedColumnName = "id_exemplaire_materiel")
	@ManyToOne(optional = false)
	private ExamplaireMateriel idExemplaireMat;

	public Intervention() {
	}

	public Intervention(Long idIntervention) {
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

	public Integer getTempsMis() {
		return tempsMis;
	}

	public void setTempsMis(Integer tempsMis) {
		this.tempsMis = tempsMis;
	}

	public Boolean getStatutRapport() {
		return statutRapport;
	}

	public void setStatutRapport(Boolean statutRapport) {
		this.statutRapport = statutRapport;
	}

	@XmlTransient
	public List<Rapport> getRapportList() {
		return rapportList;
	}

	public void setRapportList(List<Rapport> rapportList) {
		this.rapportList = rapportList;
	}

	public DirecteurTechnique getIdDirecteurTech() {
		return idDirecteurTech;
	}

	public void setIdDirecteurTech(DirecteurTechnique idDirecteurTech) {
		this.idDirecteurTech = idDirecteurTech;
	}

	public ExamplaireMateriel getIdExemplaireMat() {
		return idExemplaireMat;
	}

	public void setIdExemplaireMat(ExamplaireMateriel idExemplaireMat) {
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
		if (!(object instanceof Intervention)) {
			return false;
		}
		Intervention other = (Intervention) object;
		if ((this.idIntervention == null && other.idIntervention != null)
				|| (this.idIntervention != null && !this.idIntervention
						.equals(other.idIntervention))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Intervention[ idIntervention="
				+ idIntervention + " ]";
	}

}
