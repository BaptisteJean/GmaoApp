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
@Table(name = "archivage_automatique_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ArchivageAutomatiqueArchive.findAll", query = "SELECT a FROM ArchivageAutomatiqueArchive a"),
		@NamedQuery(name = "ArchivageAutomatiqueArchive.findByIdArchivageAutomatique", query = "SELECT a FROM ArchivageAutomatiqueArchive a WHERE a.idArchivageAutomatique = :idArchivageAutomatique")})
public class ArchivageAutomatiqueArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_archivage_automatique")
	private Long idArchivageAutomatique;
	@JoinColumn(name = "id_archivage", referencedColumnName = "id_archivage")
	@ManyToOne(optional = false)
	private ArchivageArchive idArchivage;

	public ArchivageAutomatiqueArchive() {
	}

	public ArchivageAutomatiqueArchive(Long idArchivageAutomatique) {
		this.idArchivageAutomatique = idArchivageAutomatique;
	}

	public Long getIdArchivageAutomatique() {
		return idArchivageAutomatique;
	}

	public void setIdArchivageAutomatique(Long idArchivageAutomatique) {
		this.idArchivageAutomatique = idArchivageAutomatique;
	}

	public ArchivageArchive getIdArchivage() {
		return idArchivage;
	}

	public void setIdArchivage(ArchivageArchive idArchivage) {
		this.idArchivage = idArchivage;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idArchivageAutomatique != null ? idArchivageAutomatique
				.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ArchivageAutomatiqueArchive)) {
			return false;
		}
		ArchivageAutomatiqueArchive other = (ArchivageAutomatiqueArchive) object;
		if ((this.idArchivageAutomatique == null && other.idArchivageAutomatique != null)
				|| (this.idArchivageAutomatique != null && !this.idArchivageAutomatique
						.equals(other.idArchivageAutomatique))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.ArchivageAutomatiqueArchive[ idArchivageAutomatique="
				+ idArchivageAutomatique + " ]";
	}

}
