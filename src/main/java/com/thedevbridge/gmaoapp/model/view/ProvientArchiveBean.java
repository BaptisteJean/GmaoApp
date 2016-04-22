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

import com.thedevbridge.gmaoapp.model.ProvientArchive;
import com.thedevbridge.gmaoapp.model.MaterielArchive;
import com.thedevbridge.gmaoapp.model.ProvenanceArchive;

/**
 * Backing bean for ProvientArchive entities.
 * <p/>
 * This class provides CRUD functionality for all ProvientArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ProvientArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ProvientArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ProvientArchive provientArchive;

	public ProvientArchive getProvientArchive() {
		return this.provientArchive;
	}

	public void setProvientArchive(ProvientArchive provientArchive) {
		this.provientArchive = provientArchive;
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
			this.provientArchive = this.example;
		} else {
			this.provientArchive = findById(getId());
		}
	}

	public ProvientArchive findById(Long id) {

		return this.entityManager.find(ProvientArchive.class, id);
	}

	/*
	 * Support updating and deleting ProvientArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.provientArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.provientArchive);
				return "view?faces-redirect=true&id="
						+ this.provientArchive.getIdProvient();
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
			ProvientArchive deletableEntity = findById(getId());
			MaterielArchive idMateriel = deletableEntity.getIdMateriel();
			idMateriel.getProvientArchiveList().remove(deletableEntity);
			deletableEntity.setIdMateriel(null);
			this.entityManager.merge(idMateriel);
			ProvenanceArchive idProvenance = deletableEntity.getIdProvenance();
			idProvenance.getProvientArchiveList().remove(deletableEntity);
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
	 * Support searching ProvientArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<ProvientArchive> pageItems;

	private ProvientArchive example = new ProvientArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ProvientArchive getExample() {
		return this.example;
	}

	public void setExample(ProvientArchive example) {
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
		Root<ProvientArchive> root = countCriteria.from(ProvientArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ProvientArchive> criteria = builder
				.createQuery(ProvientArchive.class);
		root = criteria.from(ProvientArchive.class);
		TypedQuery<ProvientArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ProvientArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		MaterielArchive idMateriel = this.example.getIdMateriel();
		if (idMateriel != null) {
			predicatesList
					.add(builder.equal(root.get("idMateriel"), idMateriel));
		}
		ProvenanceArchive idProvenance = this.example.getIdProvenance();
		if (idProvenance != null) {
			predicatesList.add(builder.equal(root.get("idProvenance"),
					idProvenance));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ProvientArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ProvientArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<ProvientArchive> getAll() {

		CriteriaQuery<ProvientArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ProvientArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ProvientArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ProvientArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(ProvientArchiveBean.class);

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

				return String
						.valueOf(((ProvientArchive) value).getIdProvient());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ProvientArchive add = new ProvientArchive();

	public ProvientArchive getAdd() {
		return this.add;
	}

	public ProvientArchive getAdded() {
		ProvientArchive added = this.add;
		this.add = new ProvientArchive();
		return added;
	}
}
