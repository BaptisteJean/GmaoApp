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
@Table(name = "planification_preventive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PlanificationPreventive.findAll", query = "SELECT p FROM PlanificationPreventive p"),
		@NamedQuery(name = "PlanificationPreventive.findByIdPlanifPreventive", query = "SELECT p FROM PlanificationPreventive p WHERE p.idPlanifPreventive = :idPlanifPreventive"),
		@NamedQuery(name = "PlanificationPreventive.findByHeureMaintenance", query = "SELECT p FROM PlanificationPreventive p WHERE p.heureMaintenance = :heureMaintenance"),
		@NamedQuery(name = "PlanificationPreventive.findByTacheARealiser", query = "SELECT p FROM PlanificationPreventive p WHERE p.tacheARealiser = :tacheARealiser")})
public class PlanificationPreventive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_planif_preventive")
	private Long idPlanifPreventive;
	@Column(name = "heure_maintenance")
	private String heureMaintenance;
	@Column(name = "tache_a_realiser")
	private String tacheARealiser;

	public PlanificationPreventive() {
	}

	public PlanificationPreventive(Long idPlanifPreventive) {
		this.idPlanifPreventive = idPlanifPreventive;
	}

	public Long getIdPlanifPreventive() {
		return idPlanifPreventive;
	}

	public void setIdPlanifPreventive(Long idPlanifPreventive) {
		this.idPlanifPreventive = idPlanifPreventive;
	}

	public String getHeureMaintenance() {
		return heureMaintenance;
	}

	public void setHeureMaintenance(String heureMaintenance) {
		this.heureMaintenance = heureMaintenance;
	}

	public String getTacheARealiser() {
		return tacheARealiser;
	}

	public void setTacheARealiser(String tacheARealiser) {
		this.tacheARealiser = tacheARealiser;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPlanifPreventive != null ? idPlanifPreventive.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof PlanificationPreventive)) {
			return false;
		}
		PlanificationPreventive other = (PlanificationPreventive) object;
		if ((this.idPlanifPreventive == null && other.idPlanifPreventive != null)
				|| (this.idPlanifPreventive != null && !this.idPlanifPreventive
						.equals(other.idPlanifPreventive))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.PlanificationPreventive[ idPlanifPreventive="
				+ idPlanifPreventive + " ]";
	}

}
