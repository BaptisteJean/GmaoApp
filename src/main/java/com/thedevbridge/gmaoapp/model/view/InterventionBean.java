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

import com.thedevbridge.gmaoapp.model.Intervention;
import com.thedevbridge.gmaoapp.model.DirecteurTechnique;
import com.thedevbridge.gmaoapp.model.ExamplaireMateriel;
import java.lang.Boolean;

/**
 * Backing bean for Intervention entities.
 * <p/>
 * This class provides CRUD functionality for all Intervention entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class InterventionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Intervention entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Intervention intervention;

	public Intervention getIntervention() {
		return this.intervention;
	}

	public void setIntervention(Intervention intervention) {
		this.intervention = intervention;
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
			this.intervention = this.example;
		} else {
			this.intervention = findById(getId());
		}
	}

	public Intervention findById(Long id) {

		return this.entityManager.find(Intervention.class, id);
	}

	/*
	 * Support updating and deleting Intervention entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.intervention);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.intervention);
				return "view?faces-redirect=true&id="
						+ this.intervention.getIdIntervention();
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
			Intervention deletableEntity = findById(getId());
			DirecteurTechnique idDirecteurTech = deletableEntity
					.getIdDirecteurTech();
			idDirecteurTech.getInterventionList().remove(deletableEntity);
			deletableEntity.setIdDirecteurTech(null);
			this.entityManager.merge(idDirecteurTech);
			ExamplaireMateriel idExemplaireMat = deletableEntity
					.getIdExemplaireMat();
			idExemplaireMat.getInterventionList().remove(deletableEntity);
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
	 * Support searching Intervention entities with pagination
	 */

	private int page;
	private long count;
	private List<Intervention> pageItems;

	private Intervention example = new Intervention();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Intervention getExample() {
		return this.example;
	}

	public void setExample(Intervention example) {
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
		Root<Intervention> root = countCriteria.from(Intervention.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Intervention> criteria = builder
				.createQuery(Intervention.class);
		root = criteria.from(Intervention.class);
		TypedQuery<Intervention> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Intervention> root) {

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
		Integer tempsMis = this.example.getTempsMis();
		if (tempsMis != null && tempsMis.intValue() != 0) {
			predicatesList.add(builder.equal(root.get("tempsMis"), tempsMis));
		}
		Boolean statutRapport = this.example.getStatutRapport();
		if (statutRapport != null) {
			predicatesList.add(builder.equal(root.get("statutRapport"),
					statutRapport));
		}
		DirecteurTechnique idDirecteurTech = this.example.getIdDirecteurTech();
		if (idDirecteurTech != null) {
			predicatesList.add(builder.equal(root.get("idDirecteurTech"),
					idDirecteurTech));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Intervention> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Intervention entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<Intervention> getAll() {

		CriteriaQuery<Intervention> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Intervention.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Intervention.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final InterventionBean ejbProxy = this.sessionContext
				.getBusinessObject(InterventionBean.class);

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

				return String.valueOf(((Intervention) value)
						.getIdIntervention());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Intervention add = new Intervention();

	public Intervention getAdd() {
		return this.add;
	}

	public Intervention getAdded() {
		Intervention added = this.add;
		this.add = new Intervention();
		return added;
	}
}
