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

import com.thedevbridge.gmaoapp.model.PlanificationPreventiveArchive;

/**
 * Backing bean for PlanificationPreventiveArchive entities.
 * <p/>
 * This class provides CRUD functionality for all PlanificationPreventiveArchive
 * entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PlanificationPreventiveArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving PlanificationPreventiveArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private PlanificationPreventiveArchive planificationPreventiveArchive;

	public PlanificationPreventiveArchive getPlanificationPreventiveArchive() {
		return this.planificationPreventiveArchive;
	}

	public void setPlanificationPreventiveArchive(
			PlanificationPreventiveArchive planificationPreventiveArchive) {
		this.planificationPreventiveArchive = planificationPreventiveArchive;
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
			this.planificationPreventiveArchive = this.example;
		} else {
			this.planificationPreventiveArchive = findById(getId());
		}
	}

	public PlanificationPreventiveArchive findById(Long id) {

		return this.entityManager
				.find(PlanificationPreventiveArchive.class, id);
	}

	/*
	 * Support updating and deleting PlanificationPreventiveArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.planificationPreventiveArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.planificationPreventiveArchive);
				return "view?faces-redirect=true&id="
						+ this.planificationPreventiveArchive
								.getIdPlanifPreventive();
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
			PlanificationPreventiveArchive deletableEntity = findById(getId());

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
	 * Support searching PlanificationPreventiveArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<PlanificationPreventiveArchive> pageItems;

	private PlanificationPreventiveArchive example = new PlanificationPreventiveArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public PlanificationPreventiveArchive getExample() {
		return this.example;
	}

	public void setExample(PlanificationPreventiveArchive example) {
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
		Root<PlanificationPreventiveArchive> root = countCriteria
				.from(PlanificationPreventiveArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<PlanificationPreventiveArchive> criteria = builder
				.createQuery(PlanificationPreventiveArchive.class);
		root = criteria.from(PlanificationPreventiveArchive.class);
		TypedQuery<PlanificationPreventiveArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(
			Root<PlanificationPreventiveArchive> root) {

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

	public List<PlanificationPreventiveArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back PlanificationPreventiveArchive entities
	 * (e.g. from inside an HtmlSelectOneMenu)
	 */

	public List<PlanificationPreventiveArchive> getAll() {

		CriteriaQuery<PlanificationPreventiveArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(
						PlanificationPreventiveArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria
						.from(PlanificationPreventiveArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final PlanificationPreventiveArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(PlanificationPreventiveArchiveBean.class);

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

				return String.valueOf(((PlanificationPreventiveArchive) value)
						.getIdPlanifPreventive());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private PlanificationPreventiveArchive add = new PlanificationPreventiveArchive();

	public PlanificationPreventiveArchive getAdd() {
		return this.add;
	}

	public PlanificationPreventiveArchive getAdded() {
		PlanificationPreventiveArchive added = this.add;
		this.add = new PlanificationPreventiveArchive();
		return added;
	}
}
