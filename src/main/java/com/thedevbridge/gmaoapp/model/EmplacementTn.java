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
@Table(name = "emplacement_tn")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "EmplacementTn.findAll", query = "SELECT e FROM EmplacementTn e"),
		@NamedQuery(name = "EmplacementTn.findByIdEmpltn", query = "SELECT e FROM EmplacementTn e WHERE e.idEmpltn = :idEmpltn"),
		@NamedQuery(name = "EmplacementTn.findByLibelleCaisse", query = "SELECT e FROM EmplacementTn e WHERE e.libelleCaisse = :libelleCaisse"),
		@NamedQuery(name = "EmplacementTn.findByLibelleTirroir", query = "SELECT e FROM EmplacementTn e WHERE e.libelleTirroir = :libelleTirroir")})
public class EmplacementTn implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_empltn")
	private Long idEmpltn;
	@Column(name = "libelle_caisse")
	private String libelleCaisse;
	@Column(name = "libelle_tirroir")
	private String libelleTirroir;
	@JoinColumn(name = "id_emplacement", referencedColumnName = "id_empl")
	@ManyToOne(optional = false)
	private Emplacement idEmplacement;

	public EmplacementTn() {
	}

	public EmplacementTn(Long idEmpltn) {
		this.idEmpltn = idEmpltn;
	}

	public Long getIdEmpltn() {
		return idEmpltn;
	}

	public void setIdEmpltn(Long idEmpltn) {
		this.idEmpltn = idEmpltn;
	}

	public String getLibelleCaisse() {
		return libelleCaisse;
	}

	public void setLibelleCaisse(String libelleCaisse) {
		this.libelleCaisse = libelleCaisse;
	}

	public String getLibelleTirroir() {
		return libelleTirroir;
	}

	public void setLibelleTirroir(String libelleTirroir) {
		this.libelleTirroir = libelleTirroir;
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
		hash += (idEmpltn != null ? idEmpltn.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof EmplacementTn)) {
			return false;
		}
		EmplacementTn other = (EmplacementTn) object;
		if ((this.idEmpltn == null && other.idEmpltn != null)
				|| (this.idEmpltn != null && !this.idEmpltn
						.equals(other.idEmpltn))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.EmplacementTn[ idEmpltn="
				+ idEmpltn + " ]";
	}

}
