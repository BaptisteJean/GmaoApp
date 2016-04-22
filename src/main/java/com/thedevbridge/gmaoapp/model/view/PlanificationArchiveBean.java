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

import com.thedevbridge.gmaoapp.model.PlanificationArchive;
import com.thedevbridge.gmaoapp.model.ExamplaireMaterielArchive;

/**
 * Backing bean for PlanificationArchive entities.
 * <p/>
 * This class provides CRUD functionality for all PlanificationArchive entities.
 * It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PlanificationArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving PlanificationArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private PlanificationArchive planificationArchive;

	public PlanificationArchive getPlanificationArchive() {
		return this.planificationArchive;
	}

	public void setPlanificationArchive(
			PlanificationArchive planificationArchive) {
		this.planificationArchive = planificationArchive;
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
			this.planificationArchive = this.example;
		} else {
			this.planificationArchive = findById(getId());
		}
	}

	public PlanificationArchive findById(Long id) {

		return this.entityManager.find(PlanificationArchive.class, id);
	}

	/*
	 * Support updating and deleting PlanificationArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.planificationArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.planificationArchive);
				return "view?faces-redirect=true&id="
						+ this.planificationArchive.getIdPlanification();
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
			PlanificationArchive deletableEntity = findById(getId());
			ExamplaireMaterielArchive idExemplaireMat = deletableEntity
					.getIdExemplaireMat();
			idExemplaireMat.getPlanificationArchiveList().remove(
					deletableEntity);
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
	 * Support searching PlanificationArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<PlanificationArchive> pageItems;

	private PlanificationArchive example = new PlanificationArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public PlanificationArchive getExample() {
		return this.example;
	}

	public void setExample(PlanificationArchive example) {
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
		Root<PlanificationArchive> root = countCriteria
				.from(PlanificationArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<PlanificationArchive> criteria = builder
				.createQuery(PlanificationArchive.class);
		root = criteria.from(PlanificationArchive.class);
		TypedQuery<PlanificationArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<PlanificationArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String datePlanification = this.example.getDatePlanification();
		if (datePlanification != null && !"".equals(datePlanification)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("datePlanification")),
					'%' + datePlanification.toLowerCase() + '%'));
		}
		ExamplaireMaterielArchive idExemplaireMat = this.example
				.getIdExemplaireMat();
		if (idExemplaireMat != null) {
			predicatesList.add(builder.equal(root.get("idExemplaireMat"),
					idExemplaireMat));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<PlanificationArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back PlanificationArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<PlanificationArchive> getAll() {

		CriteriaQuery<PlanificationArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(PlanificationArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(PlanificationArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final PlanificationArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(PlanificationArchiveBean.class);

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

				return String.valueOf(((PlanificationArchive) value)
						.getIdPlanification());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private PlanificationArchive add = new PlanificationArchive();

	public PlanificationArchive getAdd() {
		return this.add;
	}

	public PlanificationArchive getAdded() {
		PlanificationArchive added = this.add;
		this.add = new PlanificationArchive();
		return added;
	}
}
