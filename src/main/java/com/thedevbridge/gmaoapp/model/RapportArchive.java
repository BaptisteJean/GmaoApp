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
@Table(name = "rapport_archive")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "RapportArchive.findAll", query = "SELECT r FROM RapportArchive r"),
		@NamedQuery(name = "RapportArchive.findByIdRapport", query = "SELECT r FROM RapportArchive r WHERE r.idRapport = :idRapport"),
		@NamedQuery(name = "RapportArchive.findByDateRedaction", query = "SELECT r FROM RapportArchive r WHERE r.dateRedaction = :dateRedaction"),
		@NamedQuery(name = "RapportArchive.findByDescriptionRapport", query = "SELECT r FROM RapportArchive r WHERE r.descriptionRapport = :descriptionRapport")})
public class RapportArchive implements Serializable {
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
	private EquipeArchive idEquipe;
	@JoinColumn(name = "id_intervention", referencedColumnName = "id_intervention")
	@ManyToOne(optional = false)
	private InterventionArchive idIntervention;

	public RapportArchive() {
	}

	public RapportArchive(Long idRapport) {
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

	public EquipeArchive getIdEquipe() {
		return idEquipe;
	}

	public void setIdEquipe(EquipeArchive idEquipe) {
		this.idEquipe = idEquipe;
	}

	public InterventionArchive getIdIntervention() {
		return idIntervention;
	}

	public void setIdIntervention(InterventionArchive idIntervention) {
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
		if (!(object instanceof RapportArchive)) {
			return false;
		}
		RapportArchive other = (RapportArchive) object;
		if ((this.idRapport == null && other.idRapport != null)
				|| (this.idRapport != null && !this.idRapport
						.equals(other.idRapport))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.thedevbridge.gmaoapp.model.RapportArchive[ idRapport="
				+ idRapport + " ]";
	}

}
