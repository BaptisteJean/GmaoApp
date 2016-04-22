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
@Table(name = "destination_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DestinationArchive.findAll", query = "SELECT d FROM DestinationArchive d"),
		@NamedQuery(name = "DestinationArchive.findByIdDestination", query = "SELECT d FROM DestinationArchive d WHERE d.idDestination = :idDestination")})
public class DestinationArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_destination")
	private Long idDestination;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idDestination")
	private List<DestockageArchive> destockageArchiveList;
	@JoinColumn(name = "id_client", referencedColumnName = "id_client")
	@ManyToOne(optional = false)
	private ClientArchive idClient;

	public DestinationArchive() {
	}

	public DestinationArchive(Long idDestination) {
		this.idDestination = idDestination;
	}

	public Long getIdDestination() {
		return idDestination;
	}

	public void setIdDestination(Long idDestination) {
		this.idDestination = idDestination;
	}

	@XmlTransient
	public List<DestockageArchive> getDestockageArchiveList() {
		return destockageArchiveList;
	}

	public void setDestockageArchiveList(
			List<DestockageArchive> destockageArchiveList) {
		this.destockageArchiveList = destockageArchiveList;
	}

	public ClientArchive getIdClient() {
		return idClient;
	}

	public void setIdClient(ClientArchive idClient) {
		this.idClient = idClient;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDestination != null ? idDestination.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof DestinationArchive)) {
			return false;
		}
		DestinationArchive other = (DestinationArchive) object;
		if ((this.idDestination == null && other.idDestination != null)
				|| (this.idDestination != null && !this.idDestination
						.equals(other.idDestination))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.DestinationArchive[ idDestination="
				+ idDestination + " ]";
	}

}
