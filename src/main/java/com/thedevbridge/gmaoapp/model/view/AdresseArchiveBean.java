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

import com.thedevbridge.gmaoapp.model.AdresseArchive;

/**
 * Backing bean for AdresseArchive entities.
 * <p/>
 * This class provides CRUD functionality for all AdresseArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AdresseArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving AdresseArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private AdresseArchive adresseArchive;

	public AdresseArchive getAdresseArchive() {
		return this.adresseArchive;
	}

	public void setAdresseArchive(AdresseArchive adresseArchive) {
		this.adresseArchive = adresseArchive;
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
			this.adresseArchive = this.example;
		} else {
			this.adresseArchive = findById(getId());
		}
	}

	public AdresseArchive findById(Long id) {

		return this.entityManager.find(AdresseArchive.class, id);
	}

	/*
	 * Support updating and deleting AdresseArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.adresseArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.adresseArchive);
				return "view?faces-redirect=true&id="
						+ this.adresseArchive.getIdAdresse();
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
			AdresseArchive deletableEntity = findById(getId());

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
	 * Support searching AdresseArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<AdresseArchive> pageItems;

	private AdresseArchive example = new AdresseArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public AdresseArchive getExample() {
		return this.example;
	}

	public void setExample(AdresseArchive example) {
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
		Root<AdresseArchive> root = countCriteria.from(AdresseArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<AdresseArchive> criteria = builder
				.createQuery(AdresseArchive.class);
		root = criteria.from(AdresseArchive.class);
		TypedQuery<AdresseArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<AdresseArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String boitePostale = this.example.getBoitePostale();
		if (boitePostale != null && !"".equals(boitePostale)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("boitePostale")),
					'%' + boitePostale.toLowerCase() + '%'));
		}
		String codeAdresse = this.example.getCodeAdresse();
		if (codeAdresse != null && !"".equals(codeAdresse)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("codeAdresse")),
					'%' + codeAdresse.toLowerCase() + '%'));
		}
		String email = this.example.getEmail();
		if (email != null && !"".equals(email)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("email")),
					'%' + email.toLowerCase() + '%'));
		}
		String lieuDit = this.example.getLieuDit();
		if (lieuDit != null && !"".equals(lieuDit)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("lieuDit")),
					'%' + lieuDit.toLowerCase() + '%'));
		}
		String numeroPhone = this.example.getNumeroPhone();
		if (numeroPhone != null && !"".equals(numeroPhone)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("numeroPhone")),
					'%' + numeroPhone.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<AdresseArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back AdresseArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<AdresseArchive> getAll() {

		CriteriaQuery<AdresseArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(AdresseArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(AdresseArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final AdresseArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(AdresseArchiveBean.class);

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

				return String.valueOf(((AdresseArchive) value).getIdAdresse());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private AdresseArchive add = new AdresseArchive();

	public AdresseArchive getAdd() {
		return this.add;
	}

	public AdresseArchive getAdded() {
		AdresseArchive added = this.add;
		this.add = new AdresseArchive();
		return added;
	}
}
