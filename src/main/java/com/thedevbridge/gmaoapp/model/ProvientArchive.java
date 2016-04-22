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
@Table(name = "provient_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ProvientArchive.findAll", query = "SELECT p FROM ProvientArchive p"),
		@NamedQuery(name = "ProvientArchive.findByIdProvient", query = "SELECT p FROM ProvientArchive p WHERE p.idProvient = :idProvient")})
public class ProvientArchive implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_provient")
	private Long idProvient;
	@JoinColumn(name = "id_materiel", referencedColumnName = "id_materiel")
	@ManyToOne(optional = false)
	private MaterielArchive idMateriel;
	@JoinColumn(name = "id_provenance", referencedColumnName = "id_provenance")
	@ManyToOne(optional = false)
	private ProvenanceArchive idProvenance;

	public ProvientArchive() {
	}

	public ProvientArchive(Long idProvient) {
		this.idProvient = idProvient;
	}

	public Long getIdProvient() {
		return idProvient;
	}

	public void setIdProvient(Long idProvient) {
		this.idProvient = idProvient;
	}

	public MaterielArchive getIdMateriel() {
		return idMateriel;
	}

	public void setIdMateriel(MaterielArchive idMateriel) {
		this.idMateriel = idMateriel;
	}

	public ProvenanceArchive getIdProvenance() {
		return idProvenance;
	}

	public void setIdProvenance(ProvenanceArchive idProvenance) {
		this.idProvenance = idProvenance;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idProvient != null ? idProvient.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ProvientArchive)) {
			return false;
		}
		ProvientArchive other = (ProvientArchive) object;
		if ((this.idProvient == null && other.idProvient != null)
				|| (this.idProvient != null && !this.idProvient
						.equals(other.idProvient))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.ProvientArchive[ idProvient="
				+ idProvient + " ]";
	}

}
