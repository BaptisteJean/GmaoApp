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

import com.thedevbridge.gmaoapp.model.PlanificationCurative;

/**
 * Backing bean for PlanificationCurative entities.
 * <p/>
 * This class provides CRUD functionality for all PlanificationCurative
 * entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PlanificationCurativeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving PlanificationCurative entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private PlanificationCurative planificationCurative;

	public PlanificationCurative getPlanificationCurative() {
		return this.planificationCurative;
	}

	public void setPlanificationCurative(
			PlanificationCurative planificationCurative) {
		this.planificationCurative = planificationCurative;
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
			this.planificationCurative = this.example;
		} else {
			this.planificationCurative = findById(getId());
		}
	}

	public PlanificationCurative findById(Long id) {

		return this.entityManager.find(PlanificationCurative.class, id);
	}

	/*
	 * Support updating and deleting PlanificationCurative entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.planificationCurative);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.planificationCurative);
				return "view?faces-redirect=true&id="
						+ this.planificationCurative.getIdPlanifCurative();
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
			PlanificationCurative deletableEntity = findById(getId());

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
	 * Support searching PlanificationCurative entities with pagination
	 */

	private int page;
	private long count;
	private List<PlanificationCurative> pageItems;

	private PlanificationCurative example = new PlanificationCurative();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public PlanificationCurative getExample() {
		return this.example;
	}

	public void setExample(PlanificationCurative example) {
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
		Root<PlanificationCurative> root = countCriteria
				.from(PlanificationCurative.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<PlanificationCurative> criteria = builder
				.createQuery(PlanificationCurative.class);
		root = criteria.from(PlanificationCurative.class);
		TypedQuery<PlanificationCurative> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<PlanificationCurative> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String descriptionProbleme = this.example.getDescriptionProbleme();
		if (descriptionProbleme != null && !"".equals(descriptionProbleme)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("descriptionProbleme")),
					'%' + descriptionProbleme.toLowerCase() + '%'));
		}
		String etatMaterielApresIntervention = this.example
				.getEtatMaterielApresIntervention();
		if (etatMaterielApresIntervention != null
				&& !"".equals(etatMaterielApresIntervention)) {
			predicatesList.add(builder.like(builder.lower(root
					.<String> get("etatMaterielApresIntervention")),
					'%' + etatMaterielApresIntervention.toLowerCase() + '%'));
		}
		String heureDebut = this.example.getHeureDebut();
		if (heureDebut != null && !"".equals(heureDebut)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("heureDebut")),
					'%' + heureDebut.toLowerCase() + '%'));
		}
		String heureFin = this.example.getHeureFin();
		if (heureFin != null && !"".equals(heureFin)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("heureFin")),
					'%' + heureFin.toLowerCase() + '%'));
		}
		String solutionApportee = this.example.getSolutionApportee();
		if (solutionApportee != null && !"".equals(solutionApportee)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("solutionApportee")),
					'%' + solutionApportee.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<PlanificationCurative> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back PlanificationCurative entities (e.g.
	 * from inside an HtmlSelectOneMenu)
	 */

	public List<PlanificationCurative> getAll() {

		CriteriaQuery<PlanificationCurative> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(PlanificationCurative.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(PlanificationCurative.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final PlanificationCurativeBean ejbProxy = this.sessionContext
				.getBusinessObject(PlanificationCurativeBean.class);

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

				return String.valueOf(((PlanificationCurative) value)
						.getIdPlanifCurative());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private PlanificationCurative add = new PlanificationCurative();

	public PlanificationCurative getAdd() {
		return this.add;
	}

	public PlanificationCurative getAdded() {
		PlanificationCurative added = this.add;
		this.add = new PlanificationCurative();
		return added;
	}
}
