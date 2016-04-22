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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wabo
 */
@Entity
@Table(name = "planification_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PlanificationArchive.findAll", query = "SELECT p FROM PlanificationArchive p"),
		@NamedQuery(name = "PlanificationArchive.findByIdPlanification", query = "SELECT p FROM PlanificationArchive p WHERE p.idPlanification = :idPlanification"),
		@NamedQuery(name = "PlanificationArchive.findByDatePlanification", query = "SELECT p FROM PlanificationArchive p WHERE p.datePlanification = :datePlanification")})
public class PlanificationArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_planification")
	private Long idPlanification;
	@Column(name = "date_planification")
	private String datePlanification;
	@JoinColumn(name = "id_exemplaire_mat", referencedColumnName = "id_exemplaire_materiel")
	@ManyToOne(optional = false)
	private ExamplaireMaterielArchive idExemplaireMat;

	public PlanificationArchive() {
	}

	public PlanificationArchive(Long idPlanification) {
		this.idPlanification = idPlanification;
	}

	public Long getIdPlanification() {
		return idPlanification;
	}

	public void setIdPlanification(Long idPlanification) {
		this.idPlanification = idPlanification;
	}

	public String getDatePlanification() {
		return datePlanification;
	}

	public void setDatePlanification(String datePlanification) {
		this.datePlanification = datePlanification;
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
		hash += (idPlanification != null ? idPlanification.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof PlanificationArchive)) {
			return false;
		}
		PlanificationArchive other = (PlanificationArchive) object;
		if ((this.idPlanification == null && other.idPlanification != null)
				|| (this.idPlanification != null && !this.idPlanification
						.equals(other.idPlanification))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.PlanificationArchive[ idPlanification="
				+ idPlanification + " ]";
	}

}
