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

import com.thedevbridge.gmaoapp.model.ExamplaireMaterielArchive;
import com.thedevbridge.gmaoapp.model.EmplacementArchive;
import com.thedevbridge.gmaoapp.model.MaterielArchive;

/**
 * Backing bean for ExamplaireMaterielArchive entities.
 * <p/>
 * This class provides CRUD functionality for all ExamplaireMaterielArchive
 * entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ExamplaireMaterielArchiveBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ExamplaireMaterielArchive entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ExamplaireMaterielArchive examplaireMaterielArchive;

	public ExamplaireMaterielArchive getExamplaireMaterielArchive() {
		return this.examplaireMaterielArchive;
	}

	public void setExamplaireMaterielArchive(
			ExamplaireMaterielArchive examplaireMaterielArchive) {
		this.examplaireMaterielArchive = examplaireMaterielArchive;
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
			this.examplaireMaterielArchive = this.example;
		} else {
			this.examplaireMaterielArchive = findById(getId());
		}
	}

	public ExamplaireMaterielArchive findById(Long id) {

		return this.entityManager.find(ExamplaireMaterielArchive.class, id);
	}

	/*
	 * Support updating and deleting ExamplaireMaterielArchive entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.examplaireMaterielArchive);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.examplaireMaterielArchive);
				return "view?faces-redirect=true&id="
						+ this.examplaireMaterielArchive
								.getIdExemplaireMateriel();
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
			ExamplaireMaterielArchive deletableEntity = findById(getId());
			EmplacementArchive idEmplacement = deletableEntity
					.getIdEmplacement();
			idEmplacement.getExamplaireMaterielArchiveList().remove(
					deletableEntity);
			deletableEntity.setIdEmplacement(null);
			this.entityManager.merge(idEmplacement);
			MaterielArchive idMateriel = deletableEntity.getIdMateriel();
			idMateriel.getExamplaireMaterielArchiveList().remove(
					deletableEntity);
			deletableEntity.setIdMateriel(null);
			this.entityManager.merge(idMateriel);
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
	 * Support searching ExamplaireMaterielArchive entities with pagination
	 */

	private int page;
	private long count;
	private List<ExamplaireMaterielArchive> pageItems;

	private ExamplaireMaterielArchive example = new ExamplaireMaterielArchive();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ExamplaireMaterielArchive getExample() {
		return this.example;
	}

	public void setExample(ExamplaireMaterielArchive example) {
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
		Root<ExamplaireMaterielArchive> root = countCriteria
				.from(ExamplaireMaterielArchive.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ExamplaireMaterielArchive> criteria = builder
				.createQuery(ExamplaireMaterielArchive.class);
		root = criteria.from(ExamplaireMaterielArchive.class);
		TypedQuery<ExamplaireMaterielArchive> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ExamplaireMaterielArchive> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String fabricantMarque = this.example.getFabricantMarque();
		if (fabricantMarque != null && !"".equals(fabricantMarque)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("fabricantMarque")),
					'%' + fabricantMarque.toLowerCase() + '%'));
		}
		String libelleExemplaire = this.example.getLibelleExemplaire();
		if (libelleExemplaire != null && !"".equals(libelleExemplaire)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("libelleExemplaire")),
					'%' + libelleExemplaire.toLowerCase() + '%'));
		}
		String numeroSerie = this.example.getNumeroSerie();
		if (numeroSerie != null && !"".equals(numeroSerie)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("numeroSerie")),
					'%' + numeroSerie.toLowerCase() + '%'));
		}
		Integer puissance = this.example.getPuissance();
		if (puissance != null && puissance.intValue() != 0) {
			predicatesList.add(builder.equal(root.get("puissance"), puissance));
		}
		Integer quantiteExemplaire = this.example.getQuantiteExemplaire();
		if (quantiteExemplaire != null && quantiteExemplaire.intValue() != 0) {
			predicatesList.add(builder.equal(root.get("quantiteExemplaire"),
					quantiteExemplaire));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ExamplaireMaterielArchive> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ExamplaireMaterielArchive entities (e.g.
	 * from inside an HtmlSelectOneMenu)
	 */

	public List<ExamplaireMaterielArchive> getAll() {

		CriteriaQuery<ExamplaireMaterielArchive> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(
						ExamplaireMaterielArchive.class);
		return this.entityManager
				.createQuery(
						criteria.select(criteria
								.from(ExamplaireMaterielArchive.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ExamplaireMaterielArchiveBean ejbProxy = this.sessionContext
				.getBusinessObject(ExamplaireMaterielArchiveBean.class);

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

				return String.valueOf(((ExamplaireMaterielArchive) value)
						.getIdExemplaireMateriel());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ExamplaireMaterielArchive add = new ExamplaireMaterielArchive();

	public ExamplaireMaterielArchive getAdd() {
		return this.add;
	}

	public ExamplaireMaterielArchive getAdded() {
		ExamplaireMaterielArchive added = this.add;
		this.add = new ExamplaireMaterielArchive();
		return added;
	}
}
