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

import com.thedevbridge.gmaoapp.model.Planification;
import com.thedevbridge.gmaoapp.model.ExamplaireMateriel;

/**
 * Backing bean for Planification entities.
 * <p/>
 * This class provides CRUD functionality for all Planification entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PlanificationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Planification entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Planification planification;

	public Planification getPlanification() {
		return this.planification;
	}

	public void setPlanification(Planification planification) {
		this.planification = planification;
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
			this.planification = this.example;
		} else {
			this.planification = findById(getId());
		}
	}

	public Planification findById(Long id) {

		return this.entityManager.find(Planification.class, id);
	}

	/*
	 * Support updating and deleting Planification entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.planification);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.planification);
				return "view?faces-redirect=true&id="
						+ this.planification.getIdPlanification();
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
			Planification deletableEntity = findById(getId());
			ExamplaireMateriel idExemplaireMat = deletableEntity
					.getIdExemplaireMat();
			idExemplaireMat.getPlanificationList().remove(deletableEntity);
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
	 * Support searching Planification entities with pagination
	 */

	private int page;
	private long count;
	private List<Planification> pageItems;

	private Planification example = new Planification();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Planification getExample() {
		return this.example;
	}

	public void setExample(Planification example) {
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
		Root<Planification> root = countCriteria.from(Planification.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Planification> criteria = builder
				.createQuery(Planification.class);
		root = criteria.from(Planification.class);
		TypedQuery<Planification> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Planification> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String datePlanification = this.example.getDatePlanification();
		if (datePlanification != null && !"".equals(datePlanification)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("datePlanification")),
					'%' + datePlanification.toLowerCase() + '%'));
		}
		ExamplaireMateriel idExemplaireMat = this.example.getIdExemplaireMat();
		if (idExemplaireMat != null) {
			predicatesList.add(builder.equal(root.get("idExemplaireMat"),
					idExemplaireMat));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Planification> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Planification entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<Planification> getAll() {

		CriteriaQuery<Planification> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Planification.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Planification.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final PlanificationBean ejbProxy = this.sessionContext
				.getBusinessObject(PlanificationBean.class);

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

				return String.valueOf(((Planification) value)
						.getIdPlanification());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Planification add = new Planification();

	public Planification getAdd() {
		return this.add;
	}

	public Planification getAdded() {
		Planification added = this.add;
		this.add = new Planification();
		return added;
	}
}
