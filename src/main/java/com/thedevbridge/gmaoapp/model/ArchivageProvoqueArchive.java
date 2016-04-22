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
@Table(name = "archivage_provoque_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ArchivageProvoqueArchive.findAll", query = "SELECT a FROM ArchivageProvoqueArchive a"),
		@NamedQuery(name = "ArchivageProvoqueArchive.findByIdArchivageProvoque", query = "SELECT a FROM ArchivageProvoqueArchive a WHERE a.idArchivageProvoque = :idArchivageProvoque")})
public class ArchivageProvoqueArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_archivage_provoque")
	private Long idArchivageProvoque;
	@JoinColumn(name = "id_archivage", referencedColumnName = "id_archivage")
	@ManyToOne(optional = false)
	private ArchivageArchive idArchivage;

	public ArchivageProvoqueArchive() {
	}

	public ArchivageProvoqueArchive(Long idArchivageProvoque) {
		this.idArchivageProvoque = idArchivageProvoque;
	}

	public Long getIdArchivageProvoque() {
		return idArchivageProvoque;
	}

	public void setIdArchivageProvoque(Long idArchivageProvoque) {
		this.idArchivageProvoque = idArchivageProvoque;
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
		hash += (idArchivageProvoque != null
				? idArchivageProvoque.hashCode()
				: 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ArchivageProvoqueArchive)) {
			return false;
		}
		ArchivageProvoqueArchive other = (ArchivageProvoqueArchive) object;
		if ((this.idArchivageProvoque == null && other.idArchivageProvoque != null)
				|| (this.idArchivageProvoque != null && !this.idArchivageProvoque
						.equals(other.idArchivageProvoque))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.ArchivageProvoqueArchive[ idArchivageProvoque="
				+ idArchivageProvoque + " ]";
	}

}
