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

import com.thedevbridge.gmaoapp.model.Equipe;
import com.thedevbridge.gmaoapp.model.Departement;

/**
 * Backing bean for Equipe entities.
 * <p/>
 * This class provides CRUD functionality for all Equipe entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EquipeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Equipe entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Equipe equipe;

	public Equipe getEquipe() {
		return this.equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
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
			this.equipe = this.example;
		} else {
			this.equipe = findById(getId());
		}
	}

	public Equipe findById(Long id) {

		return this.entityManager.find(Equipe.class, id);
	}

	/*
	 * Support updating and deleting Equipe entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.equipe);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.equipe);
				return "view?faces-redirect=true&id="
						+ this.equipe.getIdEquipe();
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
			Equipe deletableEntity = findById(getId());
			Departement idDepartemant = deletableEntity.getIdDepartemant();
			idDepartemant.getEquipeList().remove(deletableEntity);
			deletableEntity.setIdDepartemant(null);
			this.entityManager.merge(idDepartemant);
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
	 * Support searching Equipe entities with pagination
	 */

	private int page;
	private long count;
	private List<Equipe> pageItems;

	private Equipe example = new Equipe();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Equipe getExample() {
		return this.example;
	}

	public void setExample(Equipe example) {
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
		Root<Equipe> root = countCriteria.from(Equipe.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Equipe> criteria = builder.createQuery(Equipe.class);
		root = criteria.from(Equipe.class);
		TypedQuery<Equipe> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Equipe> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String chefEquipe = this.example.getChefEquipe();
		if (chefEquipe != null && !"".equals(chefEquipe)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("chefEquipe")),
					'%' + chefEquipe.toLowerCase() + '%'));
		}
		String descriptionActiviteEquipe = this.example
				.getDescriptionActiviteEquipe();
		if (descriptionActiviteEquipe != null
				&& !"".equals(descriptionActiviteEquipe)) {
			predicatesList.add(builder.like(builder.lower(root
					.<String> get("descriptionActiviteEquipe")),
					'%' + descriptionActiviteEquipe.toLowerCase() + '%'));
		}
		String libelleEquipe = this.example.getLibelleEquipe();
		if (libelleEquipe != null && !"".equals(libelleEquipe)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("libelleEquipe")),
					'%' + libelleEquipe.toLowerCase() + '%'));
		}
		Departement idDepartemant = this.example.getIdDepartemant();
		if (idDepartemant != null) {
			predicatesList.add(builder.equal(root.get("idDepartemant"),
					idDepartemant));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Equipe> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Equipe entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Equipe> getAll() {

		CriteriaQuery<Equipe> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Equipe.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Equipe.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final EquipeBean ejbProxy = this.sessionContext
				.getBusinessObject(EquipeBean.class);

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

				return String.valueOf(((Equipe) value).getIdEquipe());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Equipe add = new Equipe();

	public Equipe getAdd() {
		return this.add;
	}

	public Equipe getAdded() {
		Equipe added = this.add;
		this.add = new Equipe();
		return added;
	}
}
