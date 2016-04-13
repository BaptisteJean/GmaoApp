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

import com.thedevbridge.gmaoapp.model.Adresse;

/**
 * Backing bean for Adresse entities.
 * <p/>
 * This class provides CRUD functionality for all Adresse entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AdresseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Adresse entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Adresse adresse;

	public Adresse getAdresse() {
		return this.adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
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
			this.adresse = this.example;
		} else {
			this.adresse = findById(getId());
		}
	}

	public Adresse findById(Long id) {

		return this.entityManager.find(Adresse.class, id);
	}

	/*
	 * Support updating and deleting Adresse entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.adresse);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.adresse);
				return "view?faces-redirect=true&id="
						+ this.adresse.getIdAdresse();
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
			Adresse deletableEntity = findById(getId());

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
	 * Support searching Adresse entities with pagination
	 */

	private int page;
	private long count;
	private List<Adresse> pageItems;

	private Adresse example = new Adresse();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Adresse getExample() {
		return this.example;
	}

	public void setExample(Adresse example) {
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
		Root<Adresse> root = countCriteria.from(Adresse.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Adresse> criteria = builder.createQuery(Adresse.class);
		root = criteria.from(Adresse.class);
		TypedQuery<Adresse> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Adresse> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String codeAdresse = this.example.getCodeAdresse();
		if (codeAdresse != null && !"".equals(codeAdresse)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("codeAdresse")),
					'%' + codeAdresse.toLowerCase() + '%'));
		}
		String boitePostale = this.example.getBoitePostale();
		if (boitePostale != null && !"".equals(boitePostale)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("boitePostale")),
					'%' + boitePostale.toLowerCase() + '%'));
		}
		String numeroPhone = this.example.getNumeroPhone();
		if (numeroPhone != null && !"".equals(numeroPhone)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("numeroPhone")),
					'%' + numeroPhone.toLowerCase() + '%'));
		}
		String email = this.example.getEmail();
		if (email != null && !"".equals(email)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("email")),
					'%' + email.toLowerCase() + '%'));
		}
		String region = this.example.getRegion();
		if (region != null && !"".equals(region)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("region")),
					'%' + region.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Adresse> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Adresse entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Adresse> getAll() {

		CriteriaQuery<Adresse> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Adresse.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Adresse.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final AdresseBean ejbProxy = this.sessionContext
				.getBusinessObject(AdresseBean.class);

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

				return String.valueOf(((Adresse) value).getIdAdresse());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Adresse add = new Adresse();

	public Adresse getAdd() {
		return this.add;
	}

	public Adresse getAdded() {
		Adresse added = this.add;
		this.add = new Adresse();
		return added;
	}
}
