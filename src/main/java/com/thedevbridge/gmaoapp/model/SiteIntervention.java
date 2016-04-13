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
@Table(name = "site_Intervention")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "SiteIntervention.findAll", query = "SELECT s FROM SiteIntervention s"),
		@NamedQuery(name = "SiteIntervention.findByIdSiteIntervention", query = "SELECT s FROM SiteIntervention s WHERE s.idSiteIntervention = :idSiteIntervention"),
		@NamedQuery(name = "SiteIntervention.findByNumeroBatiment", query = "SELECT s FROM SiteIntervention s WHERE s.numeroBatiment = :numeroBatiment"),
		@NamedQuery(name = "SiteIntervention.findByLibeleBureau", query = "SELECT s FROM SiteIntervention s WHERE s.libeleBureau = :libeleBureau")})
public class SiteIntervention implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idSiteIntervention")
	private Long idSiteIntervention;
	@Column(name = "numero_batiment")
	private String numeroBatiment;
	@Column(name = "libele_bureau")
	private String libeleBureau;
	@JoinColumn(name = "id_client", referencedColumnName = "id_client")
	@ManyToOne(optional = false)
	private Client idClient;

	public SiteIntervention() {
	}

	public SiteIntervention(Long idSiteIntervention) {
		this.idSiteIntervention = idSiteIntervention;
	}

	public Long getIdSiteIntervention() {
		return idSiteIntervention;
	}

	public void setIdSiteIntervention(Long idSiteIntervention) {
		this.idSiteIntervention = idSiteIntervention;
	}

	public String getNumeroBatiment() {
		return numeroBatiment;
	}

	public void setNumeroBatiment(String numeroBatiment) {
		this.numeroBatiment = numeroBatiment;
	}

	public String getLibeleBureau() {
		return libeleBureau;
	}

	public void setLibeleBureau(String libeleBureau) {
		this.libeleBureau = libeleBureau;
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
		hash += (idSiteIntervention != null ? idSiteIntervention.hashCode() : 0);
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
		if ((this.idSiteIntervention == null && other.idSiteIntervention != null)
				|| (this.idSiteIntervention != null && !this.idSiteIntervention
						.equals(other.idSiteIntervention))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.SiteIntervention[ idSiteIntervention="
				+ idSiteIntervention + " ]";
	}

}
