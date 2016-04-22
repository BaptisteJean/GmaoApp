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
@Table(name = "site_intervention")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "SiteIntervention.findAll", query = "SELECT s FROM SiteIntervention s"),
		@NamedQuery(name = "SiteIntervention.findByIdsiteintervention", query = "SELECT s FROM SiteIntervention s WHERE s.idsiteintervention = :idsiteintervention"),
		@NamedQuery(name = "SiteIntervention.findByLibeleBureau", query = "SELECT s FROM SiteIntervention s WHERE s.libeleBureau = :libeleBureau"),
		@NamedQuery(name = "SiteIntervention.findByNumeroBatiment", query = "SELECT s FROM SiteIntervention s WHERE s.numeroBatiment = :numeroBatiment")})
public class SiteIntervention implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idsiteintervention")
	private Long idsiteintervention;
	@Column(name = "libele_bureau")
	private String libeleBureau;
	@Column(name = "numero_batiment")
	private String numeroBatiment;
	@JoinColumn(name = "id_client", referencedColumnName = "id_client")
	@ManyToOne(optional = false)
	private Client idClient;

	public SiteIntervention() {
	}

	public SiteIntervention(Long idsiteintervention) {
		this.idsiteintervention = idsiteintervention;
	}

	public Long getIdsiteintervention() {
		return idsiteintervention;
	}

	public void setIdsiteintervention(Long idsiteintervention) {
		this.idsiteintervention = idsiteintervention;
	}

	public String getLibeleBureau() {
		return libeleBureau;
	}

	public void setLibeleBureau(String libeleBureau) {
		this.libeleBureau = libeleBureau;
	}

	public String getNumeroBatiment() {
		return numeroBatiment;
	}

	public void setNumeroBatiment(String numeroBatiment) {
		this.numeroBatiment = numeroBatiment;
	}

	public Client getIdClient() {
		return idClient;
	}

	public void setIdClient(Client idClient) {
		this.idClient = idClient;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idsiteintervention != null ? idsiteintervention.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof SiteIntervention)) {
			return false;
		}
		SiteIntervention other = (SiteIntervention) object;
		if ((this.idsiteintervention == null && other.idsiteintervention != null)
				|| (this.idsiteintervention != null && !this.idsiteintervention
						.equals(other.idsiteintervention))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.SiteIntervention[ idsiteintervention="
				+ idsiteintervention + " ]";
	}

}
