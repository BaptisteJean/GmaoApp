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
@Table(name = "archivage_automatique")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ArchivageAutomatique.findAll", query = "SELECT a FROM ArchivageAutomatique a"),
		@NamedQuery(name = "ArchivageAutomatique.findByIdArchivageAutomatique", query = "SELECT a FROM ArchivageAutomatique a WHERE a.idArchivageAutomatique = :idArchivageAutomatique")})
public class ArchivageAutomatique implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_archivage_automatique")
	private Long idArchivageAutomatique;
	@JoinColumn(name = "id_archivage", referencedColumnName = "id_archivage")
	@ManyToOne(optional = false)
	private Archivage idArchivage;

	public ArchivageAutomatique() {
	}

	public ArchivageAutomatique(Long idArchivageAutomatique) {
		this.idArchivageAutomatique = idArchivageAutomatique;
	}

	public Long getIdArchivageAutomatique() {
		return idArchivageAutomatique;
	}

	public void setIdArchivageAutomatique(Long idArchivageAutomatique) {
		this.idArchivageAutomatique = idArchivageAutomatique;
	}

	public Archivage getIdArchivage() {
		return idArchivage;
	}

	public void setIdArchivage(Archivage idArchivage) {
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
		if (!(object instanceof ArchivageAutomatique)) {
			return false;
		}
		ArchivageAutomatique other = (ArchivageAutomatique) object;
		if ((this.idArchivageAutomatique == null && other.idArchivageAutomatique != null)
				|| (this.idArchivageAutomatique != null && !this.idArchivageAutomatique
						.equals(other.idArchivageAutomatique))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.ArchivageAutomatique[ idArchivageAutomatique="
				+ idArchivageAutomatique + " ]";
	}

}
