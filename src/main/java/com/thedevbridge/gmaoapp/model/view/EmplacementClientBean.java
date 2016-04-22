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

import com.thedevbridge.gmaoapp.model.EmplacementClient;
import com.thedevbridge.gmaoapp.model.Emplacement;

/**
 * Backing bean for EmplacementClient entities.
 * <p/>
 * This class provides CRUD functionality for all EmplacementClient entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EmplacementClientBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving EmplacementClient entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private EmplacementClient emplacementClient;

	public EmplacementClient getEmplacementClient() {
		return this.emplacementClient;
	}

	public void setEmplacementClient(EmplacementClient emplacementClient) {
		this.emplacementClient = emplacementClient;
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
			this.emplacementClient = this.example;
		} else {
			this.emplacementClient = findById(getId());
		}
	}

	public EmplacementClient findById(Long id) {

		return this.entityManager.find(EmplacementClient.class, id);
	}

	/*
	 * Support updating and deleting EmplacementClient entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.emplacementClient);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.emplacementClient);
				return "view?faces-redirect=true&id="
						+ this.emplacementClient.getIdEmplclient();
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
			EmplacementClient deletableEntity = findById(getId());
			Emplacement idEmplacement = deletableEntity.getIdEmplacement();
			idEmplacement.getEmplacementClientList().remove(deletableEntity);
			deletableEntity.setIdEmplacement(null);
			this.entityManager.merge(idEmplacement);
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
	 * Support searching EmplacementClient entities with pagination
	 */

	private int page;
	private long count;
	private List<EmplacementClient> pageItems;

	private EmplacementClient example = new EmplacementClient();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public EmplacementClient getExample() {
		return this.example;
	}

	public void setExample(EmplacementClient example) {
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
		Root<EmplacementClient> root = countCriteria
				.from(EmplacementClient.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<EmplacementClient> criteria = builder
				.createQuery(EmplacementClient.class);
		root = criteria.from(EmplacementClient.class);
		TypedQuery<EmplacementClient> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<EmplacementClient> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String bloc = this.example.getBloc();
		if (bloc != null && !"".equals(bloc)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("bloc")),
					'%' + bloc.toLowerCase() + '%'));
		}
		String dateInstallation = this.example.getDateInstallation();
		if (dateInstallation != null && !"".equals(dateInstallation)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("dateInstallation")),
					'%' + dateInstallation.toLowerCase() + '%'));
		}
		String salle = this.example.getSalle();
		if (salle != null && !"".equals(salle)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("salle")),
					'%' + salle.toLowerCase() + '%'));
		}
		Emplacement idEmplacement = this.example.getIdEmplacement();
		if (idEmplacement != null) {
			predicatesList.add(builder.equal(root.get("idEmplacement"),
					idEmplacement));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<EmplacementClient> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back EmplacementClient entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<EmplacementClient> getAll() {

		CriteriaQuery<EmplacementClient> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(EmplacementClient.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(EmplacementClient.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final EmplacementClientBean ejbProxy = this.sessionContext
				.getBusinessObject(EmplacementClientBean.class);

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

				return String.valueOf(((EmplacementClient) value)
						.getIdEmplclient());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private EmplacementClient add = new EmplacementClient();

	public EmplacementClient getAdd() {
		return this.add;
	}

	public EmplacementClient getAdded() {
		EmplacementClient added = this.add;
		this.add = new EmplacementClient();
		return added;
	}
}
