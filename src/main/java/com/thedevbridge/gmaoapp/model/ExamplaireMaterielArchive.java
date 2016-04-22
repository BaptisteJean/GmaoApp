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
@Table(name = "examplaire_materiel_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ExamplaireMaterielArchive.findAll", query = "SELECT e FROM ExamplaireMaterielArchive e"),
		@NamedQuery(name = "ExamplaireMaterielArchive.findByIdExemplaireMateriel", query = "SELECT e FROM ExamplaireMaterielArchive e WHERE e.idExemplaireMateriel = :idExemplaireMateriel"),
		@NamedQuery(name = "ExamplaireMaterielArchive.findByFabricantMarque", query = "SELECT e FROM ExamplaireMaterielArchive e WHERE e.fabricantMarque = :fabricantMarque"),
		@NamedQuery(name = "ExamplaireMaterielArchive.findByLibelleExemplaire", query = "SELECT e FROM ExamplaireMaterielArchive e WHERE e.libelleExemplaire = :libelleExemplaire"),
		@NamedQuery(name = "ExamplaireMaterielArchive.findByNumeroSerie", query = "SELECT e FROM ExamplaireMaterielArchive e WHERE e.numeroSerie = :numeroSerie"),
		@NamedQuery(name = "ExamplaireMaterielArchive.findByPuissance", query = "SELECT e FROM ExamplaireMaterielArchive e WHERE e.puissance = :puissance"),
		@NamedQuery(name = "ExamplaireMaterielArchive.findByQuantiteExemplaire", query = "SELECT e FROM ExamplaireMaterielArchive e WHERE e.quantiteExemplaire = :quantiteExemplaire")})
public class ExamplaireMaterielArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_exemplaire_materiel")
	private Long idExemplaireMateriel;
	@Column(name = "fabricant_marque")
	private String fabricantMarque;
	@Column(name = "libelle_exemplaire")
	private String libelleExemplaire;
	@Column(name = "numero_serie")
	private String numeroSerie;
	@Column(name = "puissance")
	private Integer puissance;
	@Column(name = "quantite_exemplaire")
	private Integer quantiteExemplaire;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idExemplaire")
	private List<DestockageArchive> destockageArchiveList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idExemplaireMat")
	private List<InterventionArchive> interventionArchiveList;
	@JoinColumn(name = "id_emplacement", referencedColumnName = "id_empl")
	@ManyToOne(optional = false)
	private EmplacementArchive idEmplacement;
	@JoinColumn(name = "id_materiel", referencedColumnName = "id_materiel")
	@ManyToOne(optional = false)
	private MaterielArchive idMateriel;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idExemplaireMat")
	private List<PlanificationArchive> planificationArchiveList;

	public ExamplaireMaterielArchive() {
	}

	public ExamplaireMaterielArchive(Long idExemplaireMateriel) {
		this.idExemplaireMateriel = idExemplaireMateriel;
	}

	public Long getIdExemplaireMateriel() {
		return idExemplaireMateriel;
	}

	public void setIdExemplaireMateriel(Long idExemplaireMateriel) {
		this.idExemplaireMateriel = idExemplaireMateriel;
	}

	public String getFabricantMarque() {
		return fabricantMarque;
	}

	public void setFabricantMarque(String fabricantMarque) {
		this.fabricantMarque = fabricantMarque;
	}

	public String getLibelleExemplaire() {
		return libelleExemplaire;
	}

	public void setLibelleExemplaire(String libelleExemplaire) {
		this.libelleExemplaire = libelleExemplaire;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public Integer getPuissance() {
		return puissance;
	}

	public void setPuissance(Integer puissance) {
		this.puissance = puissance;
	}

	public Integer getQuantiteExemplaire() {
		return quantiteExemplaire;
	}

	public void setQuantiteExemplaire(Integer quantiteExemplaire) {
		this.quantiteExemplaire = quantiteExemplaire;
	}

	@XmlTransient
	public List<DestockageArchive> getDestockageArchiveList() {
		return destockageArchiveList;
	}

	public void setDestockageArchiveList(
			List<DestockageArchive> destockageArchiveList) {
		this.destockageArchiveList = destockageArchiveList;
	}

	@XmlTransient
	public List<InterventionArchive> getInterventionArchiveList() {
		return interventionArchiveList;
	}

	public void setInterventionArchiveList(
			List<InterventionArchive> interventionArchiveList) {
		this.interventionArchiveList = interventionArchiveList;
	}

	public EmplacementArchive getIdEmplacement() {
		return idEmplacement;
	}

	public void setIdEmplacement(EmplacementArchive idEmplacement) {
		this.idEmplacement = idEmplacement;
	}

	public MaterielArchive getIdMateriel() {
		return idMateriel;
	}

	public void setIdMateriel(MaterielArchive idMateriel) {
		this.idMateriel = idMateriel;
	}

	@XmlTransient
	public List<PlanificationArchive> getPlanificationArchiveList() {
		return planificationArchiveList;
	}

	public void setPlanificationArchiveList(
			List<PlanificationArchive> planificationArchiveList) {
		this.planificationArchiveList = planificationArchiveList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idExemplaireMateriel != null
				? idExemplaireMateriel.hashCode()
				: 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ExamplaireMaterielArchive)) {
			return false;
		}
		ExamplaireMaterielArchive other = (ExamplaireMaterielArchive) object;
		if ((this.idExemplaireMateriel == null && other.idExemplaireMateriel != null)
				|| (this.idExemplaireMateriel != null && !this.idExemplaireMateriel
						.equals(other.idExemplaireMateriel))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.ExamplaireMaterielArchive[ idExemplaireMateriel="
				+ idExemplaireMateriel + " ]";
	}

}
