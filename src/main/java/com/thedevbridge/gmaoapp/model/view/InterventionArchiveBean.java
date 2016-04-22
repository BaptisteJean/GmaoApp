package com.thedevbridge.gmaoapp.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.thedevbridge.gmaoapp.model.InterventionArchive;
import com.thedevbridge.gmaoapp.model.DirecteurTechniqueArchive;
import com.thedevbridge.gmaoapp.model.ExamplaireMaterielArchive;
import java.lang.Boolean;

/**
 * Backing bean for InterventionArchive entities.
 * <p/>
 * This class provides CRUD functionality for all InterventionArchive entities.
 * It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class InterventionArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving InterventionArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private InterventionArchive interventionArchive;

	public InterventionArchive getInterventionArchive() {
		return this.interventionArchive;
	}

	public void setInterventionArchive(InterventionArchive interventionArchive) {
		this.interventionArchive = interventionArchive;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "GmaoApp-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.interventionArchive = this.example;
		} else {
			this.interventionArchive = findById(getId());
		}
	}

	public InterventionArchive findById(Long id) {

		return this.entityManager.find(InterventionArchive.class, id);
	}

	/*
	 * Support updating and deleting InterventionArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.interventionArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.interventionArchive);
				return "view?faces-redirect=true&id="
						+ this.interventionArchive.getIdIntervention();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			InterventionArchive deletableEntity = findById(getId());
			DirecteurTechniqueArchive idDirecteurTech = deletableEntity
					.getIdDirecteurTech();
			idDirecteurTech.getInterventionArchiveList()
					.remove(deletableEntity);
			deletableEntity.setIdDirecteurTech(null);
			this.entityManager.merge(idDirecteurTech);
			ExamplaireMaterielArchive idExemplaireMat = deletableEntity
					.getIdExemplaireMat();
			idExemplaireMat.getInterventionArchiveList()
					.remove(deletableEntity);
			deletableEntity.setIdExemplaireMat(null);
			this.entityManager.merge(idExemplaireMat);
			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching InterventionArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<InterventionArchive> pageItems;

	private InterventionArchive example = new InterventionArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public InterventionArchive getExample() {
		return this.example;
	}

	public void setExample(InterventionArchive example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<InterventionArchive> root = countCriteria
				.from(InterventionArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<InterventionArchive> criteria = builder
				.createQuery(InterventionArchive.class);
		root = criteria.from(InterventionArchive.class);
		TypedQuery<InterventionArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<InterventionArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String dateIntervention = this.example.getDateIntervention();
		if (dateIntervention != null && !"".equals(dateIntervention)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("dateIntervention")),
					'%' + dateIntervention.toLowerCase() + '%'));
		}
		String descriptionIntervention = this.example
				.getDescriptionIntervention();
		if (descriptionIntervention != null
				&& !"".equals(descriptionIntervention)) {
			predicatesList
					.add(builder.like(builder.lower(root
							.<String> get("descriptionIntervention")),
							'%' + descriptionIntervention.toLowerCase() + '%'));
		}
		Boolean statutRapport = this.example.getStatutRapport();
		if (statutRapport != null) {
			predicatesList.add(builder.equal(root.get("statutRapport"),
					statutRapport));
		}
		Integer tempsMis = this.example.getTempsMis();
		if (tempsMis != null && tempsMis.intValue() != 0) {
			predicatesList.add(builder.equal(root.get("tempsMis"), tempsMis));
		}
		DirecteurTechniqueArchive idDirecteurTech = this.example
				.getIdDirecteurTech();
		if (idDirecteurTech != null) {
			predicatesList.add(builder.equal(root.get("idDirecteurTech"),
					idDirecteurTech));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<InterventionArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back InterventionArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<InterventionArchive> getAll() {

		CriteriaQuery<InterventionArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(InterventionArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(InterventionArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final InterventionArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(InterventionArchiveBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((InterventionArchive) value)
						.getIdIntervention());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private InterventionArchive add = new InterventionArchive();

	public InterventionArchive getAdd() {
		return this.add;
	}

	public InterventionArchive getAdded() {
		InterventionArchive added = this.add;
		this.add = new InterventionArchive();
		return added;
	}
}
