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

import com.thedevbridge.gmaoapp.model.ExamplaireMateriel;
import com.thedevbridge.gmaoapp.model.Emplacement;
import com.thedevbridge.gmaoapp.model.Materiel;

/**
 * Backing bean for ExamplaireMateriel entities.
 * <p/>
 * This class provides CRUD functionality for all ExamplaireMateriel entities.
 * It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for
 * searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ExamplaireMaterielBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving ExamplaireMateriel entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ExamplaireMateriel examplaireMateriel;

	public ExamplaireMateriel getExamplaireMateriel() {
		return this.examplaireMateriel;
	}

	public void setExamplaireMateriel(ExamplaireMateriel examplaireMateriel) {
		this.examplaireMateriel = examplaireMateriel;
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
			this.examplaireMateriel = this.example;
		} else {
			this.examplaireMateriel = findById(getId());
		}
	}

	public ExamplaireMateriel findById(Long id) {

		return this.entityManager.find(ExamplaireMateriel.class, id);
	}

	/*
	 * Support updating and deleting ExamplaireMateriel entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.examplaireMateriel);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.examplaireMateriel);
				return "view?faces-redirect=true&id="
						+ this.examplaireMateriel.getIdExemplaireMateriel();
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
			ExamplaireMateriel deletableEntity = findById(getId());
			Emplacement idEmplacement = deletableEntity.getIdEmplacement();
			idEmplacement.getExamplaireMaterielList().remove(deletableEntity);
			deletableEntity.setIdEmplacement(null);
			this.entityManager.merge(idEmplacement);
			Materiel idMateriel = deletableEntity.getIdMateriel();
			idMateriel.getExamplaireMaterielList().remove(deletableEntity);
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
	 * Support searching ExamplaireMateriel entities with pagination
	 */

	private int page;
	private long count;
	private List<ExamplaireMateriel> pageItems;

	private ExamplaireMateriel example = new ExamplaireMateriel();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ExamplaireMateriel getExample() {
		return this.example;
	}

	public void setExample(ExamplaireMateriel example) {
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
		Root<ExamplaireMateriel> root = countCriteria
				.from(ExamplaireMateriel.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<ExamplaireMateriel> criteria = builder
				.createQuery(ExamplaireMateriel.class);
		root = criteria.from(ExamplaireMateriel.class);
		TypedQuery<ExamplaireMateriel> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<ExamplaireMateriel> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Integer quantiteExemplaire = this.example.getQuantiteExemplaire();
		if (quantiteExemplaire != null && quantiteExemplaire.intValue() != 0) {
			predicatesList.add(builder.equal(root.get("quantiteExemplaire"),
					quantiteExemplaire));
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
		String fabricantMarque = this.example.getFabricantMarque();
		if (fabricantMarque != null && !"".equals(fabricantMarque)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("fabricantMarque")),
					'%' + fabricantMarque.toLowerCase() + '%'));
		}
		Integer puissance = this.example.getPuissance();
		if (puissance != null && puissance.intValue() != 0) {
			predicatesList.add(builder.equal(root.get("puissance"), puissance));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ExamplaireMateriel> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ExamplaireMateriel entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<ExamplaireMateriel> getAll() {

		CriteriaQuery<ExamplaireMateriel> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(ExamplaireMateriel.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(ExamplaireMateriel.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ExamplaireMaterielBean ejbProxy = this.sessionContext
				.getBusinessObject(ExamplaireMaterielBean.class);

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

				return String.valueOf(((ExamplaireMateriel) value)
						.getIdExemplaireMateriel());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ExamplaireMateriel add = new ExamplaireMateriel();

	public ExamplaireMateriel getAdd() {
		return this.add;
	}

	public ExamplaireMateriel getAdded() {
		ExamplaireMateriel added = this.add;
		this.add = new ExamplaireMateriel();
		return added;
	}
}
