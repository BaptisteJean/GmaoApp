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

import com.thedevbridge.gmaoapp.model.FournisseurArchive;
import com.thedevbridge.gmaoapp.model.AdresseArchive;

/**
 * Backing bean for FournisseurArchive entities.
 * <p/>
 * This class provides CRUD functionality for all FournisseurArchive entities.
 * It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class FournisseurArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving FournisseurArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private FournisseurArchive fournisseurArchive;

	public FournisseurArchive getFournisseurArchive() {
		return this.fournisseurArchive;
	}

	public void setFournisseurArchive(FournisseurArchive fournisseurArchive) {
		this.fournisseurArchive = fournisseurArchive;
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
			this.fournisseurArchive = this.example;
		} else {
			this.fournisseurArchive = findById(getId());
		}
	}

	public FournisseurArchive findById(Long id) {

		return this.entityManager.find(FournisseurArchive.class, id);
	}

	/*
	 * Support updating and deleting FournisseurArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.fournisseurArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.fournisseurArchive);
				return "view?faces-redirect=true&id="
						+ this.fournisseurArchive.getIdFournisseur();
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
			FournisseurArchive deletableEntity = findById(getId());
			AdresseArchive idAdresse = deletableEntity.getIdAdresse();
			idAdresse.getFournisseurArchiveList().remove(deletableEntity);
			deletableEntity.setIdAdresse(null);
			this.entityManager.merge(idAdresse);
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
	 * Support searching FournisseurArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<FournisseurArchive> pageItems;

	private FournisseurArchive example = new FournisseurArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public FournisseurArchive getExample() {
		return this.example;
	}

	public void setExample(FournisseurArchive example) {
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
		Root<FournisseurArchive> root = countCriteria
				.from(FournisseurArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<FournisseurArchive> criteria = builder
				.createQuery(FournisseurArchive.class);
		root = criteria.from(FournisseurArchive.class);
		TypedQuery<FournisseurArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<FournisseurArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String raisonSocial = this.example.getRaisonSocial();
		if (raisonSocial != null && !"".equals(raisonSocial)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("raisonSocial")),
					'%' + raisonSocial.toLowerCase() + '%'));
		}
		AdresseArchive idAdresse = this.example.getIdAdresse();
		if (idAdresse != null) {
			predicatesList.add(builder.equal(root.get("idAdresse"), idAdresse));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<FournisseurArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back FournisseurArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<FournisseurArchive> getAll() {

		CriteriaQuery<FournisseurArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(FournisseurArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(FournisseurArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final FournisseurArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(FournisseurArchiveBean.class);

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

				return String.valueOf(((FournisseurArchive) value)
						.getIdFournisseur());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private FournisseurArchive add = new FournisseurArchive();

	public FournisseurArchive getAdd() {
		return this.add;
	}

	public FournisseurArchive getAdded() {
		FournisseurArchive added = this.add;
		this.add = new FournisseurArchive();
		return added;
	}
}
