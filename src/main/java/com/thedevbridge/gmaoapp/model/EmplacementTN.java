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
@Table(name = "emplacement_TN")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "EmplacementTN.findAll", query = "SELECT e FROM EmplacementTN e"),
		@NamedQuery(name = "EmplacementTN.findByIdEmplTN", query = "SELECT e FROM EmplacementTN e WHERE e.idEmplTN = :idEmplTN"),
		@NamedQuery(name = "EmplacementTN.findByLibelleCaisse", query = "SELECT e FROM EmplacementTN e WHERE e.libelleCaisse = :libelleCaisse"),
		@NamedQuery(name = "EmplacementTN.findByLibelleTirroir", query = "SELECT e FROM EmplacementTN e WHERE e.libelleTirroir = :libelleTirroir")})
public class EmplacementTN implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_EmplTN")
	private Long idEmplTN;
	@Column(name = "libelle_caisse")
	private String libelleCaisse;
	@Column(name = "libelle_tirroir")
	private String libelleTirroir;
	@JoinColumn(name = "id_emplacement", referencedColumnName = "id_Empl")
	@ManyToOne(optional = false)
	private Emplacement idEmplacement;

	public EmplacementTN() {
	}

	public EmplacementTN(Long idEmplTN) {
		this.idEmplTN = idEmplTN;
	}

	public Long getIdEmplTN() {
		return idEmplTN;
	}

	public void setIdEmplTN(Long idEmplTN) {
		this.idEmplTN = idEmplTN;
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
		hash += (idEmplTN != null ? idEmplTN.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof EmplacementTN)) {
			return false;
		}
		EmplacementTN other = (EmplacementTN) object;
		if ((this.idEmplTN == null && other.idEmplTN != null)
				|| (this.idEmplTN != null && !this.idEmplTN
						.equals(other.idEmplTN))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.EmplacementTN[ idEmplTN="
				+ idEmplTN + " ]";
	}

}
