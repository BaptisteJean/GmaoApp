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
@Table(name = "rapport")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Rapport.findAll", query = "SELECT r FROM Rapport r"),
		@NamedQuery(name = "Rapport.findByIdRapport", query = "SELECT r FROM Rapport r WHERE r.idRapport = :idRapport"),
		@NamedQuery(name = "Rapport.findByDateRedaction", query = "SELECT r FROM Rapport r WHERE r.dateRedaction = :dateRedaction"),
		@NamedQuery(name = "Rapport.findByDescriptionRapport", query = "SELECT r FROM Rapport r WHERE r.descriptionRapport = :descriptionRapport")})
public class Rapport implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_rapport")
	private Long idRapport;
	@Column(name = "date_redaction")
	private String dateRedaction;
	@Column(name = "description_rapport")
	private String descriptionRapport;
	@JoinColumn(name = "id_equipe", referencedColumnName = "id_equipe")
	@ManyToOne(optional = false)
	private Equipe idEquipe;
	@JoinColumn(name = "id_intervention", referencedColumnName = "id_intervention")
	@ManyToOne(optional = false)
	private Intervention idIntervention;

	public Rapport() {
	}

	public Rapport(Long idRapport) {
		this.idRapport = idRapport;
	}

	public Long getIdRapport() {
		return idRapport;
	}

	public void setIdRapport(Long idRapport) {
		this.idRapport = idRapport;
	}

	public String getDateRedaction() {
		return dateRedaction;
	}

	public void setDateRedaction(String dateRedaction) {
		this.dateRedaction = dateRedaction;
	}

	public String getDescriptionRapport() {
		return descriptionRapport;
	}

	public void setDescriptionRapport(String descriptionRapport) {
		this.descriptionRapport = descriptionRapport;
	}

	public Equipe getIdEquipe() {
		return idEquipe;
	}

	public void setIdEquipe(Equipe idEquipe) {
		this.idEquipe = idEquipe;
	}

	public Intervention getIdIntervention() {
		return idIntervention;
	}

	public void setIdIntervention(Intervention idIntervention) {
		this.idIntervention = idIntervention;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idRapport != null ? idRapport.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Rapport)) {
			return false;
		}
		Rapport other = (Rapport) object;
		if ((this.idRapport == null && other.idRapport != null)
				|| (this.idRapport != null && !this.idRapport
						.equals(other.idRapport))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.Rapport[ idRapport=" + idRapport
				+ " ]";
	}

}
