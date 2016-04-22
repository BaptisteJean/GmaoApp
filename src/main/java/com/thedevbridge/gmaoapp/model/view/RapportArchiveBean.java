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

import com.thedevbridge.gmaoapp.model.RapportArchive;
import com.thedevbridge.gmaoapp.model.EquipeArchive;
import com.thedevbridge.gmaoapp.model.InterventionArchive;

/**
 * Backing bean for RapportArchive entities.
 * <p/>
 * This class provides CRUD functionality for all RapportArchive entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class RapportArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving RapportArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private RapportArchive rapportArchive;

	public RapportArchive getRapportArchive() {
		return this.rapportArchive;
	}

	public void setRapportArchive(RapportArchive rapportArchive) {
		this.rapportArchive = rapportArchive;
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
			this.rapportArchive = this.example;
		} else {
			this.rapportArchive = findById(getId());
		}
	}

	public RapportArchive findById(Long id) {

		return this.entityManager.find(RapportArchive.class, id);
	}

	/*
	 * Support updating and deleting RapportArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.rapportArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.rapportArchive);
				return "view?faces-redirect=true&id="
						+ this.rapportArchive.getIdRapport();
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
			RapportArchive deletableEntity = findById(getId());
			EquipeArchive idEquipe = deletableEntity.getIdEquipe();
			idEquipe.getRapportArchiveList().remove(deletableEntity);
			deletableEntity.setIdEquipe(null);
			this.entityManager.merge(idEquipe);
			InterventionArchive idIntervention = deletableEntity
					.getIdIntervention();
			idIntervention.getRapportArchiveList().remove(deletableEntity);
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
	 * Support searching RapportArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<RapportArchive> pageItems;

	private RapportArchive example = new RapportArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public RapportArchive getExample() {
		return this.example;
	}

	public void setExample(RapportArchive example) {
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
		Root<RapportArchive> root = countCriteria.from(RapportArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<RapportArchive> criteria = builder
				.createQuery(RapportArchive.class);
		root = criteria.from(RapportArchive.class);
		TypedQuery<RapportArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<RapportArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String dateRedaction = this.example.getDateRedaction();
		if (dateRedaction != null && !"".equals(dateRedaction)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("dateRedaction")),
					'%' + dateRedaction.toLowerCase() + '%'));
		}
		String descriptionRapport = this.example.getDescriptionRapport();
		if (descriptionRapport != null && !"".equals(descriptionRapport)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("descriptionRapport")),
					'%' + descriptionRapport.toLowerCase() + '%'));
		}
		EquipeArchive idEquipe = this.example.getIdEquipe();
		if (idEquipe != null) {
			predicatesList.add(builder.equal(root.get("idEquipe"), idEquipe));
		}
		InterventionArchive idIntervention = this.example.getIdIntervention();
		if (idIntervention != null) {
			predicatesList.add(builder.equal(root.get("idIntervention"),
					idIntervention));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<RapportArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back RapportArchive entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<RapportArchive> getAll() {

		CriteriaQuery<RapportArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(RapportArchive.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(RapportArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final RapportArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(RapportArchiveBean.class);

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

				return String.valueOf(((RapportArchive) value).getIdRapport());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private RapportArchive add = new RapportArchive();

	public RapportArchive getAdd() {
		return this.add;
	}

	public RapportArchive getAdded() {
		RapportArchive added = this.add;
		this.add = new RapportArchive();
		return added;
	}
}
