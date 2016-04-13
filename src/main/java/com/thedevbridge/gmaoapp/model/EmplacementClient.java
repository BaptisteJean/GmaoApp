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
@Table(name = "emplacement_Client")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "EmplacementClient.findAll", query = "SELECT e FROM EmplacementClient e"),
		@NamedQuery(name = "EmplacementClient.findByIdEmplClient", query = "SELECT e FROM EmplacementClient e WHERE e.idEmplClient = :idEmplClient"),
		@NamedQuery(name = "EmplacementClient.findByBloc", query = "SELECT e FROM EmplacementClient e WHERE e.bloc = :bloc"),
		@NamedQuery(name = "EmplacementClient.findBySalle", query = "SELECT e FROM EmplacementClient e WHERE e.salle = :salle"),
		@NamedQuery(name = "EmplacementClient.findByDateInstallation", query = "SELECT e FROM EmplacementClient e WHERE e.dateInstallation = :dateInstallation")})
public class EmplacementClient implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_EmplClient")
	private Long idEmplClient;
	@Column(name = "bloc")
	private String bloc;
	@Column(name = "salle")
	private String salle;
	@Column(name = "date_installation")
	private String dateInstallation;
	@JoinColumn(name = "id_emplacement", referencedColumnName = "id_Empl")
	@ManyToOne(optional = false)
	private Emplacement idEmplacement;

	public EmplacementClient() {
	}

	public EmplacementClient(Long idEmplClient) {
		this.idEmplClient = idEmplClient;
	}

	public Long getIdEmplClient() {
		return idEmplClient;
	}

	public void setIdEmplClient(Long idEmplClient) {
		this.idEmplClient = idEmplClient;
	}

	public String getBloc() {
		return bloc;
	}

	public void setBloc(String bloc) {
		this.bloc = bloc;
	}

	public String getSalle() {
		return salle;
	}

	public void setSalle(String salle) {
		this.salle = salle;
	}

	public String getDateInstallation() {
		return dateInstallation;
	}

	public void setDateInstallation(String dateInstallation) {
		this.dateInstallation = dateInstallation;
	}

	public Emplacement getIdEmplacement() {
		return idEmplacement;
	}

	public void setIdEmplacement(Emplacement idEmplacement) {
		this.idEmplacement = idEmplacement;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idEmplClient != null ? idEmplClient.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof EmplacementClient)) {
			return false;
		}
		EmplacementClient other = (EmplacementClient) object;
		if ((this.idEmplClient == null && other.idEmplClient != null)
				|| (this.idEmplClient != null && !this.idEmplClient
						.equals(other.idEmplClient))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.EmplacementClient[ idEmplClient="
				+ idEmplClient + " ]";
	}

}
