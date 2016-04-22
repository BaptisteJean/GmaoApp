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
@Table(name = "emplacement_client_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "EmplacementClientArchive.findAll", query = "SELECT e FROM EmplacementClientArchive e"),
		@NamedQuery(name = "EmplacementClientArchive.findByIdEmplclient", query = "SELECT e FROM EmplacementClientArchive e WHERE e.idEmplclient = :idEmplclient"),
		@NamedQuery(name = "EmplacementClientArchive.findByBloc", query = "SELECT e FROM EmplacementClientArchive e WHERE e.bloc = :bloc"),
		@NamedQuery(name = "EmplacementClientArchive.findByDateInstallation", query = "SELECT e FROM EmplacementClientArchive e WHERE e.dateInstallation = :dateInstallation"),
		@NamedQuery(name = "EmplacementClientArchive.findBySalle", query = "SELECT e FROM EmplacementClientArchive e WHERE e.salle = :salle")})
public class EmplacementClientArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_emplclient")
	private Long idEmplclient;
	@Column(name = "bloc")
	private String bloc;
	@Column(name = "date_installation")
	private String dateInstallation;
	@Column(name = "salle")
	private String salle;
	@JoinColumn(name = "id_emplacement", referencedColumnName = "id_empl")
	@ManyToOne(optional = false)
	private EmplacementArchive idEmplacement;

	public EmplacementClientArchive() {
	}

	public EmplacementClientArchive(Long idEmplclient) {
		this.idEmplclient = idEmplclient;
	}

	public Long getIdEmplclient() {
		return idEmplclient;
	}

	public void setIdEmplclient(Long idEmplclient) {
		this.idEmplclient = idEmplclient;
	}

	public String getBloc() {
		return bloc;
	}

	public void setBloc(String bloc) {
		this.bloc = bloc;
	}

	public String getDateInstallation() {
		return dateInstallation;
	}

	public void setDateInstallation(String dateInstallation) {
		this.dateInstallation = dateInstallation;
	}

	public String getSalle() {
		return salle;
	}

	public void setSalle(String salle) {
		this.salle = salle;
	}

	public EmplacementArchive getIdEmplacement() {
		return idEmplacement;
	}

	public void setIdEmplacement(EmplacementArchive idEmplacement) {
		this.idEmplacement = idEmplacement;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idEmplclient != null ? idEmplclient.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof EmplacementClientArchive)) {
			return false;
		}
		EmplacementClientArchive other = (EmplacementClientArchive) object;
		if ((this.idEmplclient == null && other.idEmplclient != null)
				|| (this.idEmplclient != null && !this.idEmplclient
						.equals(other.idEmplclient))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.EmplacementClientArchive[ idEmplclient="
				+ idEmplclient + " ]";
	}

}
