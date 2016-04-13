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
@Table(name = "provenance")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Provenance.findAll", query = "SELECT p FROM Provenance p"),
		@NamedQuery(name = "Provenance.findByIdProvenance", query = "SELECT p FROM Provenance p WHERE p.idProvenance = :idProvenance"),
		@NamedQuery(name = "Provenance.findByMarche", query = "SELECT p FROM Provenance p WHERE p.marche = :marche")})
public class Provenance implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_provenance")
	private Long idProvenance;
	@Column(name = "marche")
	private String marche;
	@JoinColumn(name = "id_client", referencedColumnName = "id_client")
	@ManyToOne(optional = false)
	private Client idClient;
	@JoinColumn(name = "id_fournisseur", referencedColumnName = "id_fournisseur")
	@ManyToOne(optional = false)
	private Fournisseur idFournisseur;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idProvenance")
	private List<Provient> provientList;

	public Provenance() {
	}

	public Provenance(Long idProvenance) {
		this.idProvenance = idProvenance;
	}

	public Long getIdProvenance() {
		return idProvenance;
	}

	public void setIdProvenance(Long idProvenance) {
		this.idProvenance = idProvenance;
	}

	public String getMarche() {
		return marche;
	}

	public void setMarche(String marche) {
		this.marche = marche;
	}

	public Client getIdClient() {
		return idClient;
	}

	public void setIdClient(Client idClient) {
		this.idClient = idClient;
	}

	public Fournisseur getIdFournisseur() {
		return idFournisseur;
	}

	public void setIdFournisseur(Fournisseur idFournisseur) {
		this.idFournisseur = idFournisseur;
	}

	@XmlTransient
	public List<Provient> getProvientList() {
		return provientList;
	}

	public void setProvientList(List<Provient> provientList) {
		this.provientList = provientList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idProvenance != null ? idProvenance.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Provenance)) {
			return false;
		}
		Provenance other = (Provenance) object;
		if ((this.idProvenance == null && other.idProvenance != null)
				|| (this.idProvenance != null && !this.idProvenance
						.equals(other.idProvenance))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Provenance[ idProvenance="
				+ idProvenance + " ]";
	}

}
