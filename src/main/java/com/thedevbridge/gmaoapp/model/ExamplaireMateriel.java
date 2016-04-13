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
@Table(name = "examplaire_Materiel")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ExamplaireMateriel.findAll", query = "SELECT e FROM ExamplaireMateriel e"),
		@NamedQuery(name = "ExamplaireMateriel.findByIdExemplaireMateriel", query = "SELECT e FROM ExamplaireMateriel e WHERE e.idExemplaireMateriel = :idExemplaireMateriel"),
		@NamedQuery(name = "ExamplaireMateriel.findByQuantiteExemplaire", query = "SELECT e FROM ExamplaireMateriel e WHERE e.quantiteExemplaire = :quantiteExemplaire"),
		@NamedQuery(name = "ExamplaireMateriel.findByLibelleExemplaire", query = "SELECT e FROM ExamplaireMateriel e WHERE e.libelleExemplaire = :libelleExemplaire"),
		@NamedQuery(name = "ExamplaireMateriel.findByNumeroSerie", query = "SELECT e FROM ExamplaireMateriel e WHERE e.numeroSerie = :numeroSerie"),
		@NamedQuery(name = "ExamplaireMateriel.findByFabricantMarque", query = "SELECT e FROM ExamplaireMateriel e WHERE e.fabricantMarque = :fabricantMarque"),
		@NamedQuery(name = "ExamplaireMateriel.findByPuissance", query = "SELECT e FROM ExamplaireMateriel e WHERE e.puissance = :puissance")})
public class ExamplaireMateriel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_exemplaire_materiel")
	private Long idExemplaireMateriel;
	@Column(name = "quantite_exemplaire")
	private Integer quantiteExemplaire;
	@Column(name = "libelle_exemplaire")
	private String libelleExemplaire;
	@Column(name = "numero_serie")
	private String numeroSerie;
	@Column(name = "fabricant_marque")
	private String fabricantMarque;
	@Column(name = "puissance")
	private Integer puissance;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idExemplaire")
	private List<Destockage> destockageList;
	@JoinColumn(name = "id_emplacement", referencedColumnName = "id_Empl")
	@ManyToOne(optional = false)
	private Emplacement idEmplacement;
	@JoinColumn(name = "id_materiel", referencedColumnName = "id_materiel")
	@ManyToOne(optional = false)
	private Materiel idMateriel;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idExemplaireMat")
	private List<Planification> planificationList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idExemplaireMat")
	private List<Intervention> interventionList;

	public ExamplaireMateriel() {
	}

	public ExamplaireMateriel(Long idExemplaireMateriel) {
		this.idExemplaireMateriel = idExemplaireMateriel;
	}

	public Long getIdExemplaireMateriel() {
		return idExemplaireMateriel;
	}

	public void setIdExemplaireMateriel(Long idExemplaireMateriel) {
		this.idExemplaireMateriel = idExemplaireMateriel;
	}

	public Integer getQuantiteExemplaire() {
		return quantiteExemplaire;
	}

	public void setQuantiteExemplaire(Integer quantiteExemplaire) {
		this.quantiteExemplaire = quantiteExemplaire;
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

	public String getFabricantMarque() {
		return fabricantMarque;
	}

	public void setFabricantMarque(String fabricantMarque) {
		this.fabricantMarque = fabricantMarque;
	}

	public Integer getPuissance() {
		return puissance;
	}

	public void setPuissance(Integer puissance) {
		this.puissance = puissance;
	}

	@XmlTransient
	public List<Destockage> getDestockageList() {
		return destockageList;
	}

	public void setDestockageList(List<Destockage> destockageList) {
		this.destockageList = destockageList;
	}

	public Emplacement getIdEmplacement() {
		return idEmplacement;
	}

	public void setIdEmplacement(Emplacement idEmplacement) {
		this.idEmplacement = idEmplacement;
	}

	public Materiel getIdMateriel() {
		return idMateriel;
	}

	public void setIdMateriel(Materiel idMateriel) {
		this.idMateriel = idMateriel;
	}

	@XmlTransient
	public List<Planification> getPlanificationList() {
		return planificationList;
	}

	public void setPlanificationList(List<Planification> planificationList) {
		this.planificationList = planificationList;
	}

	@XmlTransient
	public List<Intervention> getInterventionList() {
		return interventionList;
	}

	public void setInterventionList(List<Intervention> interventionList) {
		this.interventionList = interventionList;
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
		if (!(object instanceof ExamplaireMateriel)) {
			return false;
		}
		ExamplaireMateriel other = (ExamplaireMateriel) object;
		if ((this.idExemplaireMateriel == null && other.idExemplaireMateriel != null)
				|| (this.idExemplaireMateriel != null && !this.idExemplaireMateriel
						.equals(other.idExemplaireMateriel))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.ExamplaireMateriel[ idExemplaireMateriel="
				+ idExemplaireMateriel + " ]";
	}

}