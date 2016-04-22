/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thedevbridge.gmaoapp.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wabo
 */
@Entity
@Table(name = "planification_curative_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PlanificationCurativeArchive.findAll", query = "SELECT p FROM PlanificationCurativeArchive p"),
		@NamedQuery(name = "PlanificationCurativeArchive.findByIdPlanifCurative", query = "SELECT p FROM PlanificationCurativeArchive p WHERE p.idPlanifCurative = :idPlanifCurative"),
		@NamedQuery(name = "PlanificationCurativeArchive.findByDescriptionProbleme", query = "SELECT p FROM PlanificationCurativeArchive p WHERE p.descriptionProbleme = :descriptionProbleme"),
		@NamedQuery(name = "PlanificationCurativeArchive.findByEtatMaterielApresIntervention", query = "SELECT p FROM PlanificationCurativeArchive p WHERE p.etatMaterielApresIntervention = :etatMaterielApresIntervention"),
		@NamedQuery(name = "PlanificationCurativeArchive.findByHeureDebut", query = "SELECT p FROM PlanificationCurativeArchive p WHERE p.heureDebut = :heureDebut"),
		@NamedQuery(name = "PlanificationCurativeArchive.findByHeureFin", query = "SELECT p FROM PlanificationCurativeArchive p WHERE p.heureFin = :heureFin"),
		@NamedQuery(name = "PlanificationCurativeArchive.findBySolutionApportee", query = "SELECT p FROM PlanificationCurativeArchive p WHERE p.solutionApportee = :solutionApportee")})
public class PlanificationCurativeArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_planif_curative")
	private Long idPlanifCurative;
	@Column(name = "description_probleme")
	private String descriptionProbleme;
	@Column(name = "etat_materiel_apres_intervention")
	private String etatMaterielApresIntervention;
	@Column(name = "heure_debut")
	private String heureDebut;
	@Column(name = "heure_fin")
	private String heureFin;
	@Column(name = "solution_apportee")
	private String solutionApportee;

	public PlanificationCurativeArchive() {
	}

	public PlanificationCurativeArchive(Long idPlanifCurative) {
		this.idPlanifCurative = idPlanifCurative;
	}

	public Long getIdPlanifCurative() {
		return idPlanifCurative;
	}

	public void setIdPlanifCurative(Long idPlanifCurative) {
		this.idPlanifCurative = idPlanifCurative;
	}

	public String getDescriptionProbleme() {
		return descriptionProbleme;
	}

	public void setDescriptionProbleme(String descriptionProbleme) {
		this.descriptionProbleme = descriptionProbleme;
	}

	public String getEtatMaterielApresIntervention() {
		return etatMaterielApresIntervention;
	}

	public void setEtatMaterielApresIntervention(
			String etatMaterielApresIntervention) {
		this.etatMaterielApresIntervention = etatMaterielApresIntervention;
	}

	public String getHeureDebut() {
		return heureDebut;
	}

	public void setHeureDebut(String heureDebut) {
		this.heureDebut = heureDebut;
	}

	public String getHeureFin() {
		return heureFin;
	}

	public void setHeureFin(String heureFin) {
		this.heureFin = heureFin;
	}

	public String getSolutionApportee() {
		return solutionApportee;
	}

	public void setSolutionApportee(String solutionApportee) {
		this.solutionApportee = solutionApportee;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPlanifCurative != null ? idPlanifCurative.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof PlanificationCurativeArchive)) {
			return false;
		}
		PlanificationCurativeArchive other = (PlanificationCurativeArchive) object;
		if ((this.idPlanifCurative == null && other.idPlanifCurative != null)
				|| (this.idPlanifCurative != null && !this.idPlanifCurative
						.equals(other.idPlanifCurative))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.PlanificationCurativeArchive[ idPlanifCurative="
				+ idPlanifCurative + " ]";
	}

}
