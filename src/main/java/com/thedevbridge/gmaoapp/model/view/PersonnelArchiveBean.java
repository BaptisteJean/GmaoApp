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

import com.thedevbridge.gmaoapp.model.PersonnelArchive;

/**
 * Backing bean for PersonnelArchive entities.
 * <p/>
 * This class provides CRUD functionality for all PersonnelArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PersonnelArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving PersonnelArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private PersonnelArchive personnelArchive;

	public PersonnelArchive getPersonnelArchive() {
		return this.personnelArchive;
	}

	public void setPersonnelArchive(PersonnelArchive personnelArchive) {
		this.personnelArchive = personnelArchive;
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
			this.personnelArchive = this.example;
		} else {
			this.personnelArchive = findById(getId());
		}
	}

	public PersonnelArchive findById(Long id) {

		return this.entityManager.find(PersonnelArchive.class, id);
	}

	/*
	 * Support updating and deleting PersonnelArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.personnelArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.personnelArchive);
				return "view?faces-redirect=true&id="
						+ this.personnelArchive.getIdPersonnel();
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
			PersonnelArchive deletableEntity = findById(getId());

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
	 * Support searching PersonnelArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<PersonnelArchive> pageItems;

	private PersonnelArchive example = new PersonnelArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public PersonnelArchive getExample() {
		return this.example;
	}

	public void setExample(PersonnelArchive example) {
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
		Root<PersonnelArchive> root = countCriteria
				.from(PersonnelArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<PersonnelArchive> criteria = builder
				.createQuery(PersonnelArchive.class);
		root = criteria.from(PersonnelArchive.class);
		TypedQuery<PersonnelArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<PersonnelArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String nomPersonnel = this.example.getNomPersonnel();
		if (nomPersonnel != null && !"".equals(nomPersonnel)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("nomPersonnel")),
					'%' + nomPersonnel.toLowerCase() + '%'));
		}
		String prenomPersonnel = this.example.getPrenomPersonnel();
		if (prenomPersonnel != null && !"".equals(prenomPersonnel)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("prenomPersonnel")),
					'%' + prenomPersonnel.toLowerCase() + '%'));
		}
		String login = this.example.getLogin();
		if (login != null && !"".equals(login)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("login")),
					'%' + login.toLowerCase() + '%'));
		}
		String password = this.example.getPassword();
		if (password != null && !"".equals(password)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("password")),
					'%' + password.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<PersonnelArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back PersonnelArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<PersonnelArchive> getAll() {

		CriteriaQuery<PersonnelArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(PersonnelArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(PersonnelArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final PersonnelArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(PersonnelArchiveBean.class);

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

				return String.valueOf(((PersonnelArchive) value)
						.getIdPersonnel());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private PersonnelArchive add = new PersonnelArchive();

	public PersonnelArchive getAdd() {
		return this.add;
	}

	public PersonnelArchive getAdded() {
		PersonnelArchive added = this.add;
		this.add = new PersonnelArchive();
		return added;
	}
}
