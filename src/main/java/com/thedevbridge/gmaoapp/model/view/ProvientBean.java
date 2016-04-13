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

import com.thedevbridge.gmaoapp.model.Provient;
import com.thedevbridge.gmaoapp.model.Materiel;
import com.thedevbridge.gmaoapp.model.Provenance;

/**
 * Backing bean for Provient entities.
 * <p/>
 * This class provides CRUD functionality for all Provient entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ProvientBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Provient entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Provient provient;

	public Provient getProvient() {
		return this.provient;
	}

	public void setProvient(Provient provient) {
		this.provient = provient;
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
			this.provient = this.example;
		} else {
			this.provient = findById(getId());
		}
	}

	public Provient findById(Long id) {

		return this.entityManager.find(Provient.class, id);
	}

	/*
	 * Support updating and deleting Provient entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.provient);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.provient);
				return "view?faces-redirect=true&id="
						+ this.provient.getIdProvient();
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
			Provient deletableEntity = findById(getId());
			Materiel idMateriel = deletableEntity.getIdMateriel();
			idMateriel.getProvientList().remove(deletableEntity);
			deletableEntity.setIdMateriel(null);
			this.entityManager.merge(idMateriel);
			Provenance idProvenance = deletableEntity.getIdProvenance();
			idProvenance.getProvientList().remove(deletableEntity);
			deletableEntity.setIdProvenance(null);
			this.entityManager.merge(idProvenance);
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
	 * Support searching Provient entities with pagination
	 */

	private int page;
	private long count;
	private List<Provient> pageItems;

	private Provient example = new Provient();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Provient getExample() {
		return this.example;
	}

	public void setExample(Provient example) {
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
		Root<Provient> root = countCriteria.from(Provient.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Provient> criteria = builder.createQuery(Provient.class);
		root = criteria.from(Provient.class);
		TypedQuery<Provient> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Provient> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Materiel idMateriel = this.example.getIdMateriel();
		if (idMateriel != null) {
			predicatesList
					.add(builder.equal(root.get("idMateriel"), idMateriel));
		}
		Provenance idProvenance = this.example.getIdProvenance();
		if (idProvenance != null) {
			predicatesList.add(builder.equal(root.get("idProvenance"),
					idProvenance));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Provient> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Provient entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Provient> getAll() {

		CriteriaQuery<Provient> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Provient.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Provient.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ProvientBean ejbProxy = this.sessionContext
				.getBusinessObject(ProvientBean.class);

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

				return String.valueOf(((Provient) value).getIdProvient());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Provient add = new Provient();

	public Provient getAdd() {
		return this.add;
	}

	public Provient getAdded() {
		Provient added = this.add;
		this.add = new Provient();
		return added;
	}
}
