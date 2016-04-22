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

import com.thedevbridge.gmaoapp.model.EquipeArchive;
import com.thedevbridge.gmaoapp.model.DepartementArchive;

/**
 * Backing bean for EquipeArchive entities.
 * <p/>
 * This class provides CRUD functionality for all EquipeArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EquipeArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving EquipeArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private EquipeArchive equipeArchive;

	public EquipeArchive getEquipeArchive() {
		return this.equipeArchive;
	}

	public void setEquipeArchive(EquipeArchive equipeArchive) {
		this.equipeArchive = equipeArchive;
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
			this.equipeArchive = this.example;
		} else {
			this.equipeArchive = findById(getId());
		}
	}

	public EquipeArchive findById(Long id) {

		return this.entityManager.find(EquipeArchive.class, id);
	}

	/*
	 * Support updating and deleting EquipeArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.equipeArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.equipeArchive);
				return "view?faces-redirect=true&id="
						+ this.equipeArchive.getIdEquipe();
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
			EquipeArchive deletableEntity = findById(getId());
			DepartementArchive idDepartemant = deletableEntity
					.getIdDepartemant();
			idDepartemant.getEquipeArchiveList().remove(deletableEntity);
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
	 * Support searching EquipeArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<EquipeArchive> pageItems;

	private EquipeArchive example = new EquipeArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public EquipeArchive getExample() {
		return this.example;
	}

	public void setExample(EquipeArchive example) {
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
		Root<EquipeArchive> root = countCriteria.from(EquipeArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<EquipeArchive> criteria = builder
				.createQuery(EquipeArchive.class);
		root = criteria.from(EquipeArchive.class);
		TypedQuery<EquipeArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<EquipeArchive> root) {

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
		DepartementArchive idDepartemant = this.example.getIdDepartemant();
		if (idDepartemant != null) {
			predicatesList.add(builder.equal(root.get("idDepartemant"),
					idDepartemant));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<EquipeArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back EquipeArchive entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<EquipeArchive> getAll() {

		CriteriaQuery<EquipeArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(EquipeArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(EquipeArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final EquipeArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(EquipeArchiveBean.class);

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

				return String.valueOf(((EquipeArchive) value).getIdEquipe());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private EquipeArchive add = new EquipeArchive();

	public EquipeArchive getAdd() {
		return this.add;
	}

	public EquipeArchive getAdded() {
		EquipeArchive added = this.add;
		this.add = new EquipeArchive();
		return added;
	}
}
