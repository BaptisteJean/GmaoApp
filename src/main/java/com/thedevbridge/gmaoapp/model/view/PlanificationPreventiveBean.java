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

import com.thedevbridge.gmaoapp.model.PlanificationPreventive;

/**
 * Backing bean for PlanificationPreventive entities.
 * <p/>
 * This class provides CRUD functionality for all PlanificationPreventive
 * entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PlanificationPreventiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving PlanificationPreventive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private PlanificationPreventive planificationPreventive;

	public PlanificationPreventive getPlanificationPreventive() {
		return this.planificationPreventive;
	}

	public void setPlanificationPreventive(
			PlanificationPreventive planificationPreventive) {
		this.planificationPreventive = planificationPreventive;
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
			this.planificationPreventive = this.example;
		} else {
			this.planificationPreventive = findById(getId());
		}
	}

	public PlanificationPreventive findById(Long id) {

		return this.entityManager.find(PlanificationPreventive.class, id);
	}

	/*
	 * Support updating and deleting PlanificationPreventive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.planificationPreventive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.planificationPreventive);
				return "view?faces-redirect=true&id="
						+ this.planificationPreventive.getIdPlanifPreventive();
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
			PlanificationPreventive deletableEntity = findById(getId());

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
	 * Support searching PlanificationPreventive entities with pagination
	 */

	private int page;
	private long count;
	private List<PlanificationPreventive> pageItems;

	private PlanificationPreventive example = new PlanificationPreventive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public PlanificationPreventive getExample() {
		return this.example;
	}

	public void setExample(PlanificationPreventive example) {
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
		Root<PlanificationPreventive> root = countCriteria
				.from(PlanificationPreventive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<PlanificationPreventive> criteria = builder
				.createQuery(PlanificationPreventive.class);
		root = criteria.from(PlanificationPreventive.class);
		TypedQuery<PlanificationPreventive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<PlanificationPreventive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String heureMaintenance = this.example.getHeureMaintenance();
		if (heureMaintenance != null && !"".equals(heureMaintenance)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("heureMaintenance")),
					'%' + heureMaintenance.toLowerCase() + '%'));
		}
		String tacheARealiser = this.example.getTacheARealiser();
		if (tacheARealiser != null && !"".equals(tacheARealiser)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("tacheARealiser")),
					'%' + tacheARealiser.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<PlanificationPreventive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back PlanificationPreventive entities (e.g.
	 * from inside an HtmlSelectOneMenu)
	 */

	public List<PlanificationPreventive> getAll() {

		CriteriaQuery<PlanificationPreventive> criteria = this.entityManager
				.getCriteriaBuilder()
				.createQuery(PlanificationPreventive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(PlanificationPreventive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final PlanificationPreventiveBean ejbProxy = this.sessionContext
				.getBusinessObject(PlanificationPreventiveBean.class);

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

				return String.valueOf(((PlanificationPreventive) value)
						.getIdPlanifPreventive());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private PlanificationPreventive add = new PlanificationPreventive();

	public PlanificationPreventive getAdd() {
		return this.add;
	}

	public PlanificationPreventive getAdded() {
		PlanificationPreventive added = this.add;
		this.add = new PlanificationPreventive();
		return added;
	}
}
