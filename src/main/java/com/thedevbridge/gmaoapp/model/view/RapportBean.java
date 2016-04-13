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

import com.thedevbridge.gmaoapp.model.Rapport;
import com.thedevbridge.gmaoapp.model.Equipe;
import com.thedevbridge.gmaoapp.model.Intervention;

/**
 * Backing bean for Rapport entities.
 * <p/>
 * This class provides CRUD functionality for all Rapport entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class RapportBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Rapport entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Rapport rapport;

	public Rapport getRapport() {
		return this.rapport;
	}

	public void setRapport(Rapport rapport) {
		this.rapport = rapport;
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
			this.rapport = this.example;
		} else {
			this.rapport = findById(getId());
		}
	}

	public Rapport findById(Long id) {

		return this.entityManager.find(Rapport.class, id);
	}

	/*
	 * Support updating and deleting Rapport entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.rapport);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.rapport);
				return "view?faces-redirect=true&id="
						+ this.rapport.getIdRapport();
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
			Rapport deletableEntity = findById(getId());
			Equipe idEquipe = deletableEntity.getIdEquipe();
			idEquipe.getRapportList().remove(deletableEntity);
			deletableEntity.setIdEquipe(null);
			this.entityManager.merge(idEquipe);
			Intervention idIntervention = deletableEntity.getIdIntervention();
			idIntervention.getRapportList().remove(deletableEntity);
			deletableEntity.setIdIntervention(null);
			this.entityManager.merge(idIntervention);
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
	 * Support searching Rapport entities with pagination
	 */

	private int page;
	private long count;
	private List<Rapport> pageItems;

	private Rapport example = new Rapport();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Rapport getExample() {
		return this.example;
	}

	public void setExample(Rapport example) {
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
		Root<Rapport> root = countCriteria.from(Rapport.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Rapport> criteria = builder.createQuery(Rapport.class);
		root = criteria.from(Rapport.class);
		TypedQuery<Rapport> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Rapport> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String descriptionRapport = this.example.getDescriptionRapport();
		if (descriptionRapport != null && !"".equals(descriptionRapport)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("descriptionRapport")),
					'%' + descriptionRapport.toLowerCase() + '%'));
		}
		String dateRedaction = this.example.getDateRedaction();
		if (dateRedaction != null && !"".equals(dateRedaction)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("dateRedaction")),
					'%' + dateRedaction.toLowerCase() + '%'));
		}
		Equipe idEquipe = this.example.getIdEquipe();
		if (idEquipe != null) {
			predicatesList.add(builder.equal(root.get("idEquipe"), idEquipe));
		}
		Intervention idIntervention = this.example.getIdIntervention();
		if (idIntervention != null) {
			predicatesList.add(builder.equal(root.get("idIntervention"),
					idIntervention));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Rapport> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Rapport entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Rapport> getAll() {

		CriteriaQuery<Rapport> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Rapport.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Rapport.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final RapportBean ejbProxy = this.sessionContext
				.getBusinessObject(RapportBean.class);

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

				return String.valueOf(((Rapport) value).getIdRapport());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Rapport add = new Rapport();

	public Rapport getAdd() {
		return this.add;
	}

	public Rapport getAdded() {
		Rapport added = this.add;
		this.add = new Rapport();
		return added;
	}
}
