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
@Table(name = "destockage")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Destockage.findAll", query = "SELECT d FROM Destockage d"),
		@NamedQuery(name = "Destockage.findByIdDestockage", query = "SELECT d FROM Destockage d WHERE d.idDestockage = :idDestockage"),
		@NamedQuery(name = "Destockage.findByDateRetrait", query = "SELECT d FROM Destockage d WHERE d.dateRetrait = :dateRetrait"),
		@NamedQuery(name = "Destockage.findByQuantiteRetire", query = "SELECT d FROM Destockage d WHERE d.quantiteRetire = :quantiteRetire")})
public class Destockage implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_destockage")
	private Long idDestockage;
	@Column(name = "date_retrait")
	private String dateRetrait;
	@Column(name = "quantite_retire")
	private Integer quantiteRetire;
	@JoinColumn(name = "id_destination", referencedColumnName = "id_destination")
	@ManyToOne(optional = false)
	private Destination idDestination;
	@JoinColumn(name = "id_exemplaire", referencedColumnName = "id_exemplaire_materiel")
	@ManyToOne(optional = false)
	private ExamplaireMateriel idExemplaire;

	public Destockage() {
	}

	public Destockage(Long idDestockage) {
		this.idDestockage = idDestockage;
	}

	public Long getIdDestockage() {
		return idDestockage;
	}

	public void setIdDestockage(Long idDestockage) {
		this.idDestockage = idDestockage;
	}

	public String getDateRetrait() {
		return dateRetrait;
	}

	public void setDateRetrait(String dateRetrait) {
		this.dateRetrait = dateRetrait;
	}

	public Integer getQuantiteRetire() {
		return quantiteRetire;
	}

	public void setQuantiteRetire(Integer quantiteRetire) {
		this.quantiteRetire = quantiteRetire;
	}

	public Destination getIdDestination() {
		return idDestination;
	}

	public void setIdDestination(Destination idDestination) {
		this.idDestination = idDestination;
	}

	public ExamplaireMateriel getIdExemplaire() {
		return idExemplaire;
	}

	public void setIdExemplaire(ExamplaireMateriel idExemplaire) {
		this.idExemplaire = idExemplaire;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDestockage != null ? idDestockage.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Destockage)) {
			return false;
		}
		Destockage other = (Destockage) object;
		if ((this.idDestockage == null && other.idDestockage != null)
				|| (this.idDestockage != null && !this.idDestockage
						.equals(other.idDestockage))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Destockage[ idDestockage="
				+ idDestockage + " ]";
	}

}
